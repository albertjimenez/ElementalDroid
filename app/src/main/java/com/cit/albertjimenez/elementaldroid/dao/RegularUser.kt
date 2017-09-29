package com.cit.albertjimenez.elementaldroid.dao

import java.io.Serializable
import java.util.*

/**
 * Created by Albert Jiménez on 25/9/17 for Programming Mobile Devices.
 */

data class RegularUser(var name: String = "", var email: String = "", var discoveredElements: LinkedList<Element> = LinkedList()) : Serializable {

}
