package com.example.bottom.fragments;

import static com.example.bottom.MainActivity.CHAT_ACTIVITY;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.bottom.GroupActivity;
import com.example.bottom.R;
import com.example.bottom.adapters.ApartmentImagePager;
import com.example.bottom.elements.UserItem;
import com.example.bottom.models.ObjectClass;
import com.example.bottom.models.User;
import com.example.bottom.models.UserImage;
import com.example.bottom.viewmodels.UserChatViewModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ObjectUsersFragment extends Fragment {


    ObjectClass object;


    //image block views
    ViewPager viewPager;
    LinearLayout ItemHolder;


    LinearLayout UserItemsHolder;

    public ObjectUsersFragment(ObjectClass object){
        this.object = object;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_object_users, container, false);

        //image block views
        viewPager = rootView.findViewById(R.id.group_info_view_pager);
        ItemHolder = rootView.findViewById(R.id.group_info_image_count_holder);


        UserItemsHolder = rootView.findViewById(R.id.group_users_holder);

        byte[] image = getByteArrayFromResource(requireContext(),R.drawable.sendicon);

        //в дальнейшем удалить
        List<byte[]> ima = new ArrayList<>();
        ima.add(image);
        ima.add(image);
        ima.add(image);
        ima.add(image);

        List<UserImage> UserImages = new ArrayList<>();
        for (byte[] im:ima){
            UserImage img = new UserImage();
            img.setImageData(im);
            UserImages.add(img);
        }
        object.setImages(UserImages);
        //в дальнейшем удалить

        List<byte[]> images = object.GetImagesBytes();

        int fixedHeightInPx = 4;

        ApartmentImagePager pagerAdapter = new ApartmentImagePager(images);
        viewPager.setAdapter(pagerAdapter);

        //добаление картинок
        for (int i =0;i<images.size();i++){
            View view = new View(requireContext());

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


        //добавление пользователей
        for (User u: object.getUsers()){

            UserChatViewModel userChatViewModel = new UserChatViewModel();
            userChatViewModel.setUser(u);

            UserItem userItem = new UserItem(requireContext(),userChatViewModel,CHAT_ACTIVITY);

            UserItemsHolder.addView(userItem);
        }

        //изменение позиции картинки
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
        return rootView;
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    //получение байт картинки
    public static byte[] getByteArrayFromResource(Context context, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
}