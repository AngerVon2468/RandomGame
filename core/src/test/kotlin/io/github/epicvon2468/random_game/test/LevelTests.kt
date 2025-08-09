package io.github.epicvon2468.random_game.test

import io.github.epicvon2468.random_game.level.*

import kotlin.test.*

class LevelTests {

	@Test
	fun levelTypeAdapterTest() {
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

		val `in` = try { LEVEL_GSON.fromJson(json, Level::class.java) }
		catch (e: Exception) { fail("Failed to read Level from JSON!", e) }

		val `out` = try { LEVEL_GSON.toJson(`in`) }
		catch (e: Exception) { fail("Failed to write Level to JSON!", e) }

		assertEquals(json, `out`)
	}
}