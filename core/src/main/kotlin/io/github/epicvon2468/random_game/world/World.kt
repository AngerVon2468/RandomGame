package io.github.epicvon2468.random_game.world

import io.github.epicvon2468.random_game.level.Level
import io.github.epicvon2468.random_game.util.Loopable
import io.github.epicvon2468.random_game.world.entity.Entity
import io.github.epicvon2468.random_game.world.sync.*

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