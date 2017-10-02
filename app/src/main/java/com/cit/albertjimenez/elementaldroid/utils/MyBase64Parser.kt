package com.cit.albertjimenez.elementaldroid.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream


/**
 * Created by Albert Jiménez on 23/9/17 for Programming Mobile Devices.
 */
fun encodeImage(bmp: Bitmap): String {
    val baos = ByteArrayOutputStream()
    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val imageBytes = baos.toByteArray()
    return Base64.encodeToString(imageBytes, Base64.DEFAULT)
}

fun decodeImage(string: String): Bitmap {
    val byteArray = Base64.decode(string, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

}

