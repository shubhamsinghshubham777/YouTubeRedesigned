package com.google.youtube.utils

import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.times

fun PaddingValues(
    left: CSSLengthOrPercentageNumericValue = 0.px,
    top: CSSLengthOrPercentageNumericValue = 0.px,
    right: CSSLengthOrPercentageNumericValue = 0.px,
    bottom: CSSLengthOrPercentageNumericValue = 0.px,
): PaddingValues = PaddingValuesImpl(left = left, right = right, top = top, bottom = bottom)

fun PaddingValues(all: CSSLengthOrPercentageNumericValue = 0.px): PaddingValues =
    PaddingValuesImpl(left = all, right = all, top = all, bottom = all)

fun PaddingValues(
    leftRight: CSSLengthOrPercentageNumericValue = 0.px,
    topBottom: CSSLengthOrPercentageNumericValue = 0.px,
): PaddingValues = PaddingValuesImpl(
    left = leftRight,
    right = leftRight,
    top = topBottom,
    bottom = topBottom,
)

interface PaddingValues {
    val left: CSSLengthOrPercentageNumericValue
    val top: CSSLengthOrPercentageNumericValue
    val right: CSSLengthOrPercentageNumericValue
    val bottom: CSSLengthOrPercentageNumericValue
    operator fun times(value: Number): PaddingValues
}

internal data class PaddingValuesImpl(
    override val left: CSSLengthOrPercentageNumericValue,
    override val top: CSSLengthOrPercentageNumericValue,
    override val right: CSSLengthOrPercentageNumericValue,
    override val bottom: CSSLengthOrPercentageNumericValue,
) : PaddingValues {
    override fun times(value: Number): PaddingValues {
        return PaddingValues(
            left = left * value,
            top = top * value,
            right = right * value,
            bottom = bottom * value,
        )
    }
}
