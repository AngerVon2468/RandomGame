package wiiu.mavity.random_game.ui

import com.badlogic.gdx.graphics.g2d.*

import wiiu.mavity.random_game.Main
import wiiu.mavity.random_game.util.*

typealias PositionModifier = TextRenderer.(Float) -> Float

interface TextRendererAccess {

	var x: Float

	var y: Float

	var font: BitmapFont

	var text: String

	var xModifier: PositionModifier

	var yModifier: PositionModifier

	val completed: Boolean

	val layout: GlyphLayout

	fun draw(batch: Batch)
}

interface LoopableTextRendererAccess : TextRendererAccess {

	var loopConditionStart: Long

	var loopConditionMet: Boolean

	fun loop(
		condition: LoopableTextRendererAccess.() -> Boolean = { true },
		targetWait: Long,
		onComplete: LoopableTextRendererAccess.() -> Unit
	) {
		if (!this.condition()) return
		if (!this.loopConditionMet) {
			this.loopConditionStart = nanoTime
			this.loopConditionMet = true
			return
		}
		val nanoTime = nanoTime
		if (nanoTime - this.loopConditionStart > targetWait) {
			this.onComplete()
			this.loopConditionStart = nanoTime
			this.loopConditionMet = false
		}
	}
}

// TODO: Do periods go before the start of a parentheses, or inside at the end, or outside at the end?
/**
 * Simple abstraction to render text on-screen. This class is mutable & overridable for utility purposes, overriding and/or modifying variables while in use may have unexpected results.
 *
 * @param x The horizontal position of the text.
 * @param y The vertical position of the text.
 * @param font The font to use for the text.
 * @param text The text to render.
 * @param xModifier A modifier to apply to the location of the text on the horizontal axis.
 * @param yModifier A modifier to apply to the location of the text on the vertical axis.
 *
 * @property x The horizontal position of the text.
 * @property y The vertical position of the text.
 * @property font The font to use for the text.
 * @property text The text to render.
 * @property completed If the text has been fully rendered onto the screen (mostly relevant for [CrawlTextRenderer] & [CentredCrawlTextRenderer])
 * @property xModifier A modifier to apply to the location of the text on the horizontal axis.
 * @property yModifier A modifier to apply to the location of the text on the vertical axis.
 *
 * @author EpicVon2468
 */
open class TextRenderer(
	override var x: Float,
	override var y: Float,
	font: BitmapFont,
	text: String,
	override var xModifier: PositionModifier = { it },
	override var yModifier: PositionModifier = { it }
) : LoopableTextRendererAccess {

	override var loopConditionStart: Long = nanoTime

	override var loopConditionMet: Boolean = false

	override var font: BitmapFont = font
		set(value) {
			field = value
			this.layout.setText(field, this.text)
		}

	override var text: String = text
		set(value) {
			field = value
			this.layout.setText(this.font, field)
		}

	override val completed: Boolean = true

	override val layout = GlyphLayout(this.font, this.text)

	override fun draw(batch: Batch) {
		this.font.draw(batch, this.text, this.xModifier(this.x - this.layout.width), this.yModifier(this.y + this.layout.height))
	}
}

open class CentredTextRenderer(
	font: BitmapFont,
	text: String,
	open var newXModifier: PositionModifier = { it },
	open var newYModifier: PositionModifier = { it }
) : TextRenderer(
	0.0f,
	0.0f,
	font,
	text
) {

	override var x: Float = 0.0f
		get() = Main.viewport.worldWidth

	override var y: Float = 0.0f
		get() = Main.viewport.worldHeight

	override var xModifier: PositionModifier = { this.newXModifier(it / 2.0f) }

	override var yModifier: PositionModifier = { this.newYModifier(it / 2.0f) }
}

interface CrawlText : LoopableTextRendererAccess {

	var startTime: Long

	var textCopy: String

	var index: Int

	override val completed: Boolean
		get() = this.index == this.textCopy.length

	fun init() = if (this.textCopy.isNotEmpty()) this.text = "" else error { "Cannot use empty string!" }

	fun crawl() {
		val nanoTime = nanoTime
		if (nanoTime - this.startTime > textCrawlSpeed) {
			if (index < this.textCopy.length) this.text += this.textCopy[this.index++]
			this.startTime = nanoTime
		}
	}

	fun autoReset(targetWait: Long) = this.loop(
		condition = { this@CrawlText.completed },
		targetWait = targetWait,
		onComplete = { this@CrawlText.reset() }
	)

	fun reset() {
		this.text = ""
		this.index = 0
		this.startTime = nanoTime
	}

	fun newText(text: String) {
		this.reset()
		this.textCopy = text
		this.init()
	}
}

open class CrawlTextRenderer(
	x: Float,
	y: Float,
	font: BitmapFont,
	text: String,
	xModifier: PositionModifier = { it },
	yModifier: PositionModifier = { it }
) : TextRenderer(x, y, font, text, xModifier, yModifier), CrawlText {

	override var startTime: Long = nanoTime

	override var textCopy: String = text

	override var index: Int = 0

	override val completed: Boolean
		get() = super<CrawlText>.completed

	init {
		this.init()
	}

	override fun draw(batch: Batch) {
		this.crawl()
		super.draw(batch)
	}
}

open class CentredCrawlTextRenderer(
	font: BitmapFont,
	text: String,
	newXModifier: PositionModifier = { it },
	newYModifier: PositionModifier = { it }
) : CentredTextRenderer(font, text, newXModifier, newYModifier), CrawlText {

	override var startTime: Long = nanoTime

	override var textCopy: String = text

	override var index: Int = 0

	override val completed: Boolean
		get() = super<CrawlText>.completed

	init {
		this.init()
	}

	override fun draw(batch: Batch) {
		this.crawl()
		super.draw(batch)
	}
}