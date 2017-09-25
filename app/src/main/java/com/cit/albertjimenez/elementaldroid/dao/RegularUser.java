package com.cit.albertjimenez.elementaldroid.dao;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Albert Jiménez on 25/9/17 for Programming Mobile Devices.
 */

public class RegularUser implements Serializable {

    private String name, email;
    private LinkedList<Element> discoveredElements;

    public RegularUser(String name, String email) {
        this.name = name;
        this.email = email;
        discoveredElements = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LinkedList<Element> getDiscoveredElements() {
        return discoveredElements;
    }

    public void setDiscoveredElements(LinkedList<Element> discoveredElements) {
        this.discoveredElements = discoveredElements;
    }

    public RegularUser() {
    }

    @Override
    public String toString() {
        return "RegularUser{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", discoveredElements=" + discoveredElements +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegularUser that = (RegularUser) o;

        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
