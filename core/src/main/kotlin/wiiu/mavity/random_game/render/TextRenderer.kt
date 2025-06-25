package wiiu.mavity.random_game.render

import com.artemis.*

import com.badlogic.gdx.graphics.g2d.Batch

import ktx.artemis.mapperFor

import wiiu.mavity.random_game.ecs.GameWorld
import wiiu.mavity.random_game.ecs.components.*

object TextRenderer {

	val mText: ComponentMapper<TextComponent> = GameWorld.world.mapperFor()
	val mXY: ComponentMapper<XYComponent> = GameWorld.world.mapperFor()

	fun render(batch: Batch, entity: Entity) {
		val (x, y) = mText[entity]

	}
}