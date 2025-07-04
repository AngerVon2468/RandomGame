package wiiu.mavity.random_game.input

import wiiu.mavity.random_game.util.*

import com.badlogic.gdx.*
import com.badlogic.gdx.controllers.*

import ktx.app.KtxInputAdapter

import wiiu.mavity.random_game.Client

import kotlin.time.Duration.Companion.seconds

object DefaultInputControls : KtxInputAdapter, ControllerAdapter() {

	init {
		Gdx.input.inputProcessor = DefaultInputControls
		Controllers.addListener(this)
	}

	override fun buttonDown(controller: Controller, buttonIndex: Int): Boolean {
		val mapping = controller.mapping
		when (buttonIndex) {
			mapping.buttonStart -> if (Client.paused) Client.resume() else Client.pause()
		}
		controller.startVibration(0.5.seconds, 0.2f)
		return true
	}

	override fun keyDown(keycode: Int): Boolean {
		when (keycode) {
			Input.Keys.F11 -> fullscreenToggle()
		}
		return true
	}

	private fun fullscreenToggle() {
		val target = !isFullscreen
		if (target) graphics.setFullscreenMode(displayMode)
		else graphics.setWindowedMode(1080, 720)
		graphics.setUndecorated(target)
	}
}