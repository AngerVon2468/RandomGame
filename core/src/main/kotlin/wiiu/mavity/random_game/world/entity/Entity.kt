package wiiu.mavity.random_game.world.entity

import wiiu.mavity.random_game.util.Loopable
import wiiu.mavity.random_game.world.World

// If you have IDE warnings about the 'ID' property name,
// change IntelliJ's inspector to check for valid properties
// using this regex instead: '([a-z]|ID)[A-Za-z\d]*'
/**
 * Base class for all Entities in a [World].
 *
 * @see World
 */
abstract class Entity(
	val ID: Int,
	val world: World
) : Loopable {

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