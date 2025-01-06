package me.cdh

import picocli.CommandLine
import java.util.regex.Pattern

val ANSI_PATTERN: Pattern = Pattern.compile("\\u001B[^m]*m")
val LAST_ANSI_PATTERN: Pattern = Pattern.compile("${ANSI_PATTERN.pattern()}(?![\\s\\S]*${ANSI_PATTERN.pattern()})")
const val COLOR_REST = "\u001B[0m"

fun printSnapshot(spec: CommandLine.Model.CommandSpec, asciiEnum: AsciiArt, properties: Map<String, String>) {
    val image = asciiEnum.artPiece.split("\n").toTypedArray()
    var line = 0
    while ((image.size - properties.keys.size) / 2 > line) {
        spec.commandLine().out.println(image[line])
        line++
    }
    for (property in properties.keys) {
        val imageLine =
            if (line >= image.size)
                String.format("%${maxLength(image)} ")
            else
                String.format("%-${visibleLength(maxLength(image), image[line])}s", image[line])
        spec.commandLine().out.println(
            "$imageLine$COLOR_REST${
                String.format(
                    "%${maxLength(properties.keys.toTypedArray())}s: %-1s",
                    property,
                    properties[property] + lastColorCode(image[line])
                )
            }"
        )
        line++
    }
    while (line < image.size) spec.commandLine().out.println(image[line]).also { line++ }
}

private fun lastColorCode(ansiStr: String): String =
    LAST_ANSI_PATTERN.matcher(ansiStr).let { if (it.find()) it.group() else COLOR_REST }


fun maxLength(strings: Array<String>): Int =
    strings.maxOfOrNull { str -> ANSI_PATTERN.matcher(str).replaceAll("").length } ?: 5

private fun visibleLength(maxLengthSansAnsiSequence: Int, str: String): Int {
    var seqSum = maxLengthSansAnsiSequence
    return ANSI_PATTERN.matcher(str).let {
        while (it.find()) seqSum += it.group().length
        seqSum
    }
}