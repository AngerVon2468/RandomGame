package wiiu.mavity.random_game

import wiiu.mavity.random_game.input.DefaultInputControls
import wiiu.mavity.random_game.util.*

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.utils.viewport.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.Texture

import ktx.freetype.generateFont
import ktx.async.KtxAsync
import ktx.log.*
import ktx.app.*

import wiiu.mavity.random_game.ui.screen.MenuScreen
import wiiu.mavity.random_game.world.read

@Suppress("GDXKotlinStaticResource") // Because we're an object, not a class, it thinks we're doing something wrong
object Main : KtxApplicationAdapter {

	@JvmStatic var paused: Boolean = false; private set

	@JvmStatic lateinit var viewport: Viewport private set

	lateinit var batch: Batch private set

	@JvmStatic var screen: Screen = emptyScreen()
		set(value) {
			field.hide()
			field = value
			field.show()
			field.resize(screenWidth, screenHeight)
		}

	private lateinit var fontGenerator: FreeTypeFontGenerator

	@JvmStatic lateinit var font: BitmapFont

	private lateinit var sprite: Sprite

	private val centreCamera get() = true

	private lateinit var fpsLogger: FPSLogger

	override fun create() {
		fpsLogger = FPSLogger()
		KtxAsync.initiate()
		Gdx.app.applicationLogger = Logging
		DefaultInputControls
		info { "Initializing!" }
		viewport = FitViewport(256f, 144f)
		batch = SpriteBatch()
		sprite = Sprite("test.png")
		fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("font${fs}JetBrainsMono-Light.ttf"))
		font = fontGenerator.generateFont {
			this.mono = true
			this.hinting = FreeTypeFontGenerator.Hinting.None
		}
		read()
		screen = MenuScreen()
	}

	override fun resize(width: Int, height: Int) = if (::viewport.isInitialized) viewport(width, height, centreCamera) else Unit

	override fun render() {
		if (paused) return // stub because I want to see things happen when I push button
		fpsLogger()
		val deltaTime = deltaTime
		input(deltaTime)
		logic(deltaTime)
		draw(deltaTime)
	}

	private fun input(deltaTime: Float) = Unit

	private fun logic(deltaTime: Float) = Unit

	private fun draw(deltaTime: Float) {
		clearScreen(red = 0f, green = 0f, blue = 0f)
		viewport(centreCamera)
		batch.projectionMatrix = viewport.camera.combined
		batch.begin()
		sprite.draw(batch)
		screen.render(deltaTime)
		batch.end()
	}

	override fun pause() {
		info { "Game Paused!" }
		paused = true
	}

	override fun resume() {
		info { "Game Resumed!" }
		paused = false
	}

	override fun dispose() {
		screen.apply {
			this.pause()
			this.hide()
			this.dispose()
		}
		batch.dispose()
		font.dispose()
		fontGenerator.dispose()
		sprite.texture.dispose()
	}
}