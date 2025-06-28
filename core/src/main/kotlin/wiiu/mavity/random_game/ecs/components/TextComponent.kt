package wiiu.mavity.random_game.ecs.components

import com.artemis.Component

data class TextComponent(var text: String, var crawl: Boolean, var reset: Boolean = false) : Component()