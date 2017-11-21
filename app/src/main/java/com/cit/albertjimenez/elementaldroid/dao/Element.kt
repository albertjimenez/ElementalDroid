package com.cit.albertjimenez.elementaldroid.dao

import java.io.Serializable

/**
 * Created by Albert Jiménez on 23/9/17 for Programming Mobile Devices.
 */
data class Element(var title: String = "", var original: String = "", var extract: String = "") : Comparable<Element>, Serializable {
    override fun equals(other: Any?): Boolean {
        val aux: Element = other as Element
        return aux.title.toLowerCase() == this.title.toLowerCase()
    }

    override fun compareTo(other: Element): Int = this.title.compareTo(other.title)

    fun isEmpty(): Boolean = title == "" && original == "" && extract == ""

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + original.hashCode()
        result = 31 * result + extract.hashCode()
        return result
    }

}