package com.example.bottom.elements;

import static com.example.bottom.MainActivity.CHAT_ACTIVITY;
import static com.example.bottom.MainActivity.CHAT_FRAGMENT;
import static com.example.bottom.MainActivity.GROUP_CHAT;
import static com.example.bottom.MainActivity.PRIVATE_CHAT;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bottom.ChatActivity;
import com.example.bottom.GroupActivity;
import com.example.bottom.R;
import com.example.bottom.UserActivity;
import com.example.bottom.models.Chat;
import com.example.bottom.models.ObjectClass;
import com.example.bottom.viewmodels.UserChatViewModel;


public class UserItem extends LinearLayout {

    ImageView userImage;
    LinearLayout textBlock;
    TextView titleView;
    TextView statusView;

    Chat chat;
    Context context;


    //объект в списке чатов
    public UserItem(Context context, ObjectClass object, int mode) {
        super(context);
        this.context = context;
        chat = object.getChat();
        setViews(object.getMainImage());
        titleView.setText(object.getName());

        switch (mode){
            case CHAT_FRAGMENT:{
                statusView.setText(chat.GetLastMessage());
                setOnClickListener(v -> {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("chatType",GROUP_CHAT);
                    intent.putExtra("chat",object);
                    context.startActivity(intent);
                });
            }break;
            case CHAT_ACTIVITY:{
                statusView.setText("Участников: "+chat.getMembers().size());
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, GroupActivity.class);
                        intent.putExtra("object",object);
                        context.startActivity(intent);
                    }
                });
            }break;
        }
    }

    //личные диалоги в списке чатов
    public UserItem(Context context, UserChatViewModel userChat,int mode) {
        super(context);
        this.context = context;
        Chat chat = userChat.getChat();
        setViews(userChat.getUser().GetMainImage());

        titleView.setText(userChat.getUser().GetFullName());

        switch (mode){
            case CHAT_FRAGMENT:{
                statusView.setText(chat.GetLastMessage());
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("chatType",PRIVATE_CHAT);
                        intent.putExtra("chat",userChat);
                        context.startActivity(intent);
                    }
                });
            }break;
            case CHAT_ACTIVITY:{
                statusView.setText(userChat.getUser().getStatus());
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, UserActivity.class);
                        intent.putExtra("user",userChat.getUser());
                        context.startActivity(intent);
                    }
                });
            }break;
        }

    }

    private void setViews(byte[] imageData){

        userImage = new ImageView(context);
        textBlock = new LinearLayout(context);
        titleView = new TextView(context);
        statusView = new TextView(context);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,dpToPx(10),0,dpToPx(10));

        setLayoutParams(params);

        //main layout settings
        setOrientation(LinearLayout.HORIZONTAL);

        //user image settings
        LinearLayout.LayoutParams imageParams = new LayoutParams(dpToPx(40),dpToPx(40));

        imageParams.gravity = Gravity.CENTER_VERTICAL;
        userImage.setLayoutParams(imageParams);

        //text block settings
        textBlock.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(dpToPx(10),0,0,0);
        textBlock.setLayoutParams(textParams);

        if (imageData!=null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            userImage.setImageBitmap(bitmap);
        }else{
            userImage.setImageResource(R.drawable.ic_settings_black);
        }

        textBlock.addView(titleView);
        textBlock.addView(statusView);
        addView(userImage);
        addView(textBlock);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
