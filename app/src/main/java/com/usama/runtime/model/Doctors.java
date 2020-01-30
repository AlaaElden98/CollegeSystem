package com.usama.runtime.model;

public class Doctors {
    private static Doctors instance = null ;
    private String name, password;

    public Doctors() {

    }

    public Doctors(String name, String password, String realName) {
        this.name = name;
        this.password = password;
    }
    public static Doctors getInstance() {
        if (instance == null) {
            instance = new Doctors();
        }
        return instance;
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
