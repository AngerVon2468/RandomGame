package wiiu.mavity.random_game

import wiiu.mavity.random_game.input.DefaultInputControls
import wiiu.mavity.random_game.util.*

import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.utils.viewport.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.*

import ktx.async.KtxAsync
import ktx.log.*
import ktx.app.*

import wiiu.mavity.random_game.world.read

@Suppress("GDXKotlinStaticResource") // Because we're an object, not a class, it thinks we're doing something wrong
object Main : KtxApplicationAdapter {

    @JvmStatic lateinit var viewport: Viewport private set

    private lateinit var batch: Batch

    @JvmStatic var screen: Screen = emptyScreen()
        set(value) {
            field.hide()
            field = value
            field.show()
            field.resize(screenWidth, screenHeight)
        }

    private lateinit var sprite: Sprite

    private val centreCamera get() =true// screen is UIScreen

    private lateinit var logger: FPSLogger

    override fun create() {
        logger = FPSLogger()
        KtxAsync.initiate()
        Gdx.app.applicationLogger = Logging
        Gdx.input.inputProcessor = DefaultInputControls
        info { "Initializing!" }
        viewport = FitViewport(512f, 288f)
        batch = SpriteBatch()
        sprite = Sprite("test.png")
        read()
    }

    override fun resize(width: Int, height: Int) = if (::viewport.isInitialized) viewport(width, height, centreCamera) else Unit

    override fun render() {
        logger.log()
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

    override fun dispose() {
        screen.apply {
            this.pause()
            this.hide()
            this.dispose()
        }
    }
}