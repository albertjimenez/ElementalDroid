package com.cit.albertjimenez.elementaldroid.dao

/**
 * Created by Albert Jiménez on 23/9/17 for Programming Mobile Devices.
 */
data class Element(val name: String = "", val photoBase64: String = "", val description: String = "") {
    override fun equals(other: Any?): Boolean {
        val aux: Element = other as Element
        return aux.name.toLowerCase() == this.name.toLowerCase()
    }
}