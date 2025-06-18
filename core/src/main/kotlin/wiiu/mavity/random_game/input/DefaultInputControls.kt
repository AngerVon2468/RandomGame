package wiiu.mavity.random_game.input

import wiiu.mavity.random_game.util.*

import com.badlogic.gdx.*
import com.badlogic.gdx.controllers.*

import ktx.app.KtxInputAdapter

import wiiu.mavity.random_game.Main

import kotlin.time.Duration.Companion.seconds

object DefaultInputControls : KtxInputAdapter, ControllerAdapter() {

	init {
		Gdx.input.inputProcessor = DefaultInputControls
		Controllers.addListener(this)
	}

	override fun buttonDown(controller: Controller, buttonIndex: Int): Boolean {
		if (buttonIndex == controller.mapping.buttonStart) {
			if (Main.paused) Main.resume() else Main.pause()
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