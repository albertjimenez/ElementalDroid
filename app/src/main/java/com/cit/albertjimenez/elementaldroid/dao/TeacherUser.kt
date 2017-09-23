package com.cit.albertjimenez.elementaldroid.dao

/**
 * Created by Albert Jiménez on 23/9/17 for Programming Mobile Devices.
 */
data class TeacherUser(override val name: String, override val email: String,
                       override val discoveredElements: MutableList<Element>) : User {
}