package com.usama.runtime.model;

public class Posts {
    private String name, subject, description, dataAndTime;

    public Posts() {
    }

    public Posts(String name, String subject, String description, String dataAndTime) {
        this.name = name;
        this.subject = subject;
        this.description = description;
        this.dataAndTime = dataAndTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataAndTime() {
        return dataAndTime;
    }

    public void setDataAndTime(String dataAndTime) {
        this.dataAndTime = dataAndTime;
    }
}
