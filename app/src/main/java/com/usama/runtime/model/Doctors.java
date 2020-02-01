package com.usama.runtime.model;

public class Doctors {
    private static Doctors instance = null ;
    private String nationalID, password , realName;

    public Doctors(String nationalID, String password, String realName) {
        this.nationalID = nationalID;
        this.password = password;
        this.realName = realName;
    }

    public Doctors() {

    }

    public static Doctors getInstance() {
        return instance;
    }

    public static void setInstance(Doctors instance) {
        Doctors.instance = instance;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
