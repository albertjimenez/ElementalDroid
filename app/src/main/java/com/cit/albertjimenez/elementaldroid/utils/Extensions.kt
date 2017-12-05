package com.cit.albertjimenez.elementaldroid.utils

import android.content.Context
import android.widget.Toast
import java.util.*

/**
 * Created by Albert Jiménez on 2/10/17 for Programming Mobile Devices.
 */
fun String.initialLetter(): String = this[0].toString()

fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) + start

fun Context.toastMakeIt(context: Context, text: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(context, text, duration).show()
