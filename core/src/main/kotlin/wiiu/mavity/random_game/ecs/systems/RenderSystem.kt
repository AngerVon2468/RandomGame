package wiiu.mavity.random_game.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem

import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.utils.viewport.*

import ktx.app.clearScreen

import wiiu.mavity.random_game.ecs.components.*
import wiiu.mavity.random_game.render.TextRenderer
import wiiu.mavity.random_game.util.*

@All(RenderableComponent::class)
class RenderSystem : IteratingSystem() {

	// Initialised by Reflection... I'm not going to question it...
	lateinit var mRenderable: ComponentMapper<RenderableComponent>

	lateinit var batch: Batch
	lateinit var viewport: Viewport

	override fun initialize() {
		batch = SpriteBatch()
		viewport = FitViewport(1024f, 576f)
	}

	override fun begin() {
		clearScreen(red = 0f, green = 0f, blue = 0f)
		viewport(true)
		batch.projectionMatrix = viewport.camera.combined
		batch.begin()
	}

	override fun process(entityId: Int) {
		val entity = world[entityId]
		when (mRenderable[entity].type) {
			RenderType.Text -> TextRenderer.render(batch, entity)
			RenderType.Spite -> TODO("Shush I'm getting around to it")
			RenderType.Unknown -> TODO()
		}
	}

	override fun end() = batch.end()

	override fun dispose() {
		batch.dispose()
	}
}