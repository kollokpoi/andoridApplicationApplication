package com.example.bottom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bottom.adapters.ApartmentImagePager;
import com.example.bottom.elements.UserItem;
import com.example.bottom.fragments.ObjectInfoFragment;
import com.example.bottom.fragments.ObjectUsersFragment;
import com.example.bottom.models.ObjectClass;
import com.example.bottom.models.User;
import com.example.bottom.models.UserImage;
import com.example.bottom.viewmodels.UserChatViewModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {


    ObjectClass object;

    //image block views
    ViewPager viewPager;

    TextView titleText;

    ImageView backView;

    private static ObjectUsersFragment objectUsersFragment;
    private static ObjectInfoFragment objectInfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        object = (ObjectClass) getIntent().getSerializableExtra("object");
        viewPager = findViewById(R.id.object_view_pager);
        backView = findViewById(R.id.group_info_back_image);
        titleText = findViewById(R.id.group_title);

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        objectUsersFragment = new ObjectUsersFragment(object);
        objectInfoFragment = new ObjectInfoFragment(object);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(objectUsersFragment);
        fragments.add(objectInfoFragment);


        PagerAdapter pagerAdapter = new com.example.bottom.adapters.PagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(pagerAdapter);


        titleText.setText(object.getName());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 1:{
                        titleText.setText("Информация");
                    }break;
                    default:{
                        titleText.setText(object.getName());
                    }break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}