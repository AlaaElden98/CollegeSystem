package com.usama.runtime.model;

public class Subject {

    private String Doctor_Name , Subject_Name;

    public Subject(String doctor_Name, String subject_Name) {
        Doctor_Name = doctor_Name;
        Subject_Name = subject_Name;
    }

    public Subject() {
    }

    public String getDoctor_Name() {
        return Doctor_Name;
    }

    public void setDoctor_Name(String doctor_Name) {
        Doctor_Name = doctor_Name;
    }

    public String getSubject_Name() {
        return Subject_Name;
    }

    public void setSubject_Name(String subject_Name) {
        Subject_Name = subject_Name;
    }
}

