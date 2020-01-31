package com.usama.runtime.model;

public class Doctors {
    private static Doctors instance = null ;
    private String nationalId, password , realname;

    public Doctors(String nationalId, String password, String realname) {
        this.nationalId = nationalId;
        this.password = password;
        this.realname = realname;
    }

    public Doctors() {
    }

    public static Doctors getInstance() {
        return instance;
    }

    public static void setInstance(Doctors instance) {
        Doctors.instance = instance;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
}
