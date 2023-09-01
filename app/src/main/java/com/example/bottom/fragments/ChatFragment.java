package com.example.bottom.fragments;

import static com.example.bottom.MainActivity.CHAT_FRAGMENT;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottom.R;
import com.example.bottom.elements.UserItem;
import com.example.bottom.interfaces.IAPI;
import com.example.bottom.models.ObjectClass;
import com.example.bottom.services.RetrofitAuthorizeService;
import com.example.bottom.viewmodels.ChatsViewModel;
import com.example.bottom.viewmodels.UserChatViewModel;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatFragment extends Fragment {
    private LinearLayout objectsLinear;
    private LinearLayout chatsLinear;

    private LinearLayout objectsText;
    private LinearLayout chatsText;

    private ImageView objectArrow;
    private ImageView chatArrow;

    private LinearLayout chatsHolder;

    private int duration = 300;
    private boolean objectExpanded = true;
    private boolean chatsExpanded = true;

    private int chatsTransition;
    private int objectsTransition;
    private ProgressBar progressBar;
    public static ChatsViewModel chatsViewModel;

    private List<UserItem> chatItems;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        //startMessageReceiver();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        objectsLinear = view.findViewById(R.id.objects_view);
        chatsLinear = view.findViewById(R.id.chats_view);

        objectsText = view.findViewById(R.id.objects_text);
        chatsText = view.findViewById(R.id.chats_text);

        objectArrow = view.findViewById(R.id.objects_arrow);
        chatArrow = view.findViewById(R.id.chats_arrow);

        chatsHolder = view.findViewById(R.id.chats_holder);

        progressBar = view.findViewById(R.id.chats_progress_bar);


        AddData();
    }
    private void AddListeners(){
        objectsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objectExpanded) {
                    collapseLinearLayout(objectsLinear, objectsTransition);
                    rotateArrow(objectArrow);
                } else {
                    expandLinearLayout(objectsLinear, objectsTransition);
                    rotateArrowBack(objectArrow);
                }
                objectExpanded = !objectExpanded;
            }
        });
        //нажатие на "Чаты"
        chatsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatsExpanded) {
                    collapseLinearLayout(chatsLinear, chatsTransition);
                    rotateArrow(chatArrow);
                } else {
                    expandLinearLayout(chatsLinear, chatsTransition);
                    rotateArrowBack(chatArrow);
                }
                chatsExpanded = !chatsExpanded;
            }
        });

        //действие при загрузке объектов
        objectsLinear.post(new Runnable() {
            @Override
            public void run() {
                objectsTransition = objectsLinear.getHeight();

                ViewGroup.LayoutParams layoutParams = objectsLinear.getLayoutParams();

                if (objectExpanded) {
                    layoutParams.height = objectsTransition;
                    objectsLinear.setLayoutParams(layoutParams);

                    objectArrow.setRotation(90);
                } else {
                    layoutParams.height = 0;
                    objectsLinear.setLayoutParams(layoutParams);

                    objectArrow.setRotation(0);
                }
            }
        });
        //действие при загрузке чатов
        chatsLinear.post(new Runnable() {
            @Override
            public void run() {
                chatsTransition = chatsLinear.getHeight();

                ViewGroup.LayoutParams layoutParams = chatsLinear.getLayoutParams();

                if (chatsExpanded) {
                    layoutParams.height = chatsTransition;
                    chatsLinear.setLayoutParams(layoutParams);

                    chatArrow.setRotation(90);
                } else {
                    layoutParams.height = 0;
                    chatsLinear.setLayoutParams(layoutParams);

                    chatArrow.setRotation(0);
                }
            }
        });
    }

    private void AddData(){

        Retrofit retrofit = RetrofitAuthorizeService.getRetrofit();
        IAPI iapi = retrofit.create(IAPI.class);
        Call<ChatsViewModel> chats = iapi.getChats();
        chats.enqueue(new Callback<ChatsViewModel>() {
            @Override
            public void onResponse(Call<ChatsViewModel> call, Response<ChatsViewModel> response) {
                chatsViewModel = response.body();
                chatItems = new ArrayList<>();

                progressBar.setVisibility(View.GONE);
                chatsHolder.setVisibility(View.VISIBLE);
                for (ObjectClass objectClass: chatsViewModel.getObjectChats()){
                    UserItem ui = new UserItem(requireContext(),objectClass,CHAT_FRAGMENT);
                    chatItems.add(ui);
                    objectsLinear.addView(ui);
                }
                for (UserChatViewModel uc: chatsViewModel.getPrivateChats()){
                    UserItem ui = new UserItem(requireContext(),uc,CHAT_FRAGMENT);
                    chatItems.add(ui);
                    chatsLinear.addView(ui);
                }

                AddListeners();
                //startMessageReceiver();
            }
            @Override
            public void onFailure(Call<ChatsViewModel> call, Throwable t) {

            }
        });
    }

    private void expandLinearLayout(@NonNull LinearLayout layout, int translationY) {
        layout.setVisibility(View.VISIBLE);

        ValueAnimator animator = ValueAnimator.ofInt(0, translationY);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
                layoutParams.height = animatedValue;
                layout.setLayoutParams(layoutParams);
            }
        });
        animator.start();
    }

    private void collapseLinearLayout(LinearLayout layout,int translationY) {
        ValueAnimator animator = ValueAnimator.ofInt(translationY, 0);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
                layoutParams.height = animatedValue;
                layout.setLayoutParams(layoutParams);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    private void rotateArrow(ImageView imageView){
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 90f, 0f);
        rotateAnimator.setDuration(duration);
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnimator);
        animatorSet.start();
    }
    private void rotateArrowBack(ImageView imageView){
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 90f);
        rotateAnimator.setDuration(duration);
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnimator);
        animatorSet.start();
    }


    private Handler handler = new Handler(Looper.getMainLooper());

    private static final String SERVER_IP = "192.168.1.4";
    private static final int SERVER_PORT = 5000;

    //private void startMessageReceiver() {
    //    new Thread(() -> {
    //        try {
    //            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
    //            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
    //            String line;
    //            while ((line = reader.readLine()) != null) {
    //                final String receivedMessage = line;
    //                handler.post(new Runnable() {
    //                    @Override
    //                    public void run() {
//
    //                    }
    //                });
    //            }
//
    //            reader.close();
    //            socket.close();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    }).start();
    //}
}

