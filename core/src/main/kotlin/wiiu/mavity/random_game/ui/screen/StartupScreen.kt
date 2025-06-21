package wiiu.mavity.random_game.ui.screen

import wiiu.mavity.random_game.ui.*
import wiiu.mavity.random_game.Main

import ktx.app.KtxScreen
import ktx.app.emptyScreen

import wiiu.mavity.random_game.ui.UIScreen

class StartupScreen : KtxScreen, UIScreen {

	var startTime: Long = 0
	var resetCounter: Boolean = true

	var textRenderer: TextRenderer = CentredCrawlTextRenderer(Main.font, "Mavity Presents:")

	override fun render(delta: Float) {
		this.textRenderer.draw(Main.batch)
		if (this.textRenderer.completed) {
			val nanoTime = System.nanoTime()
			if (resetCounter) {
				startTime = nanoTime
				resetCounter = false
				return
			}
			if (nanoTime - startTime > 2_500_000_000) {
				when (this.textRenderer.text) {
					"<TITLECARD>" -> {
						Main.screen = emptyScreen()
						dispose()
					}
					else -> {
						this.textRenderer = CentredCrawlTextRenderer(Main.font, "<TITLECARD>")
						startTime = 0
						resetCounter = true
					}
				}
			}
		}
	}
}