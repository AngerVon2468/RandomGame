package wiiu.mavity.random_game

import io.ktor.network.sockets.*
import io.ktor.server.application.Application
import io.ktor.server.routing.*

import ktx.app.KtxApplicationAdapter

import wiiu.mavity.random_game.util.*

object Server : KtxApplicationAdapter {

	lateinit var connectionManager: ServerConnectionManager private set

	override fun create() {
//		embeddedServer(
//			Netty,
//			port = OptionsParser.port,
//			host = OptionsParser.ip,
//			module = Application::module
//		).start(wait = true)
		this.connectionManager = ServerConnectionManager()
		println("STARTING!")
		asyncIO {
			while (true) {
				val line = readlnOrNull()
				if (line != null) this@Server.connectionManager += line
			}
		}
	}

	override fun render() {
		this.connectionManager.preLoop()
		// Perform logic before looping, then flush the data to send & update
		this.connectionManager.postLoop()
	}

	override fun dispose() {
		this.connectionManager.dispose()
	}
}

class ServerConnectionManager : SidedConnectionManager<ServerConnection>() {

	lateinit var serverSocket: ServerSocket private set

	init {
		asyncIO { serverSocket = tcpSocket().bind(OptionsParser.ip, OptionsParser.port) }
	}

	override fun launchConnection() {
		if (!this.awaitingConnection.value) return
		this.awaitingConnection.value = false
		asyncIO {
			println("Waiting for init.")
			while (!this@ServerConnectionManager::serverSocket.isInitialized) ;
			println("Init success!")
			_connections += ServerConnection(serverSocket.accept().connection())
			setupResponseListeners()
			awaitingConnection.value = true
		}
	}
}

class ServerConnection(connection: Connection) : SidedConnection(connection)

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