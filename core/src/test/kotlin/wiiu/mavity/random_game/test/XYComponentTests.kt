package wiiu.mavity.random_game.test

import wiiu.mavity.random_game.util.XYComponent

import kotlin.test.*

class XYComponentTests {

	val component1: XYComponent = XYComponent(-3.7f, 14.5f)
	val component2: XYComponent = XYComponent(9.4f, 2.3f)
	val component3: XYComponent = component1 - 39.4f
	val component4: XYComponent = XYComponent(-Float.MAX_VALUE, -Float.MAX_VALUE)

	@Test
	fun compareTo() {
		assertTrue(component1 < component2)
		assertTrue(component1 > component3)
	}

	// Expected calculated using https://www.calculatorsoup.com/calculators/geometry-plane/distance-two-points.php
	@Test
	fun distanceTo() {
		assertEquals(expected = 17.901117f, actual = component1 distanceTo component2)
		assertEquals(expected = 4.8123191440619e+38f, actual = component4 distanceTo component2)
	}

	@Test
	fun equals() {
		assertNotEquals(component1, component2)
		assertEquals(component1, component1)
	}
}