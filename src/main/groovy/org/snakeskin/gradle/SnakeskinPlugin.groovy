package org.snakeskin.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.snakeskin.gradle.component.SnakeskinDependencyComponent
import org.snakeskin.gradle.component.SnakeskinJarComponent

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
class SnakeskinPlugin implements Plugin<Project> {
    @Override
    void apply(Project target) {
        def depComponent = new SnakeskinDependencyComponent()
        def jarComponent = new SnakeskinJarComponent()

        depComponent.beforeEvaluate(target)
        jarComponent.beforeEvaluate(target)

        target.afterEvaluate {
            depComponent.afterEvaluate(target)
            jarComponent.afterEvaluate(target)
        }
    }
}
