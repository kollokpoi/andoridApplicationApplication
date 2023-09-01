package com.example.bottom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.bottom.adapters.ImagePagerAdapter;

import java.util.List;


public class FullscreenImageActivity extends AppCompatActivity {
    private List<Uri> imageUris;
    private ViewPager viewPager;
    private ImagePagerAdapter adapter;
    private TextView counterText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        counterText = findViewById(R.id.fullscreen_title_counter);
        ImageView backImage = findViewById(R.id.full_screen_back_image);
        viewPager = findViewById(R.id.viewPager);

        backImage.setOnClickListener(this::backImageOnClick);


        // Получаем список выбранных изображений из Intent
        imageUris = getIntent().getParcelableArrayListExtra("imageUris");
        int position = getIntent().getIntExtra("position",0);

        counterText.setText((position+1) + " из " + imageUris.size());


        // Создаем адаптер для ViewPager
        adapter = new ImagePagerAdapter(imageUris);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                counterText.setText((position+1) + " из " + imageUris.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void backImageOnClick(View view){
        finish();
    }
}