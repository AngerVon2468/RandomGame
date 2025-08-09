package io.github.epicvon2468.random_game.util

import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.int

import com.google.errorprone.annotations.DoNotCall

object Options : CliktCommand("game") {

	val isInitialised: Boolean get() = this::_args.isInitialized && this::runner.isInitialized

	private lateinit var _args: Array<String>
	val args: Array<String> get() = this._args

	private lateinit var runner: (Options) -> Unit

	val distribution: String by this.option("-d", "--dist", "--distribution")
		.default("CLIENT")
		.help("The distribution of the game to launch.")
		.validate { it == "CLIENT" || it == "SERVER" }

	val port: Int by this.option("-p", "--port")
		.int()
		.default(8080)
		.help("The port for the game to launch on/connect to.")
		.validate { it > 0 }

	val ip: String by this.option("-i", "--ip")
		.default("127.0.0.1")
		.help("The ip for the game to launch on/connect to.")

	fun initMain(args: Array<String>, runner: (Options) -> Unit) = this.init(args, runner).main()

	fun init(args: Array<String>, runner: (Options) -> Unit): Options {
		if (this.isInitialised) return this
		this._args = args
		this.runner = runner
		return this
	}

	fun main() = this.main(this.args)

	/**
	 * Not actually deprecated. Just to add error for setups that don't check for [DoNotCall].
	 */
	@Deprecated(message = "NEVER CALL THIS METHOD DIRECTLY UNDER ANY CIRCUMSTANCES!!!!!!", level = DeprecationLevel.HIDDEN)
	@DoNotCall("NEVER CALL THIS METHOD DIRECTLY UNDER ANY CIRCUMSTANCES!!!!!!")
	override fun run() = this.runner(this)
}