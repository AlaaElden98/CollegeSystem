package com.usama.runtime.model;

public class Department {
    private String departmentCapacity, departmentMinSpecial, departmentMinTotal, departmentName , departmentSpecialSubject;

    public Department() {
    }

    public Department(String departmentCapacity, String departmentMinSpecial, String departmentMinTotal, String departmentName, String departmentSpecialSubject) {
        this.departmentCapacity = departmentCapacity;
        this.departmentMinSpecial = departmentMinSpecial;
        this.departmentMinTotal = departmentMinTotal;
        this.departmentName = departmentName;
        this.departmentSpecialSubject = departmentSpecialSubject;
    }

    public String getDepartmentCapacity() {
        return departmentCapacity;
    }

    public void setDepartmentCapacity(String departmentCapacity) {
        this.departmentCapacity = departmentCapacity;
    }

    public String getDepartmentMinSpecial() {
        return departmentMinSpecial;
    }

    public void setDepartmentMinSpecial(String departmentMinSpecial) {
        this.departmentMinSpecial = departmentMinSpecial;
    }

    public String getDepartmentMinTotal() {
        return departmentMinTotal;
    }

    public void setDepartmentMinTotal(String departmentMinTotal) {
        this.departmentMinTotal = departmentMinTotal;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentSpecialSubject() {
        return departmentSpecialSubject;
    }

    public void setDepartmentSpecialSubject(String departmentSpecialSubject) {
        this.departmentSpecialSubject = departmentSpecialSubject;
    }
}
