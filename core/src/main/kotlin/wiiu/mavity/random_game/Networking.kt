package wiiu.mavity.random_game

import com.badlogic.gdx.utils.Disposable

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.*
import io.ktor.utils.io.*

import kotlinx.coroutines.*
// Already have another import that also has the same name, have to specify to avoid import ambiguity.
import kotlinx.coroutines.CancellationException

import ktx.log.*

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
		if (nanoTime - this.prevFlushTime > flushWaitTime) {
			this.connections.forEach(SidedConnection::postLoop)
			this.prevFlushTime = nanoTime
		}
	}

	abstract fun launchConnection()

	/**
	 * Starts a new coroutine to listen for input from the connection.
	 */
	fun setupResponseListeners() {
		println("Connection made! Setting up response listeners!")
		asyncIO {
			val dataHandler = DataHandler()
			outer@while (true) {
				for (it in this@SidedConnectionManager.connections) {
					try {
						suspend fun close(reason: String) {
							this@SidedConnectionManager._connections -= it
							info { "Dropping connection: ${it.connection} ($reason)" }
							it.postLoop0(close = true)
						}
						if (it.connection.input.isClosedForRead) close("READ CLOSED!")
						val line = it.connection.input.readUTF8Line()
						dataHandler.read(line)
						val instructions = dataHandler.instructions
						if (Instruction.END in instructions) close("END REACHED")
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
			_connections -= it
			it += "(END;-1)}"
			it.postLoop0(close = true)
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
	open suspend fun postLoop0(close: Boolean = false) {
		if (this.connection.output.isClosedForWrite) return
		this += "}\n"
		this.connection.output.writeStringUtf8(this.cache)
		if (close) {
			this.cache = ""
			this.connection.output.flushAndClose()
		} else {
			this.cache = "{"
			this.connection.output.flush()
		}
	}

	/**
	 * Adds the [data] string to the [cache] to be sent during [postLoop0]. Null values will be converted to an empty string.
	 */
	open operator fun plusAssign(data: String?) {
		this.cache += (data ?: "")
	}

	override fun dispose() = this.connection.socket.dispose()
}

data class Targets(val rawTargets: Array<Int>) {

	constructor(targets: String) : this(
		if ("|" in targets) targets.split("|").map(String::toInt).toTypedArray() else arrayOf(targets.toInt())
	)

	constructor(target: Int) : this(arrayOf(target))

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is Targets) return false
		if (!other.rawTargets.contentEquals(this.rawTargets)) return false
		if (other.hashCode() != this.hashCode()) return false
		return true
	}

	override fun hashCode(): Int = this.rawTargets.contentHashCode()
}

typealias Instructions = MutableMap<Instruction, Pair<Targets, String>>

class DataHandler {

	private val _instructions: Instructions = mutableMapOf()
	val instructions: Instructions
		get() = this._instructions.toMutableMap() // Give a copy of the map at read time.

	// Data formatted like so:
	// {(EXAMPLE_INSTRUCTION;TARGET_ID(s);EXTRA_DATA)+(EXAMPLE_INSTRUCTION_TWO;TARGET_ID(s))}
	// {(MOVE;0;9:4)+(DAMAGE;7|0|3;40)+(DESPAWN;4)}
	// Use -1 to target ALL
	fun read(line: String?) {
		if (this._instructions.isNotEmpty()) this._instructions.clear()
		if (line == null || line == "{}") return
		debug { "Received line: \"$line\"" }
		val content = line.substringAfter("{").substringBeforeLast("}").split("+")
		for (entry in content) {
			if (entry.isEmpty()) continue
			val data = entry.substringAfter("(").substringBeforeLast(")").split(";")
			require(data.isNotEmpty()) { "Cannot parse empty instructions" }
			val instruction: Instruction = try {
				Instruction.valueOf(data[0])
			} catch (e: Exception) {
				error { "Failed to parse instruction at element 0 in: \"$data\"" }
				throw e
			}
			val targetIDs: Targets = try {
				Targets(data[1])
			} catch (e: Exception) {
				error { "Failed to parse target(s) at element 1 in: \"$data\"" }
				throw e
			}
			val extra: String = if (data.size == 3) { data[2] } else ""
			this._instructions += instruction to (targetIDs to extra)
		}
	}
}

enum class Instruction {
	// Unknown Instruction
	UNKNOWN,

	// Game World Instructions
	MOVE,
	DAMAGE,
	DESPAWN,

	// Connection Instructions
	END
}