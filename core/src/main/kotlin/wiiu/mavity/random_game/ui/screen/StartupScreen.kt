package wiiu.mavity.random_game.ui.screen

import wiiu.mavity.random_game.ui.*
import wiiu.mavity.random_game.Main

import ktx.app.KtxScreen

import wiiu.mavity.random_game.ui.UIScreen
import wiiu.mavity.random_game.util.nanoTime

class StartupScreen : KtxScreen, UIScreen {

	var startTime: Long = 0
	var resetCounter: Boolean = true

	val textRenderer: CrawlText = CentredCrawlTextRenderer(Main.font, "Mavity Presents:")

	override fun render(delta: Float) {
		this.textRenderer.draw(Main.batch)
		if (!this.textRenderer.completed)  return
		val nanoTime = nanoTime
		if (resetCounter) {
			startTime = nanoTime
			resetCounter = false
			return
		}
		if (nanoTime - startTime <= 2_500_000_000) return
		when (this.textRenderer.text) {
			"<TITLECARD>" -> {
				Main.screen = LoadingScreen()
				dispose()
			}
			else -> {
				this.textRenderer.newText("<TITLECARD>")
				startTime = 0
				resetCounter = true
			}
		}
	}
}