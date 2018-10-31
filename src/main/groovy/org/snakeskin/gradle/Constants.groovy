package org.snakeskin.gradle

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
class Constants {
    static final String SNAKESKIN_CORE = "SnakeSkin-Core"
    static final String SNAKESKIN_FRC  = "SnakeSkin-FRC"
    static final String SNAKESKIN_CTRE = "SnakeSkin-CTRE"

    static final String SNAKESKIN_BINTRAY_URL = "http://dl.bintray.com/team401/SnakeSkin"

    static final String INDEX_PATH = "META-INF/services/org.snakeskin.compiler.AnnotatedRunnable"
    static final String INDEX_MATCHER = "**/" + INDEX_PATH

    static final String FRC_MAIN_CLASS = "org.snakeskin.SnakeskinMain" //Updated for WPILib 2019+
    static final String FRC_ROBOT_CLASS = "org.snakeskin.Robot" //No longer required
}
