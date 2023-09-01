package com.example.bottom.models;

import java.io.Serializable;

public class ApartmentStatus implements Serializable {

    private String statusName = "Не построено";
    private int id = 0;

    public ApartmentStatus(int id, String statusName){
        this.id = id;
        this.statusName = statusName;
    }

    public String getName() {
        return statusName;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return statusName;
    }
}
