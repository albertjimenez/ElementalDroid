package com.cit.albertjimenez.elementaldroid.datastructures

import com.cit.albertjimenez.elementaldroid.dao.Element
import com.cit.albertjimenez.elementaldroid.dao.RegularUser
import com.cit.albertjimenez.elementaldroid.dao.TeacherUser
import com.cit.albertjimenez.elementaldroid.dao.User
import com.google.firebase.database.FirebaseDatabase
import java.util.*


/**
 * Created by Albert Jiménez on 23/9/17 for Programming Mobile Devices.
 */
class DataManagerFB {
    val profileUser: MutableList<User> = mutableListOf<User>() //Every profile with their own discoveries
    val elementsCollection: HashMap<RegularUser, Element> = HashMap() // All the elements present
    val teacherSet: Set<TeacherUser> = HashSet()

    //Endpoints
    public val profilesURL = "profile"
    public val elementsURL = "elements"
    public val teachersURL = "teachers"

    //FirebaseDatabase
    var database = FirebaseDatabase.getInstance()
    public var myRefElements = database.getReference(elementsURL)
    public var myRefUsers = database.getReference(profilesURL)
    public var myRefTeachers = database.getReference(teachersURL)


    fun storeNewElement(element: Element, user: User) {
        if (!profileUser.contains(user)) {
            user.discoveredElements.add(element)
            profileUser.add(user)
        } else profileUser.find { it == user }?.discoveredElements?.add(element)


        //TODO necessary changing to childs push for concurrency
        myRefElements.setValue(profileUser)

    }

    fun storeNewUser(user: User) {
        if (!profileUser.contains(user))
            profileUser.add(user)
        myRefUsers.setValue(profileUser)

    }

    fun retrieveElementsByUser(user: User): MutableList<Element>? {
        return profileUser.find { it == user }?.discoveredElements
    }


}