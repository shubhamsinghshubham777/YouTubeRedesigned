package com.google.youtube.utils

import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue

fun PaddingValues(
    left: CSSLengthOrPercentageNumericValue,
    top: CSSLengthOrPercentageNumericValue,
    right: CSSLengthOrPercentageNumericValue,
    bottom: CSSLengthOrPercentageNumericValue,
): PaddingValues = PaddingValuesImpl(left = left, right = right, top = top, bottom = bottom)

fun PaddingValues(all: CSSLengthOrPercentageNumericValue): PaddingValues =
    PaddingValuesImpl(left = all, right = all, top = all, bottom = all)

fun PaddingValues(
    leftRight: CSSLengthOrPercentageNumericValue,
    topBottom: CSSLengthOrPercentageNumericValue,
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
}

internal data class PaddingValuesImpl(
    override val left: CSSLengthOrPercentageNumericValue,
    override val top: CSSLengthOrPercentageNumericValue,
    override val right: CSSLengthOrPercentageNumericValue,
    override val bottom: CSSLengthOrPercentageNumericValue,
) : PaddingValues
