package wiiu.mavity.random_game.ecs.components

import com.artemis.Component

data class RenderableComponent(val type: RenderType) : Component() {

	constructor() : this(RenderType.Unknown)
}

enum class RenderType {

	Text,
	Spite,
	Unknown
}