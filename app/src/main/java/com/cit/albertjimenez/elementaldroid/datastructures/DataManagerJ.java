package com.cit.albertjimenez.elementaldroid.datastructures;

import android.util.Log;

import com.cit.albertjimenez.elementaldroid.dao.RegularUser;
import com.cit.albertjimenez.elementaldroid.dao.TeacherUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by Albert Jiménez on 25/9/17 for Programming Mobile Devices.
 */

public class DataManagerJ implements Serializable {


    private LinkedList<RegularUser> users;  //Every profile with their own discoveries
//    private HashMap<User, Element> elementHashMap; // All the elements present
private HashSet<TeacherUser> teacherUsers;

    //Endpoints
    public static final String profilesURL = "profile";
    public static final String elementsURL = "elements";
    public static final String teachersURL = "teachers";

    //FirebaseDatabase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefElements = database.getReference(elementsURL);
    DatabaseReference myRefUsers = database.getReference(profilesURL);
    DatabaseReference myRefTeachers = database.getReference(teachersURL);

    public DataManagerJ() {
        this.users = new LinkedList<>();
//        this.elementHashMap = new HashMap<>();
        this.teacherUsers = new HashSet<>();
    }

    public boolean isTeacher(TeacherUser teacherUser) {
        return teacherUsers.contains(teacherUser);
    }

    public void storeNewUser(RegularUser user) {
        if (!users.contains(user)) {
            System.out.println("New user added " + user);
            users.add(user);
            myRefUsers.push().setValue(user);
        }

    }

    public void initFB() {
        myRefUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("Child added");
                System.out.println(dataSnapshot.getValue(RegularUser.class));
                RegularUser user = dataSnapshot.getValue(RegularUser.class);
                if (!users.contains(user)) users.add(user);
                System.out.println(users);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println("Child changed");
                System.out.println(dataSnapshot.getValue());
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
                Log.d("TEACHER", teacherUsers.toString());


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
    }
}
