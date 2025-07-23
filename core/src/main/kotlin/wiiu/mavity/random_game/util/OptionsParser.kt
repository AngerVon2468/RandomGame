package wiiu.mavity.random_game.util

object OptionsParser {

	private lateinit var _args: Array<String>
	val args: Array<String> get() = this._args

	private val _mArgs: MutableMap<String, String> = mutableMapOf()
	val mArgs: Map<String, Any> get() = this._mArgs

	var distribution: String = "CLIENT"; private set
	var port: Int = 8080; private set
	var ip: String = "127.0.0.1"; private set

	fun init(args: Array<String>) {
		if (this::_args.isInitialized) return
		this._args = args
		if (args.isEmpty()) return
		for (arg in args) {
			val argValue = arg.substringAfter("=")
			when (val arg = arg.substringBefore("=").substringAfter("--")) {
				"d", "dist", "distribution" -> {
					require(argValue == "CLIENT" || argValue == "SERVER") {
						"Invalid Distribution: \"$argValue\", expected CLIENT or SERVER."
					}
					this.distribution = argValue
				}
				"p", "port" -> this.port = argValue.toInt()
				"i", "ip" -> this.ip = argValue
				else -> this._mArgs[arg] = argValue
			}
		}
	}

	inline operator fun <reified T> get(key: String): T = when (key) {
		"d", "dist", "distribution" -> this.distribution
		"p", "port" -> this.port
		"i", "ip" -> this.ip
		else -> this.mArgs[key]
	} as T
}