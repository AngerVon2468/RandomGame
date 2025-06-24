package wiiu.mavity.random_game.ecs.components

import com.artemis.Component

import com.badlogic.gdx.graphics.g2d.Batch

abstract class Renderable : Component() {

	abstract fun render(batch: Batch, deltaTime: Float)
}