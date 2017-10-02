package com.cit.albertjimenez.elementaldroid.utils

import java.util.*

/**
 * Created by Albert Jiménez on 2/10/17 for Programming Mobile Devices.
 */
fun String.initialLetter(): String {
    return this[0].toString()
}

fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) + start