package com.example.bottom.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bottom.R;
import com.example.bottom.models.ObjectClass;

public class ObjectInfoFragment extends Fragment {

    TextView sectionsText;
    TextView addressText;

    ObjectClass object;
    public ObjectInfoFragment(ObjectClass object){
        this.object = object;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_object_info, container, false);

        sectionsText = view.findViewById(R.id.object_sections);
        addressText = view.findViewById(R.id.object_address);

        addressText.setText(object.getName());
        sectionsText.setText(String.valueOf(object.getSectionCount()));

        return view;
    }
}