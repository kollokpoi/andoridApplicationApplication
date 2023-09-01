package com.example.bottom.elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.bottom.AchieveActivity;
import com.example.bottom.R;
import com.example.bottom.models.Apartament;

import java.io.Serializable;
import java.util.List;

public class AchieveItem extends LinearLayout {
    private Apartament apartament;
    @SuppressLint("SetTextI18n")
    public AchieveItem(Context context, Apartament apartament) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);

        ImageView image = new ImageView(context);
        TextView floor = new TextView(context);
        TextView room = new TextView(context);

        List<byte[]> imageData = apartament.getImageBytes();
        if (imageData!=null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData.get(0), 0, imageData.get(0).length);
            image.setImageBitmap(bitmap);
        }
        else{
            image.setImageResource(R.drawable.arrow);
        }

        int imageHeight = dpToPx(90);
        int imageWeight = dpToPx(100);

        LayoutParams imageParams = new LayoutParams(imageWeight,imageHeight);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setLayoutParams(imageParams);

        floor.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        room.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        floor.setText(context.getString(R.string.achieve_floor) + " " + apartament.getFloor());
        room.setText(context.getString(R.string.apart_aparts) + " " + apartament.getRoomNumber());

        addView(image);
        addView(floor);
        addView(room);

        this.apartament = apartament;

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AchieveActivity.class);
                intent.putExtra("apartment", apartament);
                context.startActivity(intent);
            }
        });
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
