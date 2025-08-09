package io.github.epicvon2468.random_game.ui.screen

import io.github.epicvon2468.random_game.ui.*
import io.github.epicvon2468.random_game.Client

import ktx.app.KtxScreen

class StartupScreen : KtxScreen, UIScreen {

	val textRenderer: CrawlText = CentredCrawlTextRenderer(Client.font, "Mavity Presents:")

	override fun render(delta: Float) {
		this.textRenderer.draw(Client.batch)
		this.textRenderer.loop(
			condition = { this.completed },
			targetWait = 2_500_000_000,
			onComplete = {
				when (this@StartupScreen.textRenderer.text) {
					"<TITLECARD>" -> {
						Client.screen = LoadingScreen()
						this@StartupScreen.dispose()
					}
					else -> this@StartupScreen.textRenderer.newText("<TITLECARD>")
				}
			}
		)
	}
}