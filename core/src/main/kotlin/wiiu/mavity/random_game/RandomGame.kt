package wiiu.mavity.random_game

import com.badlogic.gdx.*

import ktx.app.KtxApplicationAdapter
import ktx.async.KtxAsync
import ktx.log.*

object RandomGame : KtxApplicationAdapter {

    override fun create() {
        KtxAsync.initiate()
        Gdx.app.applicationLogger = Logging
        info { "abc" }
        error(IllegalStateException("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            "Test"
        }
        info { "def" }
    }
}