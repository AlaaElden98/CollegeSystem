package com.usama.runtime.model;

public class Student {
    String nationalId, password, seatNumber, specialize, total, userName;

    public Student(String nationalId, String password, String seatNumber, String specialize, String total, String userName) {
        this.nationalId = nationalId;
        this.password = password;
        this.seatNumber = seatNumber;
        this.specialize = specialize;
        this.total = total;
        this.userName = userName;
    }

    public Student() {
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

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSpecialize() {
        return specialize;
    }

    public void setSpecialize(String specialize) {
        this.specialize = specialize;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
