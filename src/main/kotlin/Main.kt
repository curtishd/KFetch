package me.cdh

import com.sun.jna.Platform
import oshi.SystemInfo
import picocli.CommandLine
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
        when {
            Platform.isWindows() || Platform.isWindowsCE() -> printSnapshot(spec, AsciiArt.LAMBDA_SHUTTLE, fetchStats())
            Platform.isMac() -> printSnapshot(spec, AsciiArt.TIE_FIGHTER, fetchStats())
            Platform.isLinux() -> printSnapshot(spec, AsciiArt.CDH, fetchStats())
        }
    }

    private fun fetchStats(): Map<String, String> = buildMap {
        putAll(Stats.hardwareProperties(SystemInfo().hardware))
        putAll(Stats.osProperties(SystemInfo().operatingSystem))
    }
}