package wiiu.mavity.random_game.input

import wiiu.mavity.random_game.util.isFullscreen

import com.badlogic.gdx.*

import ktx.app.KtxInputAdapter

object DefaultInputControls : KtxInputAdapter {

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.F11 -> fullscreenToggle()
        }
        return true
    }

    private fun fullscreenToggle() {
        val fullscreen = isFullscreen
        if (!fullscreen) Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
        else Gdx.graphics.setWindowedMode(0, 0)
        Gdx.graphics.setUndecorated(fullscreen)
    }
}