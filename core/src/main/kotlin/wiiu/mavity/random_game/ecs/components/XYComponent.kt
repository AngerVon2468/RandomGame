package wiiu.mavity.random_game.ecs.components

import com.artemis.Component

import com.badlogic.gdx.math.Vector2

import kotlin.reflect.full.primaryConstructor

/**
 * A component containing x and y coordinates.
 *
 * Implements most of the basic arithmetic operations in the base class for utility purposes.
 */
open class XYComponent(var x: Float = 0.0f, var y: Float = 0.0f) : Component() {

	operator fun component1(): Float = this.x

	operator fun component2(): Float = this.y

	inline operator fun <reified T : XYComponent> plus(other: T): T = this.newInstance(this.x + other.x, this.y + other.y)

	inline operator fun <reified T : XYComponent> plus(other: Vector2): T = this.newInstance(this.x + other.x, this.y + other.y)

	inline operator fun <reified T : XYComponent> plus(other: Number): T = this.newInstance(this.x + other.toFloat(), this.y + other.toFloat())

	inline operator fun <reified T : XYComponent> minus(other: T): T = this.newInstance(this.x - other.x, this.y - other.y)

	inline operator fun <reified T : XYComponent> minus(other: Vector2): T = this.newInstance(this.x - other.x, this.y - other.y)

	inline operator fun <reified T : XYComponent> minus(other: Number): T = this.newInstance(this.x - other.toFloat(), this.y - other.toFloat())

	inline operator fun <reified T : XYComponent> times(other: T): T = this.newInstance(this.x * other.x, this.y * other.y)

	inline operator fun <reified T : XYComponent> times(other: Vector2): T = this.newInstance(this.x * other.x, this.y * other.y)

	inline operator fun <reified T : XYComponent> times(other: Number): T = this.newInstance(this.x * other.toFloat(), this.y * other.toFloat())

	inline operator fun <reified T : XYComponent> div(other: T): T = this.newInstance(this.x / other.x, this.y / other.y)

	inline operator fun <reified T : XYComponent> div(other: Vector2): T = this.newInstance(this.x / other.x, this.y / other.y)

	inline operator fun <reified T : XYComponent> div(other: Number): T = this.newInstance(this.x / other.toFloat(), this.y / other.toFloat())

	operator fun <T : XYComponent> plusAssign(other: T) {
		this.x += other.x
		this.y += other.y
	}

	operator fun plusAssign(other: Vector2) {
		this.x += other.x
		this.y += other.y
	}

	operator fun plusAssign(other: Number) {
		this.x += other.toFloat()
		this.y += other.toFloat()
	}

	operator fun <T : XYComponent> minusAssign(other: T) {
		this.x -= other.x
		this.y -= other.y
	}

	operator fun minusAssign(other: Vector2) {
		this.x -= other.x
		this.y -= other.y
	}

	operator fun minusAssign(other: Number) {
		this.x -= other.toFloat()
		this.y -= other.toFloat()
	}

	operator fun <T : XYComponent> timesAssign(other: T) {
		this.x *= other.x
		this.y *= other.y
	}

	operator fun timesAssign(other: Vector2) {
		this.x *= other.x
		this.y *= other.y
	}

	operator fun timesAssign(other: Number) {
		this.x *= other.toFloat()
		this.y *= other.toFloat()
	}

	operator fun <T : XYComponent> divAssign(other: T) {
		this.x /= other.x
		this.y /= other.y
	}

	operator fun divAssign(other: Vector2) {
		this.x /= other.x
		this.y /= other.y
	}

	operator fun divAssign(other: Number) {
		this.x /= other.toFloat()
		this.y /= other.toFloat()
	}

	inline operator fun <reified T : XYComponent> unaryPlus(): T = this.newInstance(+this.x, +this.y)

	inline operator fun <reified T : XYComponent> unaryMinus(): T = this.newInstance(-this.x, -this.y)

	inline operator fun <reified T : XYComponent> inc(): T = this.newInstance(this.x + 1.0f, this.y + 1.0f)

	inline operator fun <reified T : XYComponent> dec(): T = this.newInstance(this.x - 1.0f, this.y - 1.0f)

	/**
	 * See [Comparable.compareTo].
	 */
	operator fun <T : XYComponent> compareTo(other: T): Int = this.compareTo0(other.total())

	operator fun compareTo(other: Vector2): Int = this.compareTo0(other.x, other.y)

	fun compareTo0(x: Number, y: Number): Int = this.compareTo0(x.toFloat() + y.toFloat())

	fun compareTo0(otherTotal: Float): Int {
		val thisTotal = this.total()
		return if (thisTotal < otherTotal) -1 else if (thisTotal > otherTotal) 1 else 0
	}

	fun total(): Float = this.x + this.y

	inline fun <reified T : XYComponent> copy(): T = this.newInstance()

	fun vec2Copy(): Vector2 = Vector2(this.x, this.y)

	fun toVec2(): Vector2 = this.vec2Copy()

	// Return new instance of current class type.
	inline fun <reified T : XYComponent> newInstance(x: Float = this.x, y: Float = this.y): T = T::class.primaryConstructor!!.call(x, y)

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is XYComponent) return false
		if (other.x != this.x) return false
		if (other.y != this.y) return false
		if (other.hashCode() != this.hashCode()) return false
		return true
	}

	override fun hashCode(): Int {
		var result = x.hashCode()
		result = 31 * result + y.hashCode()
		return result
	}

	override fun toString(): String = "${this::class.simpleName}(x=$x, y=$y)"
}