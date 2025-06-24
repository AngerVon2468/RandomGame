package wiiu.mavity.random_game.util

import com.artemis.*

import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.*

import org.jetbrains.annotations.Range

import java.io.File

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

val nanoTime: Long
	get() = System.nanoTime()

operator fun Viewport.invoke(centreCamera: Boolean = false) = this.apply(centreCamera)

operator fun Viewport.invoke(width: Int, height: Int, centreCamera: Boolean = false) = this.update(width, height, centreCamera)

operator fun FPSLogger.invoke() = this.log()

fun Controller.startVibration(duration: Duration, strength: @Range(from = 0L, to = 1L) Float) =
	this.startVibration(duration.inWholeMilliseconds.toInt(), strength)

typealias PhysicsWorld = com.badlogic.gdx.physics.box2d.World

operator fun World.get(entityId: Int): Entity = this.getEntity(entityId)