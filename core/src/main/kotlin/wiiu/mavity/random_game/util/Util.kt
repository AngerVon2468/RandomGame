package wiiu.mavity.random_game.util

import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.*

fun Sprite(path: String): Sprite = Sprite(Texture(path))

val graphics: Graphics
    get() = Gdx.graphics

val screenWidth: Int
    get() = graphics.width

val screenHeight: Int
    get() = graphics.height

val fps: Int
    get() = graphics.framesPerSecond

val isFullscreen: Boolean
    get() = graphics.isFullscreen

val deltaTime: Float
    get() = graphics.deltaTime

operator fun Viewport.invoke(centreCamera: Boolean = false) = this.apply(centreCamera)

operator fun Viewport.invoke(width: Int, height: Int, centreCamera: Boolean = false) = this.update(width, height, centreCamera)