package wiiu.mavity.random_game.util

interface Loopable {

	fun logic(deltaTime: Float) = Unit

	fun render(deltaTime: Float) = Unit
}