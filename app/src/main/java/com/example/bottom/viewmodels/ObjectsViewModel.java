package com.example.bottom.viewmodels;

import com.example.bottom.models.ObjectClass;

import java.util.ArrayList;
import java.util.List;

public class ObjectsViewModel {

    public ObjectsViewModel(){
        totalAccepted=5;
        totalBuilt=4;
        totalDeclined=7;

        objectList = new ArrayList<>();
        objectList.add(new ObjectClass());
        objectList.add(new ObjectClass());
    }
    private int totalBuilt;
    private int totalAccepted;
    private int totalDeclined;
    private List<ObjectClass> objectList;

    public List<ObjectClass> getObjectList() {
        return objectList;
    }

    public int getTotalAccepted() {
        return totalAccepted;
    }

    public int getTotalBuilt() {
        return totalBuilt;
    }

    public int getTotalDeclined() {
        return totalDeclined;
    }
}
