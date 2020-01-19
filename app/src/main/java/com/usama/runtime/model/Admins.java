package com.usama.runtime.model;

public class Admins {
    String name , password;

    public Admins(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Admins() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
