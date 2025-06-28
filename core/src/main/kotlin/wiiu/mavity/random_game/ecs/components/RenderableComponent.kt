package wiiu.mavity.random_game.ecs.components

import com.artemis.Component

data class RenderableComponent(val type: RenderType) : Component() {

	@Suppress("unused") // Used via Reflection
	constructor() : this(RenderType.Unknown)
}

enum class RenderType {

	Text,
	Sprite,
	Unknown
}