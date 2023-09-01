package com.example.bottom.models;

import android.media.Image;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User implements Serializable {



    private String Id;
    private String FirstName;
    private String LastName;
    private String Email;
    private String PhoneNumber;
    private String LastOnline;
    private List<UserImage> Images;

    public void setImages(List<UserImage> images) {
        Images = images;
    }

    public List<UserImage> getImages() {
        return Images;
    }

    public String getLastOnline() {
        return LastOnline;
    }
    public String getEmail() {
        return Email;
    }
    public String getFirstName() {
        return FirstName;
    }
    public String getLastName() {
        return LastName;
    }
    public String getId() {
        return Id;
    }
    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public byte[] GetMainImage(){
        if (Images!=null){
            return null;
        }else{
            return Images.get(0).getImageData();
        }
    }

    public String GetFullName(){
        return this.FirstName + " " + this.LastName;
    }

    public List<byte[]> GetImagesBytes(){
        List<byte[]> imagesData = new ArrayList<>();
        for (int i = 0; i<this.Images.size();i++){
            imagesData.add(this.Images.get(i).getImageData());
        }
        return imagesData;
    }

    public  String getStatus() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String result = "";

        try {
            Date currentDate = new Date();
            Date receivedDate = dateFormat.parse(LastOnline);

            long timeDifference = Math.abs((currentDate.getTime() - receivedDate.getTime()) / 1000); // Difference in seconds

            if (timeDifference < 120) {
                result = "в сети";
            } else if (timeDifference < 172800) { // Less than 48 hours
                if (isYesterday(receivedDate)) {
                    result = "был в сети вчера в " + formatTime(receivedDate);
                } else {
                    result = "был в сети в " + formatTime(receivedDate);
                }
            } else {
                result = "последний раз в сети " + formatDate(receivedDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    private  boolean isYesterday(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateStr = dateFormat.format(new Date());
        String receivedDateStr = dateFormat.format(date);
        return currentDateStr.equals(receivedDateStr);
    }

    private  String formatTime(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(date);
    }

    private  String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }
}
