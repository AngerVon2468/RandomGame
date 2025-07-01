package wiiu.mavity.random_game

import com.badlogic.gdx.utils.Disposable

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Connection
import io.ktor.network.sockets.TcpSocketBuilder
import io.ktor.network.sockets.aSocket

import kotlinx.coroutines.Dispatchers

import wiiu.mavity.random_game.util.asyncIO
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("PropertyName") // You can't tell me how to name my own properties!
abstract class SidedConnectionManager<T : SidedConnection> : Disposable {

	val selectorManager = SelectorManager(Dispatchers.IO)

	protected val _connections: MutableList<T> = mutableListOf()
	val connections: List<T> get() = this._connections

	val awaitingConnection = AtomicBoolean()

	fun tcpSocket(): TcpSocketBuilder = aSocket(this.selectorManager).tcp()

	open fun loop() {
		launchConnection()
		this.connections.forEach(SidedConnection::loop)
	}

	abstract fun launchConnection()

	override fun dispose() {
		this._connections.forEach {
			it.dispose()
			this._connections -= it
		}
		this.selectorManager.close()
	}
}

abstract class SidedConnection(val connection: Connection) : Disposable {

	open fun loop() {
		asyncIO {
			connection.output.flush()
		}
	}

	override fun dispose() = this.connection.socket.dispose()
}