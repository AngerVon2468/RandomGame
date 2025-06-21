package wiiu.mavity.random_game.ui.screen

import ktx.app.KtxScreen

import wiiu.mavity.random_game.Main
import wiiu.mavity.random_game.ui.*
import wiiu.mavity.random_game.ui.UIScreen

class LoadingScreen : KtxScreen, UIScreen {

	val textRenderer: CrawlText = CentredCrawlTextRenderer(Main.font, "Loading...")

	override fun render(delta: Float) {
		this.textRenderer.draw(Main.batch)
		if (this.textRenderer.completed) this.textRenderer.reset()
	}
}