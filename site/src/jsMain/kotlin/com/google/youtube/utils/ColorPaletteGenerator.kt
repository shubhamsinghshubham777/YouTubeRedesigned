package com.google.youtube.utils

import kotlinx.browser.document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.khronos.webgl.get
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLImageElement
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun generateColorPalette(imagePath: String, paletteSize: Int = 2): List<String> {
    return withContext(Dispatchers.Default) {
        suspendCoroutine { continuation ->
            val image = document.createElement("img") as HTMLImageElement
            image.onload = {
                try {
                    val canvas = document.createElement("canvas") as HTMLCanvasElement
                    val context = canvas.getContext("2d") as CanvasRenderingContext2D

                    // Scale down the image to reduce processing time
                    val scaleFactor = 100.0 / maxOf(image.naturalWidth, image.naturalHeight)
                    val scaledWidth = (image.naturalWidth * scaleFactor).toInt()
                    val scaledHeight = (image.naturalHeight * scaleFactor).toInt()
                    with(canvas) {
                        width = scaledWidth
                        height = scaledHeight
                    }
                    context.drawImage(
                        image = image,
                        dx = 0.0,
                        dy = 0.0,
                        dw = scaledWidth.toDouble(),
                        dh = scaledHeight.toDouble()
                    )
                    val imageData = context.getImageData(
                        sx = 0.0,
                        sy = 0.0,
                        sw = scaledWidth.toDouble(),
                        sh = scaledHeight.toDouble()
                    )
                    val data = imageData.data
                    val pixels = mutableListOf<Triple<Int, Int, Int>>()

                    // Extract RGB values from pixel data
                    for (i in 0 until data.length step 4) {
                        val r = data[i].toInt()
                        val g = data[i + 1].toInt()
                        val b = data[i + 2].toInt()
                        pixels.add(Triple(r, g, b))
                    }

                    if (pixels.isEmpty()) {
                        continuation.resumeWithException(Exception("No pixels found!"))
                    } else {
                        // Initialize the first color box with all pixels
                        val initialBox = createInitialColorBox(pixels)
                        val boxes = mutableListOf(initialBox)

                        // Split boxes until we have three
                        while (boxes.size < paletteSize) {
                            val boxToSplit = boxes.maxByOrNull { box ->
                                (box.maxR - box.minR) + (box.maxG - box.minG) + (box.maxB - box.minB)
                            } ?: break
                            boxes.remove(boxToSplit)
                            val (box1, box2) = boxToSplit.split()
                            boxes.add(box1)
                            boxes.add(box2)
                        }

                        // Generate the palette colors
                        val palette = boxes.take(paletteSize).map { box ->
                            if (box.pixels.isEmpty()) Triple(
                                0,
                                0,
                                0
                            ) else calculateAverageColor(box.pixels)
                        }

                        // Convert RGB to HEX strings
                        val hexColors = palette.map { (r, g, b) ->
                            "#${r.toHex()}${g.toHex()}${b.toHex()}"
                        }

                        continuation.resume(hexColors)
                    }
                } catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }

            image.onerror = { _, _, _, _, _ ->
                continuation.resumeWithException(Exception("Failed to load image"))
            }

            image.src = imagePath
        }
    }
}

private fun createInitialColorBox(pixels: List<Triple<Int, Int, Int>>): ColorBox {
    val rValues = pixels.map { it.first }
    val gValues = pixels.map { it.second }
    val bValues = pixels.map { it.third }
    return ColorBox(
        pixels = pixels,
        minR = rValues.minOrNull() ?: 0,
        maxR = rValues.maxOrNull() ?: 0,
        minG = gValues.minOrNull() ?: 0,
        maxG = gValues.maxOrNull() ?: 0,
        minB = bValues.minOrNull() ?: 0,
        maxB = bValues.maxOrNull() ?: 0
    )
}

private fun calculateAverageColor(pixels: List<Triple<Int, Int, Int>>): Triple<Int, Int, Int> {
    val avgR = pixels.map { it.first }.average().toInt()
    val avgG = pixels.map { it.second }.average().toInt()
    val avgB = pixels.map { it.third }.average().toInt()
    return Triple(avgR, avgG, avgB)
}

private fun Int.toHex(): String = this.toString(16).padStart(2, '0')

data class ColorBox(
    val pixels: List<Triple<Int, Int, Int>>,
    val minR: Int,
    val maxR: Int,
    val minG: Int,
    val maxG: Int,
    val minB: Int,
    val maxB: Int,
) {
    fun split(): Pair<ColorBox, ColorBox> {
        val rangeR = maxR - minR
        val rangeG = maxG - minG
        val rangeB = maxB - minB

        val splitChannel = when (maxOf(rangeR, rangeG, rangeB)) {
            rangeR -> 'R'
            rangeG -> 'G'
            else -> 'B'
        }

        val sortedPixels = when (splitChannel) {
            'R' -> pixels.sortedBy { it.first }
            'G' -> pixels.sortedBy { it.second }
            else -> pixels.sortedBy { it.third }
        }

        val medianIndex = sortedPixels.size / 2
        val firstHalf = sortedPixels.subList(0, medianIndex)
        val secondHalf = sortedPixels.subList(medianIndex, sortedPixels.size)

        return Pair(createBoxFromPixels(firstHalf), createBoxFromPixels(secondHalf))
    }

    private fun createBoxFromPixels(pixels: List<Triple<Int, Int, Int>>): ColorBox {
        if (pixels.isEmpty()) return ColorBox(emptyList(), 0, 0, 0, 0, 0, 0)
        val r = pixels.map { it.first }
        val g = pixels.map { it.second }
        val b = pixels.map { it.third }
        return ColorBox(
            pixels = pixels,
            minR = r.minOrNull() ?: 0,
            maxR = r.maxOrNull() ?: 0,
            minG = g.minOrNull() ?: 0,
            maxG = g.maxOrNull() ?: 0,
            minB = b.minOrNull() ?: 0,
            maxB = b.maxOrNull() ?: 0
        )
    }
}
