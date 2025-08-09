package io.github.epicvon2468.random_game

import com.badlogic.gdx.ApplicationLogger

import io.github.epicvon2468.random_game.util.*

import java.time.LocalDateTime

object Logging : ApplicationLogger {

	private const val INFO = "INFO"
	private const val ERROR = "ERROR"
	private const val DEBUG = "DEBUG"

	private fun build(tag: String?, message: String?, level: String, extra: String = "", extra2: String = ""): String =
		"$BLUE[${THE_FORMATTER.format(LocalDateTime.now())}]$RESET $GREEN[${Thread.currentThread().name}/$level]$RESET $CYAN($tag)$RESET $extra$message$extra2"

	override fun log(tag: String?, message: String?) = println(build(tag, message, INFO))

	override fun log(tag: String?, message: String?, exception: Throwable?) {
		println(build(tag, message, INFO))
		exception?.printStackTrace(System.out)
		exception?.printStackTrace(System.err)
	}

	override fun error(tag: String?, message: String?) {
		val built = build(tag, message, ERROR, RED, RESET)
		println(built)
		System.err.println(built)
	}

	override fun error(tag: String?, message: String?, exception: Throwable?) {
		val built = build(tag, message, ERROR, RED, RESET)
		println(built)
		exception?.printStackTrace(System.out)
		System.err.println(built)
		exception?.printStackTrace(System.err)
	}

	override fun debug(tag: String?, message: String?) = println(build(tag, message, DEBUG))

	override fun debug(tag: String?, message: String?, exception: Throwable?) {
		println(build(tag, message, DEBUG))
		exception?.printStackTrace(System.out)
		exception?.printStackTrace(System.err)
	}
}