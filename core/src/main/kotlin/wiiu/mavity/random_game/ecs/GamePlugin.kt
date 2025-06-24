package wiiu.mavity.random_game.ecs

import com.artemis.*

import wiiu.mavity.random_game.ecs.systems.RenderSystem

object GamePlugin : ArtemisPlugin {

	override fun setup(builder: WorldConfigurationBuilder) {
		builder
			.with(WorldConfigurationBuilder.Priority.HIGHEST, RenderSystem())
	}
}