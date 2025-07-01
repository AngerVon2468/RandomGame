package wiiu.mavity.random_game.lwjgl3

/**
 * Utility to quick-launch on the SERVER distribution.
 */
fun main() {
	main(arrayOf("--dist=SERVER", "--ip=127.0.0.1", "--port=8080"))
}