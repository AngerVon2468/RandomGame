package wiiu.mavity.random_game.ui.screen

import wiiu.mavity.random_game.ui.CentredTextRenderer
import wiiu.mavity.random_game.Main

import ktx.app.KtxScreen

import wiiu.mavity.random_game.ui.UIScreen

class MenuScreen : KtxScreen, UIScreen {

	val textRenderer = CentredTextRenderer(Main.font, "This is a test.")

	override fun render(delta: Float) {
		this.textRenderer.draw(Main.batch)
	}
}