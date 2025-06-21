package wiiu.mavity.random_game.ui.screen

import wiiu.mavity.random_game.ui.*
import wiiu.mavity.random_game.Main

import ktx.app.KtxScreen

import wiiu.mavity.random_game.ui.UIScreen

class StartupScreen : KtxScreen, UIScreen {

	val textRenderer: TextRenderer = CentredCrawlTextRenderer(Main.font, "Mavity Presents:")

	override fun render(delta: Float) {
		this.textRenderer.draw(Main.batch)
	}
}