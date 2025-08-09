package wiiu.mavity.random_game.level

import com.google.gson.*
import com.google.gson.stream.*

import wiiu.mavity.random_game.util.Two
import wiiu.mavity.random_game.world.entity.Entity

typealias Position = Two<Int, Int>
val Position.x: Int get() = this.first
val Position.y: Int get() = this.second
fun Two.Companion.fromStringP(str: String): Position = fromString0(str) { it.toInt() }

// If you have IDE warnings about the 'ID' property name,
// change IntelliJ's inspector to check for valid properties
// using this regex instead: '([a-z]|ID)[A-Za-z\d]*'

typealias TileID = Two<String, String>
val TileID.declarer: String get() = this.first
val TileID.ID: String get() = this.first
fun Two.Companion.fromStringTID(str: String): TileID = fromString0(str) { it }

typealias EntityClass = Class<out Entity>

data class TileInfo(
	val ID: TileID,
	val entityClass: EntityClass?
) : Two<TileID, EntityClass?>(ID, entityClass) {

	override fun toString(): String = "$ID${if (entityClass != null) "!${entityClass.name}" else ""}"

	companion object {

		@Suppress("UNCHECKED_CAST")
		fun fromStringTI(str: String): TileInfo {
			val index: Int = str.indexOf('!')
			val str0 = if (index == -1) str else str.subSequence(0, index).toString()
			val ID = TileID.fromStringTID(str0)
			val entityClass: EntityClass?
			= if (index != -1) Class.forName(str.subSequence(index + 1, str.length).toString()) as EntityClass?
			else null
			return TileInfo(ID, entityClass)
		}
	}
}

data class Level(val chunks: Map<Position, Chunk>)

data class Chunk(val position: Position, val tiles: Map<Position, TileInfo>)

object LevelTypeAdapter : TypeAdapter<Level>() {

	override fun write(out: JsonWriter, value: Level) {
		out.beginObject()

		for (entry in value.chunks.values) ChunkTypeAdapter.write(out, entry)

		out.endObject()
	}

	override fun read(`in`: JsonReader): Level {
		val map: MutableMap<Position, Chunk> = mutableMapOf()

		`in`.beginObject()

		while (`in`.hasNext()) {
			val chunk = ChunkTypeAdapter.read(`in`)
			map += chunk.position to chunk
		}

		`in`.endObject()

		return Level(map)
	}
}

object ChunkTypeAdapter : TypeAdapter<Chunk>() {

	override fun write(out: JsonWriter, value: Chunk) {
		out.name(value.position.toString())

		out.beginObject()

		for (entry in value.tiles) out.name(entry.key.toString()).value(entry.value.toString())

		out.endObject()
	}

	override fun read(`in`: JsonReader): Chunk {
		val position = Position.fromStringP(`in`.nextName())
		val tiles: MutableMap<Position, TileInfo> = mutableMapOf()

		`in`.beginObject()

		while (`in`.hasNext())
			tiles += Position.fromStringP(`in`.nextName()) to TileInfo.fromStringTI(`in`.nextString())

		`in`.endObject()

		return Chunk(position, tiles)
	}
}

val LEVEL_GSON: Gson = GsonBuilder()
	.setPrettyPrinting()
	.registerTypeAdapter(Level::class.java, LevelTypeAdapter)
	.registerTypeAdapter(Chunk::class.java, ChunkTypeAdapter)
	.create()