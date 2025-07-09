package wiiu.mavity.random_game

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.utils.viewport.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.*

import io.ktor.network.sockets.*

import ktx.freetype.generateFont
import ktx.async.KtxAsync
import ktx.log.*
import ktx.app.*

import wiiu.mavity.random_game.ui.screen.StartupScreen
import wiiu.mavity.random_game.input.DefaultInputControls
import wiiu.mavity.random_game.util.*

import kotlinx.coroutines.runBlocking

import kotlin.collections.plusAssign

// TODO: Note to self: The priority order of rendering makes whatever is drawn last show up the highest.
@Suppress("GDXKotlinStaticResource") // Because we're an object, not a class, it thinks we're doing something wrong
object Client : KtxApplicationAdapter {

	lateinit var connectionManager: ClientConnectionManager private set

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

	private lateinit var fpsLogger: FPSLogger

	override fun create() {
		this.connectionManager = ClientConnectionManager()
		this.connectionManager.setupResponseListeners()
		this.fpsLogger = FPSLogger(75)
		KtxAsync.initiate()
		Gdx.app.applicationLogger = Logging
		@Suppress("UnusedExpression") // Calls the `init` statement.
		DefaultInputControls
		info { "Initializing!" }
		this.viewport = FitViewport(1024f, 576f)
		this.batch = SpriteBatch()
		this.fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("font${fs}JetBrainsMono-Light.ttf"))
		this.font = this.fontGenerator.generateFont {
			this.size = 32
			this.mono = true
		}
		this.screen = StartupScreen()
	}

	override fun resize(width: Int, height: Int) = if (this::viewport.isInitialized) this.viewport(width, height, true) else Unit

	override fun render() {
		if (this.paused) return // stub because I want to see things happen when I push button
		this.connectionManager.preLoop()
		this.fpsLogger()
		val deltaTime = deltaTime
		input(deltaTime)
		logic(deltaTime)
		draw(deltaTime)
		this.connectionManager.postLoop()
	}

	private fun input(deltaTime: Float) = Unit

	private fun logic(deltaTime: Float) = Unit

	private fun draw(deltaTime: Float) {
		clearScreen(red = 0f, green = 0f, blue = 0f)
		this.viewport(true)
		this.batch.projectionMatrix = this.viewport.camera.combined
		this.batch.begin()
		this.screen.render(deltaTime)
		this.batch.end()
	}

	override fun pause() {
		info { "Game Paused!" }
		this.paused = true
	}

	override fun resume() {
		info { "Game Resumed!" }
		this.paused = false
	}

	override fun dispose() {
		this.screen.apply {
			this.pause()
			this.hide()
			this.dispose()
		}
		this.batch.dispose()
		this.font.dispose()
		this.fontGenerator.dispose()
		this.connectionManager.dispose()
	}
}

class ClientConnectionManager : SidedConnectionManager<ClientConnection>() {

	val theConnection: ClientConnection get() = this.connections[0]

	// We only need the one connection.
	override fun launchConnection() {
		if (!this.awaitingConnection.value) return
		this.awaitingConnection.value = false
		asyncIO {
			_connections += ClientConnection(tcpSocket().connect(OptionsParser.ip, OptionsParser.port).connection()).also { it += "{" }
		}
	}
}

class ClientConnection(connection: Connection) : SidedConnection(connection)