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
            //Builds a "fat-jar", which includes all dependencies marked 'compile' into the output jar
            from(project.configurations.runtimeClasspath.collect { it.isDirectory() ? it : project.zipTree(it) })

            if (Flags.useFrcManifest) { //If we should apply the FRC manifest
                manifest {
                    attributes 'Main-Class': Constants.FRC_MAIN_CLASS
                    attributes 'Robot-Class': Constants.FRC_ROBOT_CLASS
                }
            }
        }
    }
}
