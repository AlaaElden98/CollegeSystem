package com.usama.runtime.model;

public class Department {
    private String capacity, id, min_special, min_total, name;

    public Department() {
    }

    public Department(String capacity, String id, String min_special, String min_total, String name) {
        this.capacity = capacity;
        this.id = id;
        this.min_special = min_special;
        this.min_total = min_total;
        this.name = name;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMin_special() {
        return min_special;
    }

    public void setMin_special(String min_special) {
        this.min_special = min_special;
    }

    public String getMin_total() {
        return min_total;
    }

    public void setMin_total(String min_total) {
        this.min_total = min_total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
