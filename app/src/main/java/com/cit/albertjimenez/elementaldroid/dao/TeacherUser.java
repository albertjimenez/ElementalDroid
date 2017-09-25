package com.cit.albertjimenez.elementaldroid.dao;

import java.io.Serializable;

/**
 * Created by Albert Jiménez on 25/9/17 for Programming Mobile Devices.
 */

public class TeacherUser extends RegularUser implements Serializable {
    private String name, email;

    public TeacherUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public TeacherUser() {
    }

    @Override
    public String toString() {
        return "TeacherUser{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeacherUser that = (TeacherUser) o;

        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
