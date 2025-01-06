package me.cdh

import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage

val chars = charArrayOf(' ', '%', '#', '*', '+', '=', '-', ':', '.', '@')

fun resizedImg(img: BufferedImage, width: Int, height: Int): BufferedImage {
    val tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH)
    val reImg = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    reImg.createGraphics().let {
        it.drawImage(tmp, 0, 0, null)
        it.dispose()
    }
    return reImg
}

fun convertToASCII(img: BufferedImage): String = buildString {
    for (y in 0..<img.height) {
        for (x in 0..<img.width) {
            val rgb = img.getRGB(x, y)
            val pixelColor = Color(rgb)
            val gray = (0.2 * pixelColor.red + 0.69 * pixelColor.green + 0.11 * pixelColor.blue).toInt()
            val asciiChar = chars[gray * (chars.size - 1) / 255]
            append(asciiChar)
        }
        append("\n")
    }
}