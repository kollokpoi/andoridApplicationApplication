package com.example.bottom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bottom.adapters.ApartmentImagePager;
import com.example.bottom.adapters.StatusSpinnerAdapter;
import com.example.bottom.models.Apartament;
import com.example.bottom.models.ApartmentStatus;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AchieveActivity extends AppCompatActivity {

    Apartament apartament;

    TextView titleView;
    ImageView backView;
    ImageView infoView;

    //image block views
    ViewPager viewPager;
    LinearLayout ItemHolder;

    //main information block
    TextView squareView;
    Spinner statusSpinner;
    TextView descriptionView;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achieve);

        apartament = (Apartament) getIntent().getSerializableExtra("apartment");

        //top views
        titleView = findViewById(R.id.achieve_title);
        backView = findViewById(R.id.achieve_back_image);
        infoView = findViewById(R.id.achieve_info_image);

        //image block views
        viewPager = findViewById(R.id.achieve_view_pager);
        ItemHolder = findViewById(R.id.achieve_image_count_holder);

        //main information block
        squareView = findViewById(R.id.achieve_item_square_view);
        statusSpinner = findViewById(R.id.achieve_item_status_spinner);
        descriptionView = findViewById(R.id.achieve_item_description_view);

        //set floor and apart
        titleView.setText("Этаж " + apartament.getFloor() + " Квартира " + apartament.getRoomNumber());

        StatusSpinnerAdapter adapter = new StatusSpinnerAdapter(this,MainActivity.apartmentStatuses);

        statusSpinner.setAdapter(adapter);
        statusSpinner.setSelection(apartament.getStatusId());

        byte[] image = getByteArrayFromResource(this,R.drawable.sendicon);

        List<byte[]> ima = new ArrayList<>();
        ima.add(image);
        ima.add(image);
        ima.add(image);
        ima.add(image);

        apartament.setImageBytes(ima);

        List<byte[]> images = apartament.getImageBytes();

        int fixedHeightInPx = 4;

        ApartmentImagePager pagerAdapter = new ApartmentImagePager(images);
        viewPager.setAdapter(pagerAdapter);

        for (int i =0;i<images.size();i++){
            View view = new View(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,dpToPx(fixedHeightInPx),1);
            params.setMargins(dpToPx(2),0,dpToPx(2),0);
            params.gravity = Gravity.CENTER_VERTICAL;

            if (i==0){
                params.weight=2;
            }

            view.setLayoutParams(params);
            view.setBackground(getResources().getDrawable(R.drawable.dark_rounded_bg));


            int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(finalI,true);
                }
            });

            ItemHolder.addView(view);

        }

        final int[] pickedItem = {0};

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                View view = ItemHolder.getChildAt(position);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
                params.weight = 2;
                view.setLayoutParams(params);

                View lastView = ItemHolder.getChildAt(pickedItem[0]);

                LinearLayout.LayoutParams lastParams = (LinearLayout.LayoutParams) lastView.getLayoutParams();
                lastParams.weight = 1;
                lastView.setLayoutParams(lastParams);

                pickedItem[0] = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //go back click
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static byte[] getByteArrayFromResource(Context context, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
    private void animateViewSize(final View view, int startHeight, int endHeight) {
        int duration = 200;
        // Создайте объект ValueAnimator для изменения высоты
        ValueAnimator heightAnimator = ValueAnimator.ofInt(dpToPx(startHeight), dpToPx(endHeight));
        heightAnimator.setDuration(duration);
        heightAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        heightAnimator.start();
    }
}