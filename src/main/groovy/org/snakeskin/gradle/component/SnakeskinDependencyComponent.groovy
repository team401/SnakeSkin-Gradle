package org.snakeskin.gradle.component

import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState
import org.gradle.api.artifacts.DependencyResolutionListener
import org.gradle.api.artifacts.ResolvableDependencies
import org.snakeskin.gradle.Constants
import org.snakeskin.gradle.dsl.SnakeskinExtension

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
class SnakeskinDependencyComponent {
    private static String snakeskinDep(String module, String version) {
        return "org.snakeskin:$module:$version"
    }

    void beforeEvaluate(Project project) {
        def extension = project.extensions.create("snakeskin", SnakeskinExtension, project.objects) //Create the 'snakeskin' DSL block

        try {
            project.plugins.apply("kotlin-kapt")
        } catch (ignored) {}

        project.gradle.addProjectEvaluationListener(new ProjectEvaluationListener() {
            @Override
            void beforeEvaluate(Project projectIn) {

            }

            @Override
            void afterEvaluate(Project projectIn, ProjectState state) {
                def newExtension = projectIn.extensions.getByType(SnakeskinExtension)

                try {
                    newExtension.modules.kaptDeps.each {
                        println("[SnakeSkin] Successfully registered annotation processor")
                        projectIn.configurations.kapt.dependencies.add(projectIn.dependencies.create(snakeskinDep(it, extension.version)))
                    }
                } catch (ignored) {}
            }
        })
    }

    void afterEvaluate(Project project) {
        def extension = project.extensions.getByType(SnakeskinExtension)

        def repositories = project.repositories

        project.gradle.addListener(new DependencyResolutionListener() {
            @Override
            void beforeResolve(ResolvableDependencies dependencies) {
                //Add the SnakeSkin Bintray maven repository
                repositories.add(repositories.maven {
                    name "SnakeSkin Bintray"
                    url Constants.SNAKESKIN_BINTRAY_URL
                })

                extension.modules.compileDeps.each { //Iterate the compile deps specified by our DSL extension
                    project.configurations.compile.dependencies.add(project.dependencies.create(snakeskinDep(it, extension.version)))
                    //Add the dependency to the 'compile' configuration
                }

                project.gradle.removeListener(this) //Remove ourselves from the listeners since we're done
            }

            @Override
            void afterResolve(ResolvableDependencies dependencies) {}
        })
    }
}