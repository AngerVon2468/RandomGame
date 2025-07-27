package wiiu.mavity.random_game.level

import com.google.gson.*
import com.google.gson.stream.*

import wiiu.mavity.random_game.world.entity.Entity

// TODO: Switch Chunk#tiles to Map<Position, TileInfo>. Move Two to Utils?

open class Two<out A, out B>(open val first: A, open val second: B) {

	constructor(pair: Pair<A, B>) : this(pair.first, pair.second)

	override fun toString(): String = "$first:$second"

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is Two<*, *>) return false
		if (other.first != this.first) return false
		if (other.second != this.second) return false
		if (other.hashCode() != this.hashCode()) return false
		return true
	}

	override fun hashCode(): Int {
		var result = this.first?.hashCode() ?: 0
		result = 31 * result + (this.second?.hashCode() ?: 0)
		return result
	}

	companion object {

		inline fun <reified A, reified B> fromString0(
			str: String,
			convert1: (String) -> A,
			convert2: (String) -> B
		): Two<A, B> {
			val rawPos = str.split(":")
			return Two(convert1(rawPos[0]), convert2(rawPos[1]))
		}

		inline fun <reified T> fromString0(
			str: String,
			convert: (String) -> T
		): Two<T, T> = fromString0(str, convert, convert)
	}
}

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
fun Two.Companion.fromStringTI(str: String): TileID = fromString0(str) { it }

typealias TileInfo = Two<TileID, Class<out Entity>?>
val TileInfo.ID: TileID get() = this.first
val TileInfo.entityClass: Class<out Entity>? get() = this.second

data class Level(val chunks: Map<Position, Chunk>)

// tiles: Position : Tile ID
data class Chunk(val position: Position, val tiles: Map<Position, TileID>)

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
		val tiles: MutableMap<Position, TileID> = mutableMapOf()

		`in`.beginObject()

		while (`in`.hasNext()) tiles += Position.fromStringP(`in`.nextName()) to TileID.fromStringTI(`in`.nextString())

		`in`.endObject()

		return Chunk(position, tiles)
	}
}

fun read() {
	val gson = GsonBuilder()
		.setPrettyPrinting()
		.registerTypeAdapter(Level::class.java, LevelTypeAdapter)
		.registerTypeAdapter(Chunk::class.java, ChunkTypeAdapter)
}