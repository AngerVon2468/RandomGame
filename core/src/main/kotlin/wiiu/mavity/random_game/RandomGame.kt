package wiiu.mavity.random_game

import com.badlogic.gdx.*

import ktx.log.*

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
object RandomGame : ApplicationAdapter() {

    override fun create() {
        Gdx.app.applicationLogger = Logging
        info {
            "Test"
        }
    }
}