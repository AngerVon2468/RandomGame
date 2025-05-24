@file:JvmName("Lwjgl3Launcher")

package wiiu.mavity.random_game.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.*

import wiiu.mavity.random_game.Main

/** Launches the desktop (LWJGL3) application. */
fun main() {
    StartupHelper.helpMe()
    Lwjgl3Application(Main, Lwjgl3ApplicationConfiguration().apply {
        setTitle("RandomGame")
        setWindowedMode(640, 480)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}