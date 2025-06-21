package wiiu.mavity.random_game.ui

import com.badlogic.gdx.graphics.g2d.*

import wiiu.mavity.random_game.Main
import wiiu.mavity.random_game.util.textCrawlSpeed

typealias PositionModifier = TextRenderer.(Float) -> Float

// TODO: Do periods go before the start of a parentheses, or inside at the end, or outside at the end?
/**
 * Simple abstraction to render text on-screen. This class is mutable & overridable for utility purposes, OVERRIDE AT OWN RISK!
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
	open var x: Float,
	open var y: Float,
	font: BitmapFont,
	text: String,
	open var xModifier: PositionModifier = { it },
	open var yModifier: PositionModifier = { it }
) {

	open var font: BitmapFont = font
		set(value) {
			field = value
			this.layout.setText(field, this.text)
		}

	open var text: String = text
		set(value) {
			field = value
			this.layout.setText(this.font, field)
		}

	open val completed: Boolean = true

	open val layout = GlyphLayout(this.font, this.text)

	open fun draw(batch: Batch) {
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

interface CrawlText {

	var startTime: Long

	var textCopy: String

	var index: Int

	val completed: Boolean
		get() = this.index == this.textCopy.length
}

open class CrawlTextRenderer(
	x: Float,
	y: Float,
	font: BitmapFont,
	text: String,
	xModifier: PositionModifier = { it },
	yModifier: PositionModifier = { it }
) : TextRenderer(x, y, font, text, xModifier, yModifier), CrawlText {

	override var startTime: Long = System.nanoTime()

	override var textCopy: String = text

	override var index: Int = 0

	override val completed: Boolean
		get() = super<CrawlText>.completed

	init {
		if (this.textCopy.isNotEmpty()) this.text = ""
		else error { "Cannot use empty string!" }
	}

	override fun draw(batch: Batch) {
		val nanoTime = System.nanoTime()
		if (nanoTime - startTime > textCrawlSpeed) {
			if (index < this.textCopy.length) this.text += this.textCopy[this.index++]
			startTime = nanoTime
		}
		super.draw(batch)
	}
}

open class CentredCrawlTextRenderer(
	font: BitmapFont,
	text: String,
	newXModifier: PositionModifier = { it },
	newYModifier: PositionModifier = { it }
) : CentredTextRenderer(font, text, newXModifier, newYModifier), CrawlText {

	override var startTime: Long = System.nanoTime()

	override var textCopy: String = text

	override var index: Int = 0

	override val completed: Boolean
		get() = super<CrawlText>.completed

	init {
		if (this.textCopy.isNotEmpty()) this.text = ""
		else error { "Cannot use empty string!" }
	}

	override fun draw(batch: Batch) {
		val nanoTime = System.nanoTime()
		if (nanoTime - startTime > textCrawlSpeed) {
			if (index < this.textCopy.length) this.text += this.textCopy[this.index++]
			startTime = nanoTime
		}
		super.draw(batch)
	}
}