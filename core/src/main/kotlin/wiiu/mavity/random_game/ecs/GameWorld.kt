package wiiu.mavity.random_game.ecs

import com.artemis.*

import com.badlogic.gdx.utils.Disposable

// TODO: https://github.com/junkdog/artemis-odb/wiki/Making-a-Game
object GameWorld : Disposable {

	// Initialised by Reflection... I'm not going to question it...
	private lateinit var configuration: WorldConfiguration

	private lateinit var _world: World

	val world: World get() = this._world

	fun init(options: WorldConfigurationBuilder.() -> Unit) {
		this.configuration = WorldConfigurationBuilder().also { it.options() }.build()
		this._world = World(this.configuration)
	}

	inline fun createEntity(options: EntityEdit.() -> Unit): Entity {
		val entity: Entity = this.world.createEntity()
		entity.edit().options()
		return entity
	}

	fun tick(deltaTime: Float) {
		this.world.delta = deltaTime
		this.world.process()
	}

	override fun dispose() {
		this.world.dispose()
	}
}