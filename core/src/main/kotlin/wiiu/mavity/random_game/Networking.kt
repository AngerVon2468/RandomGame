package wiiu.mavity.random_game

import com.badlogic.gdx.utils.Disposable

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.*
import io.ktor.utils.io.*

import kotlinx.coroutines.Dispatchers

import kotlin.reflect.KClass

import wiiu.mavity.random_game.util.asyncIO

import java.util.concurrent.atomic.AtomicBoolean

val registeredNetworkables: MutableMap<KClass<*>, Networkable<*>> = mutableMapOf()

@Suppress("PropertyName") // You can't tell me how to name my own properties!
abstract class SidedConnectionManager<T : SidedConnection> : Disposable {

	val selectorManager = SelectorManager(Dispatchers.IO)

	protected val _connections: MutableList<T> = mutableListOf()
	val connections: List<T> get() = this._connections

	val awaitingConnection = AtomicBoolean()

	fun tcpSocket(): TcpSocketBuilder = aSocket(this.selectorManager).tcp()

	open fun preLoop() {
		launchConnection()
		this.connections.forEach { it += "{" }
	}

	open fun postLoop() {
		this.connections.forEach { it += "\b}\n" }
		this.connections.forEach(SidedConnection::postLoop)
	}

	abstract fun launchConnection()

	@Suppress("UNCHECKED_CAST")
	inline fun <reified T> getNetworkable(): Networkable<T> {
		val clazz = T::class
		val result = registeredNetworkables.filterKeys { it == clazz }[clazz] as Networkable<T>?
		return result ?: throw IllegalStateException("Couldn't find Networkable for class: $clazz!")
	}

	inline operator fun <reified T> plusAssign(obj: T) {
		val networkable = getNetworkable<T>()
		val send = "(${networkable.deconstruct(obj)})+"
		this.connections.forEach { it += send }
	}

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
		asyncIO { connection.output.flush() }
	}

	operator fun plusAssign(text: String) {
		asyncIO { connection.output.writeStringUtf8(text) }
	}

	override fun dispose() = this.connection.socket.dispose()
}

interface Networkable<T> {

	fun deconstruct(obj: T): String

	fun construct(packet: String): T
}