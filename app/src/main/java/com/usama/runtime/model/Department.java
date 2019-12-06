package com.usama.runtime.model;

public class Department {
    private String departmentCapacity, departmentMinSpecial, departmentMinValue, departmentName;

    public Department() {
    }

    public Department(String departmentCapacity, String departmentMinSpecial, String departmentMinValue, String departmentName) {
        this.departmentCapacity = departmentCapacity;
        this.departmentMinSpecial = departmentMinSpecial;
        this.departmentMinValue = departmentMinValue;
        this.departmentName = departmentName;
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

    public String getDepartmentMinValue() {
        return departmentMinValue;
    }

    public void setDepartmentMinValue(String departmentMinValue) {
        this.departmentMinValue = departmentMinValue;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
