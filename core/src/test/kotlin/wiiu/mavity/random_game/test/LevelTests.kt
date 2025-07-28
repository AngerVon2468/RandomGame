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

		val json = """
		{
		  "1:1": {
		    "1:1": "builtin:dirt!wiiu.mavity.random_game.impls.world.entity.TestCharacterEntity",
		    "7:5": "builtin:stone"
		  },
		  "1:2": {},
		  "1:9": {
		    "3:8": "custom:custom"
		  }
		}
		""".trimIndent()

		val `in` = try { gson.fromJson(json, Level::class.java) }
		catch (e: Exception) { fail("Failed to read Level from JSON!", e) }

		val `out` = try { gson.toJson(`in`) }
		catch (e: Exception) { fail("Failed to write Level to JSON!", e) }

		assertEquals(json, `out`)
	}
}