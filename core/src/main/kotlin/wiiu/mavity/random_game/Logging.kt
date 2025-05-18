package wiiu.mavity.random_game

import com.badlogic.gdx.ApplicationLogger

import java.time.LocalDateTime
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

object Logging : ApplicationLogger {

    private val FORMAT = DateTimeFormatterBuilder()
        .appendValue(ChronoField.HOUR_OF_DAY, 2)
        .appendLiteral(':')
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
        .optionalStart()
        .appendLiteral(':')
        .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
        .toFormatter()

    private const val INFO = "INFO"
    private const val ERROR = "ERROR"
    private const val DEBUG = "DEBUG"

    private fun build(tag: String?, message: String?, level: String): String =
        "$BLUE[${LocalDateTime.now().format(FORMAT)}]$RESET $GREEN[${Thread.currentThread().name}/$level]$RESET $CYAN($tag)$RESET $message"

    override fun log(tag: String?, message: String?) = println(build(tag, message, INFO))

    override fun log(tag: String?, message: String?, exception: Throwable?) {
        println(build(tag, message, INFO))
        exception?.printStackTrace()
    }

    override fun error(tag: String?, message: String?) {
        val built = build(tag, message, ERROR)
        println(built)
        System.err.println(built)
    }

    override fun error(tag: String?, message: String?, exception: Throwable?) {
        val built = build(tag, message, ERROR)
        println(built)
        System.err.println(built)
        exception?.printStackTrace()
    }

    override fun debug(tag: String?, message: String?) = println(build(tag, message, DEBUG))

    override fun debug(tag: String?, message: String?, exception: Throwable?) {
        println(build(tag, message, DEBUG))
        exception?.printStackTrace()
    }
}

// ANSI Codes
const val RESET: String = "\u001B[0m"
const val GREEN: String = "\u001B[32m"
const val BLUE: String = "\u001B[34m"
const val CYAN: String = "\u001B[36m"