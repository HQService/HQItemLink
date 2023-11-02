package kr.cosine.itemlink.extension

import java.text.DecimalFormat

private const val DEFAULT_FORMAT = "#,##0.###"
private val decimalFormat = DecimalFormat(DEFAULT_FORMAT)

internal fun Double.applyComma(): String = decimalFormat.format(this)

internal fun Int.applyComma(): String = decimalFormat.format(this)

internal fun Long.applyComma(): String = decimalFormat.format(this)