package wiiu.mavity.random_game

import com.badlogic.gdx.utils.Disposable

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.*
import io.ktor.utils.io.*

import kotlinx.coroutines.Dispatchers

import wiiu.mavity.random_game.util.*

import java.util.concurrent.atomic.AtomicBoolean

@Suppress("PropertyName") // You can't tell me how to name my own properties!
abstract class SidedConnectionManager<T : SidedConnection> : Disposable {

	private var prevTime: Long = nanoTime

	val selectorManager = SelectorManager(Dispatchers.IO)

	protected val _connections: MutableList<T> = mutableListOf()
	val connections: List<T> get() = this._connections

	val awaitingConnection = AtomicBoolean(true)

	fun tcpSocket(): TcpSocketBuilder = aSocket(this.selectorManager).tcp()

	open fun preLoop() {
		launchConnection()
	}

	open fun postLoop() {
		val nanoTime = nanoTime
		if (nanoTime - prevTime > flushWaitTime) {
			this.connections.forEach { it += "}\n" }
			this.connections.forEach(SidedConnection::postLoop)
			this.connections.forEach { it += "{" }
			prevTime = nanoTime
		}
	}

	abstract fun launchConnection()

	fun setupResponseListeners() {
		println("Connection made! Setting up response listeners!")
		this.connections.forEach {
			asyncIO {
				while (true) {
					it.connection.input.readUTF8LineTo(System.out)
				}
			}
		}
	}

	operator fun plusAssign(data: String) = this.connections.forEach { it += data }

	override fun dispose() {
		this._connections.forEach {
			it.dispose()
			this._connections -= it
		}
		this.selectorManager.close()
	}
}

abstract class SidedConnection(val connection: Connection) : Disposable {

	open fun postLoop() {
		asyncIO {
			if (!connection.output.isClosedForWrite) connection.output.flush()
		}
	}

	open operator fun plusAssign(data: String) {
		asyncIO { connection.output.writeStringUtf8(data) }
	}

	override fun dispose() = this.connection.socket.dispose()
}