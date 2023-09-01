package com.example.bottom.models;

import com.google.gson.annotations.SerializedName;

public class MessageData {
    @SerializedName("NotMappedData")
    private byte[] Data;


    public byte[] getData() {
        return Data;
    }
    public void setData(byte[] data) {
        this.Data = data;
    }
}
