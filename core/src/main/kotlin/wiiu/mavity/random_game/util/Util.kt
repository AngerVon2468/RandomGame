package wiiu.mavity.random_game.util

import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.*
import com.badlogic.gdx.physics.box2d.World

import org.jetbrains.annotations.Range

import java.io.File

import java.time.format.*
import java.time.temporal.ChronoField
import java.util.concurrent.atomic.AtomicBoolean

import kotlinx.coroutines.*

import kotlin.coroutines.*
import kotlin.math.*
import kotlin.time.Duration

fun Sprite(path: String): Sprite = Sprite(Texture(path))

val graphics: Graphics
	get() = Gdx.graphics

val screenWidth: Int
	get() = graphics.width

val screenHeight: Int
	get() = graphics.height

val fps: Int
	get() = graphics.framesPerSecond

val isFullscreen: Boolean
	get() = graphics.isFullscreen

val deltaTime: Float
	get() = graphics.deltaTime

val displayMode: Graphics.DisplayMode
	get() = graphics.displayMode

val fs: String
	get() = File.separator

// 1_000_000_000 == 1.0 seconds in nanos
// 500_000_000 == 0.5 seconds in nanos
// 250_000_000 == 0.25 seconds in nanos
// etc...
val textCrawlSpeed: Long // Time in nanos
	get() = 125_000_000

val flushWaitTime: Long
	get() = 1_000_000_000

val nanoTime: Long
	get() = System.nanoTime()

operator fun Viewport.invoke(centreCamera: Boolean = false) = this.apply(centreCamera)

operator fun Viewport.invoke(width: Int, height: Int, centreCamera: Boolean = false) = this.update(width, height, centreCamera)

operator fun FPSLogger.invoke() = this.log()

fun Controller.startVibration(duration: Duration, strength: @Range(from = 0L, to = 1L) Float) =
	this.startVibration(duration.inWholeMilliseconds.toInt(), strength)

typealias PhysicsWorld = World

fun squareRoot(number: Number): Float = sqrt(number.toFloat())

fun Number.powerOf(number: Number): Float = this.toFloat().pow(number.toFloat())

fun async(
	context: CoroutineContext = EmptyCoroutineContext,
	start: CoroutineStart = CoroutineStart.DEFAULT,
	dispatcher: CoroutineDispatcher = Dispatchers.Default,
	block: suspend CoroutineScope.() -> Unit
): Job = CoroutineScope(dispatcher).launch(context, start, block)

fun asyncIO(
	context: CoroutineContext = EmptyCoroutineContext,
	start: CoroutineStart = CoroutineStart.DEFAULT,
	block: suspend CoroutineScope.() -> Unit
): Job = async(context, start, Dispatchers.IO, block)

// ANSI Colour Codes
const val RESET: String = "\u001B[0m"
const val GREEN: String = "\u001B[32m"
const val BLUE: String = "\u001B[34m"
const val CYAN: String = "\u001B[36m"
const val RED: String = "\u001B[31m"

val THE_FORMATTER: DateTimeFormatter = DateTimeFormatterBuilder()
	.appendValue(ChronoField.HOUR_OF_DAY, 2)
	.appendLiteral(':')
	.appendValue(ChronoField.MINUTE_OF_HOUR, 2)
	.appendLiteral(':')
	.appendValue(ChronoField.SECOND_OF_MINUTE, 2)
	.toFormatter()

var AtomicBoolean.value: Boolean
	get() = this.get()
	set(value) = this.set(value)