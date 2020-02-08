package com.usama.runtime.model;

public class StudentsAttendance {
    private String national_id , student_name , uniqueNumber;

    public StudentsAttendance(String national_id, String student_name, String uniqueNumber) {
        this.national_id = national_id;
        this.student_name = student_name;
        this.uniqueNumber = uniqueNumber;
    }

    public StudentsAttendance() {
    }

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }
}
