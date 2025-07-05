package wiiu.mavity.random_game

import com.badlogic.gdx.utils.Disposable

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.*
import io.ktor.utils.io.*

import kotlinx.coroutines.*
// Already have another import that also has the same name, have to specify to avoid import ambiguity.
import kotlinx.coroutines.CancellationException

import ktx.log.error

import wiiu.mavity.random_game.util.*

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("PropertyName") // You can't tell me how to name my own properties!
abstract class SidedConnectionManager<T : SidedConnection> : Disposable {

	/**
	 * The timestamp (in nanoseconds) at which the output connection was last instructed to flush.
	 */
	private var prevFlushTime: Long = nanoTime

	val selectorManager = SelectorManager(Dispatchers.IO)

	protected val _connections: MutableList<T> = CopyOnWriteArrayList()
	val connections: List<T> get() = this._connections

	/**
	 * @return If the manager is waiting for a new connection to be made.
	 */
	val awaitingConnection = AtomicBoolean(true)

	fun tcpSocket(): TcpSocketBuilder = aSocket(this.selectorManager).tcp()

	open fun preLoop() = launchConnection()

	open fun postLoop() {
		val nanoTime = nanoTime
		if (nanoTime - prevFlushTime > flushWaitTime) {
			this += "}\n"
			this.connections.forEach(SidedConnection::postLoop)
			this += "{"
			prevFlushTime = nanoTime
		}
	}

	abstract fun launchConnection()

	/**
	 * Starts a new coroutine to listen for input from the connection.
	 */
	fun setupResponseListeners() {
		println("Connection made! Setting up response listeners!")
		asyncIO {
			outer@while (true) {
				for (it in this@SidedConnectionManager.connections) {
					try {
						suspend fun close(reason: String) {
							this@SidedConnectionManager._connections -= it
							println("Dropping connection: ${it.connection} ($reason)")
							it.postLoop0()
							it.dispose()
						}
						if (it.connection.input.isClosedForRead) close("READ CLOSED!")
						val line = it.connection.input.readUTF8Line()
						if (line?.contains("(END)}") == true) close("END REACHED")
					} catch(e: Throwable) {
						if (e is CancellationException) break@outer
						error { "Exception caught during response listener loop! Abandoning! $e" }
						throw e
					}
				}
			}
		}
	}

	operator fun plusAssign(data: String?) = this.connections.forEach { it += data }

	override fun dispose() = runBlocking {
		_connections.forEach {
			it.postLoop0()
			it.dispose()
			_connections -= it
		}
		if (this@SidedConnectionManager is ServerConnectionManager) serverSocket.dispose()
		selectorManager.close()
	}
}

abstract class SidedConnection(val connection: Connection) : Disposable {

	/**
	 * Cached content to push to the connection. Content MUST be sent via a cached string to avoid race conditions. Must be UTF-8.
	 */
	private var cache: String = ""

	/**
	 * Launches [postLoop0] in a new coroutine on [Dispatchers.IO].
	 */
	open fun postLoop() {
		asyncIO { this@SidedConnection.postLoop0() }
	}

	/**
	 * Writes the [cache] to the [connection]'s [ByteWriteChannel], resets the cache string and then flushes.
	 */
	open suspend fun postLoop0() {
		if (this.connection.output.isClosedForWrite) return
		this.connection.output.writeStringUtf8(this.cache)
		this.cache = ""
		this.connection.output.flush()
	}

	/**
	 * Adds the [data] string to the [cache] to be sent during [postLoop0]. Null values will be converted to an empty string.
	 */
	open operator fun plusAssign(data: String?) {
		this.cache += (data ?: "")
	}

	override fun dispose() = this.connection.socket.dispose()
}