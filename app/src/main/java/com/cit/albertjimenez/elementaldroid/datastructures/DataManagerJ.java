package com.cit.albertjimenez.elementaldroid.datastructures;

import com.cit.albertjimenez.elementaldroid.dao.Element;
import com.cit.albertjimenez.elementaldroid.dao.RegularUser;
import com.cit.albertjimenez.elementaldroid.dao.TeacherUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by Albert Jiménez on 25/9/17 for Programming Mobile Devices.
 */

public class DataManagerJ implements Serializable {
    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

    private static DataManagerJ dataManagerJ = null;

    private HashSet<RegularUser> users;  //Every profile with their own discoveries
    private HashSet<Element> elementHashSet; // All the elements present
    private HashSet<TeacherUser> teacherUsers;

    public HashSet<Element> getElementHashSet() {
        return elementHashSet;
    }

    //Endpoints
    public static final String profilesURL = "profile";
    public static final String elementsURL = "elements";
    public static final String teachersURL = "teachers";

    //FirebaseDatabase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefElements = database.getReference(elementsURL);
    DatabaseReference myRefUsers = database.getReference(profilesURL);
    DatabaseReference myRefTeachers = database.getReference(teachersURL);

    private DataManagerJ() {
        this.users = new HashSet<>();
        this.elementHashSet = new HashSet<>();
        this.teacherUsers = new HashSet<>();
    }

    public static DataManagerJ getInstance() {
        return dataManagerJ == null ? new DataManagerJ() : dataManagerJ;
    }

    public void storeNewElement(Element element) {
        if (elementHashSet.add(element))
            myRefElements.push().setValue(element);

    }

    public boolean isTeacher(TeacherUser teacherUser) {
        return teacherUsers.contains(teacherUser);
    }

    public void storeNewUser(RegularUser user) {
        if (users.add(user))
            myRefUsers.push().setValue(user);

    }


    public void initFB() {
        myRefUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RegularUser user = dataSnapshot.getValue(RegularUser.class);
                if (!users.contains(user)) users.add(user);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Teachers section:
        myRefTeachers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                teacherUsers.add(dataSnapshot.getValue(TeacherUser.class));


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Elements section:

        myRefElements.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                elementHashSet.add(dataSnapshot.getValue(Element.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                elementHashSet.add(dataSnapshot.getValue(Element.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
