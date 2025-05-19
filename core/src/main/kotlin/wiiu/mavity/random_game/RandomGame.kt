package wiiu.mavity.random_game

import wiiu.mavity.random_game.util.Sprite

import com.badlogic.gdx.utils.viewport.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.*

import ktx.async.KtxAsync
import ktx.log.*
import ktx.app.*

@Suppress("GDXKotlinStaticResource") // Because we're an object, not a class, it thinks we're doing something wrong
object RandomGame : KtxApplicationAdapter {

    @JvmStatic lateinit var viewport: Viewport private set

    private lateinit var batch: Batch

    @JvmStatic var screen: Screen = emptyScreen()

    private lateinit var sprite: Sprite

    private val centreCamera get() =true// screen is UIScreen

    override fun create() {
        KtxAsync.initiate()
        Gdx.app.applicationLogger = Logging
        info { "Initializing!" }
        viewport = FitViewport(512f, 288f)
        batch = SpriteBatch()
        sprite = Sprite("test.png")
    }

    override fun resize(width: Int, height: Int) = if (::viewport.isInitialized) viewport.update(width, height, centreCamera) else Unit

    override fun render() {
        val deltaTime = Gdx.graphics.deltaTime
        input(deltaTime)
        logic(deltaTime)
        draw(deltaTime)
    }

    private fun input(deltaTime: Float) {

    }

    private fun logic(deltaTime: Float) {

    }

    private fun draw(deltaTime: Float) {
        clearScreen(red = 0f, green = 0f, blue = 0f)
        viewport.apply(centreCamera)
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        // draw stuff here
        screen.render(deltaTime)
        sprite.draw(batch)
        batch.end()
    }
}