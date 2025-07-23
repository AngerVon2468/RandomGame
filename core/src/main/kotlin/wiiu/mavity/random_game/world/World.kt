package wiiu.mavity.random_game.world

import wiiu.mavity.random_game.level.Level
import wiiu.mavity.random_game.util.Loopable
import wiiu.mavity.random_game.world.entity.Entity
import wiiu.mavity.random_game.world.sync.*

/**
 * Base class for all World implementations.
 *
 * @see LocalWorld
 * @see SynchronisedWorld
 * @see ServerWorld
 * @see ClientWorld
 */
abstract class World(val level: Level) : Loopable {

	val entities: MutableMap<Int, Entity> = mutableMapOf()

	abstract fun connect()
}