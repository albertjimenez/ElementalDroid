package com.cit.albertjimenez.elementaldroid.dao

import java.io.Serializable

/**
 * Created by Albert Jiménez on 23/9/17 for Programming Mobile Devices.
 */
data class RegularUser(override val name: String, override val email: String,
                       override val discoveredElements: MutableList<Element>) : Serializable, User {
}