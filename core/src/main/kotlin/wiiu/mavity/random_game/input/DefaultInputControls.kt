package wiiu.mavity.random_game.input

import wiiu.mavity.random_game.util.*

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
        val target = !isFullscreen
        if (target) graphics.setFullscreenMode(graphics.displayMode)
        else graphics.setWindowedMode(1080, 720)
        graphics.setUndecorated(target)
    }
}