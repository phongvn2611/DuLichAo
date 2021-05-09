package com.example.virtualtravelapp.model;

public class Group {
    private int idGroup;
    private String name;

    public Group() {}

    public Group(int idGroup, String name) {
        this.idGroup = idGroup;
        this.name = name;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
