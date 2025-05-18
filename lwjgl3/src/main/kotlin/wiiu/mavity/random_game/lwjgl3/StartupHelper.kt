package wiiu.mavity.random_game.lwjgl3

/**
 * Restarting the JVM and re-linking I/O is really fucking cursed. Don't do that shit. Also, fuck macOS.
 */
object StartupHelper {

    fun helpMe() {
        // Here, we are trying to work around an issue with how LWJGL3 loads its extracted .dll files.
        // By default, LWJGL3 extracts to the directory specified by "java.io.tmpdir", which is usually the user's home.
        // If the user's name has non-ASCII (or some non-alphanumeric) characters in it, that would fail.
        // By extracting to the relevant "ProgramData" folder, which is usually "C:\ProgramData", we avoid this.
        if (System.getProperty("os.name").contains(other = "windows", ignoreCase = true))
            System.setProperty("java.io.tmpdir", System.getenv("ProgramData") + "/libGDX-temp")
    }
}