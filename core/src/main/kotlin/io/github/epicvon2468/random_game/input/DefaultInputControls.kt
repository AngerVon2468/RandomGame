package io.github.epicvon2468.random_game.input

import io.github.epicvon2468.random_game.util.*
import io.github.epicvon2468.random_game.Client

import com.badlogic.gdx.*
import com.badlogic.gdx.controllers.*

import ktx.app.KtxInputAdapter
import ktx.log.error

import kotlin.time.Duration.Companion.seconds

// Scan for ←→↓←→↓
object DefaultInputControls : KtxInputAdapter, ControllerAdapter() {

	init {
		Gdx.input.inputProcessor = DefaultInputControls
		Controllers.addListener(this)
	}

	private var _theController: Controller? = null
	val controllerConnected: Boolean get() = this._theController != null
	val theController: Controller get() = this._theController!!

	override fun connected(controller: Controller) {
		if (this.controllerConnected) {
			error { "New controller paired when main already present! Multiplayer not implemented yet!" }
			return
		}
		this._theController = controller
	}

	override fun disconnected(controller: Controller) {
		if (!this.controllerConnected) {
			error { "Excuse me, WHAT!?" }
			throw IllegalStateException("Received disconnect event when no controller was bound.")
		}
		if (this.theController.uniqueId == controller.uniqueId) this._theController = null
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