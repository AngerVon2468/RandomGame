package wiiu.mavity.random_game.world

import com.google.gson.*
import com.google.gson.stream.*

data class Position(val first: Int, val second: Int) {

    override fun toString(): String = "$first:$second"

    companion object {

        fun fromString(str: String): Position {
            val rawPos = str.split(":")
            return Position(rawPos[0].toInt(), rawPos[1].toInt())
        }
    }
}

data class World(val chunks: Map<Position, Chunk>)

// tiles: Position : Tile ID
data class Chunk(val position: Position, val tiles: Map<Position, String>)

object WorldTypeAdapter : TypeAdapter<World>() {

    override fun write(out: JsonWriter, value: World) {
        out.beginObject()

        for (entry in value.chunks.values) ChunkTypeAdapter.write(out, entry)

        out.endObject()
    }

    override fun read(`in`: JsonReader): World {
        val map: MutableMap<Position, Chunk> = mutableMapOf()

        `in`.beginObject()

        while (`in`.hasNext()) {
            val chunk = ChunkTypeAdapter.read(`in`)
            map += chunk.position to chunk
        }

        `in`.endObject()

        return World(map)
    }
}

object ChunkTypeAdapter : TypeAdapter<Chunk>() {

    override fun write(out: JsonWriter, value: Chunk) {
        out.name(value.position.toString())

        out.beginObject()

        for (entry in value.tiles) out.name(entry.key.toString()).value(entry.value)

        out.endObject()
    }

    override fun read(`in`: JsonReader): Chunk {
        val position = Position.fromString(`in`.nextName())
        val tiles: MutableMap<Position, String> = mutableMapOf()

        `in`.beginObject()

        while (`in`.hasNext()) tiles += Position.fromString(`in`.nextName()) to `in`.nextString()

        `in`.endObject()

        return Chunk(position, tiles)
    }
}

fun read() {
    val gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(World::class.java, WorldTypeAdapter)
        .registerTypeAdapter(Chunk::class.java, ChunkTypeAdapter)
}