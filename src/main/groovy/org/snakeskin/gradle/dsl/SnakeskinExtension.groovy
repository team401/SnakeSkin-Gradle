package org.snakeskin.gradle.dsl

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory

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
class SnakeskinExtension {
    String version = "+" //Default to latest version
    final Modules modules

    @javax.inject.Inject
    SnakeskinExtension(ObjectFactory objectFactory) {
        modules = objectFactory.newInstance(Modules)
    }

    void modules(Action<? super Modules> action) {
        action.execute(modules)
    }
}