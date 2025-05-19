package wiiu.mavity.random_game

import wiiu.mavity.random_game.ui.UIScreen

import com.badlogic.gdx.utils.viewport.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.*

import ktx.async.KtxAsync
import ktx.log.*
import ktx.app.*

@Suppress("GDXKotlinStaticResource") // Because we're an object, not a class, it thinks we're doing something wrong
object RandomGame : KtxApplicationAdapter {

    @JvmStatic lateinit var viewport: Viewport private set

    @JvmStatic var screen: Screen = emptyScreen()

    private val batch: Batch = CpuSpriteBatch();

    private val centreCamera get() = screen is UIScreen

    override fun create() {
        KtxAsync.initiate()
        Gdx.app.applicationLogger = Logging
        info { "Initializing!" }
        viewport = FitViewport(16f, 9f)
    }

    override fun resize(width: Int, height: Int) {
        if (::viewport.isInitialized) viewport.update(width, height, centreCamera)
    }

    override fun render() {
        input()
        logic()
        draw()
    }

    private fun input() {

    }

    private fun logic() {

    }

    private fun draw() {
        clearScreen(red = 255f, green = 0f, blue = 0f)
        viewport.apply(centreCamera)
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        // draw stuff here
        batch.end()
    }
}