package wiiu.mavity.random_game.ui.screen

import ktx.app.KtxScreen

import wiiu.mavity.random_game.Client
import wiiu.mavity.random_game.ui.*
import wiiu.mavity.random_game.util.Sprite

class LoadingScreen : KtxScreen, UIScreen {

	val textRenderer: CrawlText = CentredCrawlTextRenderer(Client.font, "Loading...")
	val sprite = Sprite("test.png").also {
		it.x = Client.viewport.worldWidth / 2.0f - (it.width / 2)
		it.y = Client.viewport.worldHeight / 2.0f - (it.height / 2)
	}

	override fun render(delta: Float) {
		this.sprite.draw(Client.batch)
		this.textRenderer.draw(Client.batch)
		this.textRenderer.autoReset(500_000_000)
	}

	override fun dispose() {
		this.sprite.texture.dispose()
	}
}