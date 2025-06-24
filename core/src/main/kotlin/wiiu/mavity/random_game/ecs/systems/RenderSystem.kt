package wiiu.mavity.random_game.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem

import com.badlogic.gdx.graphics.g2d.*

import wiiu.mavity.random_game.ecs.components.Renderable
import wiiu.mavity.random_game.util.get

@All(Renderable::class)
class RenderSystem : IteratingSystem() {

	// Initialised by Reflection... I'm not going to question it...
	lateinit var componentMapper: ComponentMapper<Renderable>

	lateinit var batch: Batch

	override fun initialize() = this::batch.set(SpriteBatch())

	override fun begin() = this.batch.begin()

	override fun process(entityId: Int) {
		componentMapper[world[entityId]]
	}

	override fun end() = this.batch.end()
}