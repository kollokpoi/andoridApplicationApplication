package com.example.bottom.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObjectClass implements Serializable {
    public ObjectClass(){
        Name = "fdg";
    }
    private  int Id;
    private String Name;
    private List<User> Users;
    private Chat Chat;

    private int SectionCount;

    private List<UserImage> Images;

    private List<Apartament> Apartments;
    private List<Apartament> acceptedApartments;
    private List<Apartament> declinedApartments;


    public int getSectionCount() {
        return SectionCount;
    }

    public List<Apartament> getAcceptedApartments() {
        return acceptedApartments;
    }

    public List<Apartament> getDeclinedApartments() {
        return declinedApartments;
    }

    public List<Apartament> getApartments() {
        return Apartments;
    }

    public String getName() {
        return Name;
    }
    public int getId() {
        return Id;
    }
    public List<User> getUsers() {
        return Users;
    }
    public Chat getChat() {
        return Chat;
    }

    public List<UserImage> getImages() {
        return Images;
    }

    public void setImages(List<UserImage> images) {
        Images = images;
    }
    public List<byte[]> GetImagesBytes(){
        List<byte[]> imagesData = new ArrayList<>();
        for (int i = 0; i<this.Images.size();i++){
            imagesData.add(this.Images.get(i).getImageData());
        }
        return imagesData;
    }
    public byte[] getMainImage(){
        if (Images.size()!=0)
            return Images.get(0).getImageData();
        else
            return null;
    }
}
