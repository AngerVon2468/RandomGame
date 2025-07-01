package wiiu.mavity.random_game

import com.badlogic.gdx.utils.Disposable

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.*
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.*

import ktx.app.KtxApplicationAdapter

import wiiu.mavity.random_game.util.*

import java.util.concurrent.atomic.*

import kotlinx.coroutines.Dispatchers

import kotlin.concurrent.thread

object Server : KtxApplicationAdapter {

	lateinit var connectionManager: ServerConnectionManager

	override fun create() {
//		thread {
//			embeddedServer(
//				Netty,
//				port = OptionsParser.port,
//				host = OptionsParser.ip,
//				module = Application::module
//			).start(wait = true).also {
//				Runtime.getRuntime().addShutdownHook(thread(name = "KtorServerShutdownThread", start = false) {
//					it.stop(0L, 0L)
//				})
//			}
//		}
		this.connectionManager = ServerConnectionManager()
		println("STARTING!")
	}

	override fun render() {
		// Perform logic before looping, then flush the data to send & update
		this.connectionManager.loop()
		println("Hi")
	}

	override fun dispose() {
		this.connectionManager.dispose()
	}
}

class ServerConnectionManager : Disposable {

	val selectorManager = SelectorManager(Dispatchers.IO)
	lateinit var serverSocket: ServerSocket private set

	private val _connections: MutableList<ServerConnection> = mutableListOf()
	val connections: List<ServerConnection> get() = this._connections

	val awaitingConnection = AtomicBoolean()

	init {
		asyncIO {
			serverSocket = aSocket(selectorManager).tcp().bind(OptionsParser.ip, OptionsParser.port)
		}
	}

	fun loop() {
		launch()
		connections.forEach(ServerConnection::loop)
	}

	fun launch() {
		if (!this.awaitingConnection.get()) return
		this.awaitingConnection.set(false)
		asyncIO {
			_connections += ServerConnection(serverSocket.accept().connection())
			awaitingConnection.set(true)
		}
	}

	override fun dispose() {
		this._connections.forEach {
			it.dispose()
			this._connections -= it
		}
		this.serverSocket.dispose()
		this.selectorManager.close()
	}
}

class ServerConnection(val connection: Connection) : Disposable {

	fun loop() {
		asyncIO { connection.output.flush() }
	}

	override fun dispose() = this.connection.socket.close()
}

fun Application.module() {
	routing {

		get("/motd") {
			TODO("MOTD")
		}

		get("/worldData") {
			TODO("Send static world file")
		}

	}
}