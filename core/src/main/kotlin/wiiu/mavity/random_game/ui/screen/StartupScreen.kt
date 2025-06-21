package wiiu.mavity.random_game.ui.screen

import wiiu.mavity.random_game.ui.*
import wiiu.mavity.random_game.Main

import ktx.app.KtxScreen

import wiiu.mavity.random_game.ui.UIScreen

class StartupScreen : KtxScreen, UIScreen {

	val textRenderer: CrawlText = CentredCrawlTextRenderer(Main.font, "Mavity Presents:")

	override fun render(delta: Float) {
		this.textRenderer.draw(Main.batch)
		this.textRenderer.loop(
			condition = { this.completed },
			targetWait = 2_500_000_000,
			onComplete = {
				when (this@StartupScreen.textRenderer.text) {
					"<TITLECARD>" -> {
						Main.screen = LoadingScreen()
						this@StartupScreen.dispose()
					}
					else -> this@StartupScreen.textRenderer.newText("<TITLECARD>")
				}
			}
		)
	}
}