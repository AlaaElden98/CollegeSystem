package com.usama.runtime.model;

public class Doctors {
    private String name,password,realName;

    public Doctors(String name, String password, String realname) {
        this.name = name;
        this.password = password;
        this.realName = realname;
    }
    public Doctors() {
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

    public String getRealname() {
        return realName;
    }

    public void setRealname(String realname) {
        this.realName = realname;
    }

}
