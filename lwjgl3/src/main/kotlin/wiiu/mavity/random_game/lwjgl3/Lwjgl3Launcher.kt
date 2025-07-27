@file:JvmName("Lwjgl3Launcher")

package wiiu.mavity.random_game.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.*

import wiiu.mavity.random_game.*
import wiiu.mavity.random_game.util.Options

/** Launches the desktop (LWJGL3) application. */
fun main(args: Array<String>) = Options.initMain(args) {
	platformHelper()
	Lwjgl3Application(
		if (it.distribution == "CLIENT") Client else Server,
		Lwjgl3ApplicationConfiguration().apply {
			setTitle("RandomGame")
			setWindowedMode(640, 480)
			setWindowIcon(
				*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray())
			)
		}
	)
}

fun platformHelper() {
	// Here, we are trying to work around an issue with how LWJGL3 loads its extracted .dll files.
	// By default, LWJGL3 extracts to the directory specified by "java.io.tmpdir", which is usually the user's home.
	// If the user's name has non-ASCII (or some non-alphanumeric) characters in it, that would fail.
	// By extracting to the relevant "ProgramData" folder, which is usually "C:\ProgramData", we avoid this.
	if ("windows" in System.getProperty("os.name").lowercase()) System.setProperty("java.io.tmpdir", "${System.getenv("ProgramData")}/libGDX-temp")
}