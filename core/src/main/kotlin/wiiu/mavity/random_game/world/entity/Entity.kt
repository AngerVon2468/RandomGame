package wiiu.mavity.random_game.world.entity

import wiiu.mavity.random_game.world.World

@Suppress("PropertyName") // "ID" is the correct spelling.
abstract class Entity(
	val ID: Int,
	val world: World
) {

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is Entity) return false
		if (other.ID != this.ID) return false
		if (other.world != this.world) return false
		if (other.hashCode() != this.hashCode()) return false
		return true
	}

	final override fun hashCode(): Int {
		var result = this.ID
		result = 31 * result + this.world.hashCode()
		return result
	}
}