package me.cdh

import oshi.SystemInfo
import picocli.CommandLine
import kotlin.random.Random
import kotlin.random.asJavaRandom
import kotlin.system.exitProcess

@CommandLine.Command(
    name = "ktfetch",
    mixinStandardHelpOptions = true,
    version = ["1.0.0"],
    description = ["ktfetch is a system tool written in Kotlin"],
    footer = ["written by cdh"]
)
object Main : Runnable {
    @CommandLine.Spec
    lateinit var spec: CommandLine.Model.CommandSpec

    @JvmStatic
    fun main(args: Array<String>) {
        CommandLine(Main).execute(*args).also { exitProcess(it) }
    }

    override fun run() {
        when(Random.asJavaRandom().nextInt(1,4)) {
            1 -> printSnapshot(spec, AsciiArt.WINDOW, fetchStats())
            2 -> printSnapshot(spec, AsciiArt.TIE_FIGHTER, fetchStats())
            3 -> printSnapshot(spec, AsciiArt.LAMBDA_SHUTTLE, fetchStats())
        }
    }

    private fun fetchStats(): Map<String, String> = buildMap {
        putAll(Stats.hardwareProperties(SystemInfo().hardware))
        putAll(Stats.osProperties(SystemInfo().operatingSystem))
    }
}