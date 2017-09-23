package com.cit.albertjimenez.elementaldroid.dao

/**
 * Created by Albert Jiménez on 23/9/17 for Programming Mobile Devices.
 */
interface User {
    val name: String
    val email: String
    val discoveredElements: MutableList<Element>

}