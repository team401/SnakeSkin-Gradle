package org.snakeskin.gradle.dsl

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
class Modules {
    def compileDeps = new LinkedHashSet<String>()
    def kaptDeps = new LinkedHashSet<String>()

    void core() {
        compileDeps.add(Constants.SNAKESKIN_CORE)
        kaptDeps.add(Constants.SNAKESKIN_CORE)
    }

    void frc() {
        compileDeps.add(Constants.SNAKESKIN_FRC)
        Flags.useFrcManifest = true //Mark this true so the Jar Component knows to add the FRC manifest
    }

    void ctre() {
        compileDeps.add(Constants.SNAKESKIN_CTRE)
    }
}