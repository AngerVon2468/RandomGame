package wiiu.mavity.random_game.util

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.Gdx

fun Sprite(path: String): Sprite = Sprite(Texture(path))

val screenWidth: Int
    get() = Gdx.graphics.width

val screenHeight: Int
    get() = Gdx.graphics.height

val fps: Int
    get() = Gdx.graphics.framesPerSecond

val isFullscreen: Boolean
    get() = Gdx.graphics.isFullscreen