package org.snakeskin.gradle.component

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.snakeskin.gradle.Constants
import org.snakeskin.gradle.Flags

/*
 * snakeskin-gradle - Created on 6/24/18
 * Author: Cameron Earle
 * 
 * This code is licensed under the GNU GPL v3
 * You can find more info in the LICENSE file at project root
 */

/**
 * @author Cameron Earle
 * @version 6/24/18
 */
class SnakeskinJarComponent {
    void beforeEvaluate(Project project) {}

    void afterEvaluate(Project project) {
        project.tasks.withType(Jar) { //Apply this to any task that deals with jars
            doFirst {
                def lines = new LinkedHashSet<String>() //Set to store the lines that will go in the final index file

                //Find dependency index files
                for (dep in project.configurations.runtime) { //Iterate each dependency
                    project.zipTree(dep).matching { include Constants.INDEX_MATCHER }.each { //Iterate each file
                        lines.addAll(it.readLines()) //Load all of the lines from the file into the set
                    }
                }

                //Find our index files
                project.fileTree(project.buildDir).matching { include Constants.INDEX_MATCHER }.each { //Iterate each file (there should only be one)
                    lines.addAll(it.readLines()) //Load all of the lines from the file into the set
                }

                def serviceFile = project.file("${project.buildDir}/${Constants.INDEX_PATH}") //The final output service file
                serviceFile.parentFile.mkdirs() //Make any missing directories
                if (serviceFile.exists()) serviceFile.delete() //Delete any existing service file
                serviceFile.createNewFile() //Create a new, blank service file

                lines.each { //Iterate the set of lines
                    serviceFile << it << "\n" //Add the line from the set into the file
                }
            }

            //Builds a "fat-jar", which includes all dependencies marked 'compile' into the output jar
            from(project.configurations.compile.collect { it.isDirectory() ? it : project.zipTree(it) }) {
                exclude(Constants.INDEX_PATH) //Prevents java from handling our index file, we'll handle it below
            }

            from project.fileTree(project.buildDir).matching { include Constants.INDEX_PATH } //Includes the index file we just combined into the output jar

            if (Flags.useFrcManifest) { //If we should apply the FRC manifest
                manifest {
                    attributes 'Main-Class': Constants.FRC_MAIN_CLASS
                    attributes 'Robot-Class': Constants.FRC_ROBOT_CLASS
                }
            }
        }
    }
}
