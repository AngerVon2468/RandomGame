package wiiu.mavity.random_game.world

import wiiu.mavity.random_game.level.Level

abstract class World(val level: Level) {

	abstract fun connect()
}