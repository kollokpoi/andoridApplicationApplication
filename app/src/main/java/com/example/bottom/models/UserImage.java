package com.example.bottom.models;

import java.io.Serializable;

public class UserImage implements Serializable {
    private int id;
    private byte[] ImageData;

    public int getId() {
        return id;
    }

    public byte[] getImageData() {
        return ImageData;
    }

    public void setImageData(byte[] imageData) {
        ImageData = imageData;
    }
}
