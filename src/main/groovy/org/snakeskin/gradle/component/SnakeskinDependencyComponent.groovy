package org.snakeskin.gradle.component

import org.gradle.api.Project
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
        project.extensions.create("snakeskin", SnakeskinExtension, project.objects) //Create the 'snakeskin' DSL block

        if (project.plugins.hasPlugin("kotlin")) {
            project.plugins.apply("kotlin-kapt") //If the project has kotlin, we need to apply kotlin-kapt to do annotation processing
        }
    }

    void afterEvaluate(Project project) {
        def extension = project.extensions.getByType(SnakeskinExtension)

        boolean hasKapt = project.plugins.hasPlugin('kotlin-kapt')

        def compileDeps = project.configurations.compile.dependencies
        def kaptDeps = hasKapt ? project.configurations.kapt.dependencies : null
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
                    compileDeps.add(project.dependencies.create(snakeskinDep(it, extension.version)))
                    //Add the dependency to the 'compile' configuration
                }

                if (hasKapt) { //If we have kapt support
                    extension.modules.kaptDeps.each {
                        //Iterate the kapt deps specified by our DSL extension (should only be Core, not accessible to the DSL)
                        kaptDeps.add(project.dependencies.create(snakeskinDep(it, extension.version)))
                        //Add the dependency to the 'kapt' configuration
                    }
                }

                project.gradle.removeListener(this) //Remove ourselves from the listeners since we're done
            }

            @Override
            void afterResolve(ResolvableDependencies dependencies) {}
        })
    }
}