package wiiu.mavity.random_game.test

import com.google.gson.GsonBuilder

import wiiu.mavity.random_game.level.*

import kotlin.test.*

class LevelTests {

	@Test
	fun levelTypeAdapterTest() {
		val gson = GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(Level::class.java, LevelTypeAdapter)
			.registerTypeAdapter(Chunk::class.java, ChunkTypeAdapter)
			.create()
		try {
			val ret = gson.fromJson("""
				{
					"1:1": {
						"1:1": "builtin:dirt!wiiu.mavity.random_game.impls.world.entity.TestCharacterEntity",
						"1:5": "builtin:stone"
					},
					"1:2": {},
					"1:9": {}
				}
			""".trimIndent(), Level::class.java)
		} catch (e: Exception) {
			val error = AssertionError("Failed to read Level from JSON!")
			error.addSuppressed(e)
			throw error
		}
		// TODO: Add deserialisation tests
	}
}