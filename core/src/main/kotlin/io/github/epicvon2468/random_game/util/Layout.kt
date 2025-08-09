package io.github.epicvon2468.random_game.util

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.LayoutBase

import java.time.Instant
import java.lang.System.lineSeparator as newline

// Blanks out the entire line for some reason??
// `THE_FORMATTER.format(e.instant)`
class Layout : LayoutBase<ILoggingEvent>() {

	private fun format(instant: Instant): String = instant.toString()
		.substringBeforeLast(".") // We don't need more precision than seconds
		.substringAfter("T") // We don't need more info than hour:minute:second

	override fun doLayout(e: ILoggingEvent): String =
		"${BLUE}[${format(e.instant)}]${RESET} ${GREEN}[${e.threadName}/${e.level}]${RESET} ${CYAN}(${e.loggerName})${RESET} ${e.message}${newline()}"
}