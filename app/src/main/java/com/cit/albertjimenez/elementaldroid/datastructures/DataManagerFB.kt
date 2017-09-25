package com.cit.albertjimenez.elementaldroid.datastructures

import android.util.Log
import com.cit.albertjimenez.elementaldroid.dao.Element
import com.cit.albertjimenez.elementaldroid.dao.RegularUser
import com.cit.albertjimenez.elementaldroid.dao.TeacherUser
import com.cit.albertjimenez.elementaldroid.dao.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


/**
 * Created by Albert Jiménez on 23/9/17 for Programming Mobile Devices.
 */
class DataManagerFB {
    var profileUser: MutableList<User> = mutableListOf<User>() //Every profile with their own discoveries
    val elementsCollection: HashMap<RegularUser, Element> = HashMap() // All the elements present
    val teacherSet: Set<TeacherUser> = HashSet()


    //Endpoints
    public val profilesURL = "profile"
    public val elementsURL = "elements"
    public val teachersURL = "teachers"

    //FirebaseDatabase
    var database = FirebaseDatabase.getInstance()
    var myRefElements = database.getReference(elementsURL)
    val myRefUsers = database.getReference(profilesURL)
    var myRefTeachers = database.getReference(teachersURL)


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
        Log.d("DATASTRUCTURE", profileUser.toString())

        myRefUsers.setValue(profileUser)

    }


    fun retrieveElementsByUser(user: User): MutableList<Element>? {
        return profileUser.find { it == user }?.discoveredElements
    }

    fun initFB() {

        myRefUsers.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
//                var aux: MutableList<User> = mutableListOf()
//                if (p0?.hasChild(profilesURL)!!)
//                    aux = p0.value as MutableList<User>
//
//                println("EVENT Child changed!")
//                println(profileUser.toString())
//                profileUser.addAll(aux)
//                myRefUsers.setValue(profileUser)

            }
        })
    }


}

//data class DSProfileUser(val dsProfileUser: MutableList<User>)

