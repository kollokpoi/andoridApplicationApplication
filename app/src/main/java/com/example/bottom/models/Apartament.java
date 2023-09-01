package com.example.bottom.models;

import java.io.Serializable;
import java.util.List;

public class Apartament implements Serializable {


    public Apartament(){
        floor = 1;
        status = new ApartmentStatus(1,"Не построено");
        roomNumber = 5;
    }

    private ApartmentStatus status;
    private List<byte[]> imageBytes;
    private int floor;
    private int roomNumber;

    public int getStatusId() {
        return status.getId();
    }

    public void setImageBytes(List<byte[]> imageBytes) {
        this.imageBytes = imageBytes;
    }

    public List<byte[]> getImageBytes() {
        return imageBytes;
    }

    public int getFloor() {
        return floor;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

}
