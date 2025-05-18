package wiiu.mavity.random_game

import wiiu.mavity.random_game.ui.UIScreen

import com.badlogic.gdx.utils.viewport.*
import com.badlogic.gdx.*

import ktx.async.KtxAsync
import ktx.log.*
import ktx.app.*

@Suppress("GDXKotlinStaticResource") // Because we're an object, not a class, it thinks we're doing something wrong
object RandomGame : KtxApplicationAdapter {

    @JvmStatic lateinit var viewport: Viewport private set

    @JvmStatic var screen: Screen = emptyScreen()

    override fun create() {
        KtxAsync.initiate()
        Gdx.app.applicationLogger = Logging
        info { "Initializing!" }
        viewport = FitViewport(16f, 9f)
    }

    override fun resize(width: Int, height: Int) {
        if (::viewport.isInitialized) viewport.update(width, height, screen is UIScreen)
    }

    override fun render() {
        clearScreen(red = 255f, green = 0f, blue = 0f)
        viewport.apply()
    }
}