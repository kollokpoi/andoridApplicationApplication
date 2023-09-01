package com.example.bottom;

import static com.example.bottom.MainActivity.CHAT_ACTIVITY;
import static com.example.bottom.MainActivity.GROUP_CHAT;
import static com.example.bottom.MainActivity.MAIN_CHAT;
import static com.example.bottom.MainActivity.PRIVATE_CHAT;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.bottom.classes.CommandModel;
import com.example.bottom.elements.UserItem;
import com.example.bottom.fragments.ChatFragment;
import com.example.bottom.interfaces.IAPI;
import com.example.bottom.models.Chat;
import com.example.bottom.models.Message;
import com.example.bottom.models.MessageData;
import com.example.bottom.models.ObjectClass;
import com.example.bottom.models.User;
import com.example.bottom.services.RetrofitAuthorizeService;
import com.example.bottom.tcpModels.MessageModel;
import com.example.bottom.viewmodels.ChatsViewModel;
import com.example.bottom.viewmodels.UserChatViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatActivity extends Activity {

    private EditText messageEditText;
    private ImageButton sendButton;
    private ImageButton addFileBtn;
    private LinearLayout filesLayout;

    private LinearLayout topChatBlock;

    private int chatType;
    private Chat chat;

    private static final int REQUEST_PICK_IMAGES = 1; // Код запроса для выбора изображений
    private List<Uri> selectedImageUris = new ArrayList<>(); // Список для хранения выбранных изображений


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        addFileBtn = findViewById(R.id.fileAddButton);
        filesLayout = findViewById(R.id.chat_image_view_block);

        topChatBlock = findViewById(R.id.topChatBlock);

        this.chatType = getIntent().getIntExtra("chatType",1);

        switch (chatType){
            case MAIN_CHAT:{

            }
            break;
            case GROUP_CHAT:{
                ObjectClass object = (ObjectClass) getIntent().getSerializableExtra("chat");
                chat = object.getChat();
                topChatBlock.addView(new UserItem(this,object,CHAT_ACTIVITY));
            }
            break;
            case PRIVATE_CHAT:{
                UserChatViewModel userChat = (UserChatViewModel) getIntent().getSerializableExtra("chat");
                chat = userChat.getChat();
                topChatBlock.addView(new UserItem(this,userChat,CHAT_ACTIVITY));
            }
            break;
            default:{

            }break;
        }

        //обработка ввода текста
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Проверка наличия текста в EditText и соответствующее изменение видимости кнопки "отправить"
                if (charSequence.length() > 0) {
                    if (sendButton.getVisibility() == View.GONE) {
                        sendButton.setVisibility(View.VISIBLE);
                        sendButton.startAnimation(AnimationUtils.loadAnimation(ChatActivity.this, R.anim.slide_in_from_side));
                    }
                } else {
                    if (sendButton.getVisibility() == View.VISIBLE) {
                        Animation animation = AnimationUtils.loadAnimation(ChatActivity.this, R.anim.slide_out_to_side);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                sendButton.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        sendButton.startAnimation(animation);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // Установка слушателя клика на кнопку "отправить"


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> {
                    Socket socket = null;
                    try {
                        socket = new Socket("192.168.1.4", 5000);
                        OutputStream outputStream = socket.getOutputStream();

                        Gson serializer = new Gson();
                        List<MessageData> data = new ArrayList<>();
                        for (Uri uri : selectedImageUris) {
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(uri);
                                if (inputStream != null) {
                                    MessageData messageData = new MessageData();
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    byte[] buffer = new byte[1024];
                                    int bytesRead;
                                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                                    }
                                    messageData.setData(byteArrayOutputStream.toByteArray());
                                    data.add(messageData);
                                    inputStream.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        Message message = new Message();

                        message.setMessageText(String.valueOf(messageEditText.getText()));
                        message.setTimeStamp(String.valueOf(LocalDateTime.now()));
                        message.setMessageData(data);

                        MessageModel model = new MessageModel();

                        model.setChatId(chat.getId());
                        model.setUserId(MainActivity.appUser.getId());
                        model.setMessage(message);

                        CommandModel commandModel = new CommandModel();

                        String modelJson = serializer.toJson(model);

                        commandModel.setCommand("sendMessage");
                        commandModel.setData(modelJson);

                        String commandJson = serializer.toJson(commandModel);

                        byte[] resultBytes = commandJson.getBytes(StandardCharsets.UTF_8);

                        int offset = 0;

                        while (offset < resultBytes.length) {
                            int bytesToSend = Math.min(1024, resultBytes.length - offset);
                            outputStream.write(resultBytes, offset, bytesToSend);
                            offset += bytesToSend;
                        }

                        socket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

                messageEditText.setText("");
                filesLayout.removeAllViews();
                selectedImageUris.clear();
            }
        });

        addFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void updateMessages(){
        Retrofit retrofit = RetrofitAuthorizeService.getRetrofit();
        IAPI iapi = retrofit.create(IAPI.class);

        Call<List<Message>> messages = iapi.getMessages(chat.getId());

        messages.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                List<Message> messageList = response.body();
                addMessagesUi(messageList);
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {

            }
        });
    }

    private void addMessagesUi(List<Message> messages){
        List<Message> finalMessage;
        switch (chatType){
            case MAIN_CHAT:{
                List<Message> startMessage = ChatFragment.chatsViewModel.getMainChat().getMessages();
                for (Message m:messages){
                    startMessage.add(m);
                }
                finalMessage = startMessage;

                ChatFragment.chatsViewModel.getMainChat().setMessage(finalMessage);
            }break;
            case GROUP_CHAT:{

            }break;
            case PRIVATE_CHAT:{

            }break;
        }
    }


    public void backOnClick(View v) {
        finish();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Позволяет выбирать несколько изображений
        startActivityForResult(intent, REQUEST_PICK_IMAGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
            selectedImageUris.clear();
            if (clipData != null) {
                // Если выбрано несколько изображений
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    boolean contains = false;
                    for (Uri cu:selectedImageUris) {
                        if (cu == imageUri) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains)
                        selectedImageUris.add(imageUri);
                }
            } else {
                // Если выбрано только одно изображение
                Uri imageUri = data.getData();
                selectedImageUris.add(imageUri);
            }
            updateImageContainer();
        }
    }
    private void updateImageContainer() {
        // Очищаем контейнер перед добавлением новых изображений
        filesLayout.removeAllViews();
        filesLayout.setPadding(0,10,0,10);

        // Добавляем ImageView для каждого выбранного изображения
        for (int i =0; i<selectedImageUris.size();i++) {

            View view = LayoutInflater.from(this).inflate(R.layout.image_layout, null);

            ImageView imageView = view.findViewById(R.id.mainImageView);
            ImageView cancelView = view.findViewById(R.id.cancel_image_view);

            // Устанавливаем максимальные размеры для ImageView
            int minWidthPixels = dpToPx(120); // Максимальная ширина в пикселях (например, 300dp)
            int maxHeightPixels = dpToPx(80); // Максимальная высота в пикселях (например, 300dp)

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(minWidthPixels, maxHeightPixels);
            layoutParams.setMargins(dpToPx(5),0,dpToPx(5),0);
            view.setLayoutParams(layoutParams);

            // Устанавливаем атрибуты масштабирования и выравнивания

            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Добавляем изображение в ImageView
            imageView.setImageURI(selectedImageUris.get(i));

            int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFullScreenActivity(finalI);
                }
            });

            int finalI1 = i;
            cancelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedImageUris.remove(finalI1);
                    updateImageContainer();
                }
            });
            // Добавляем ImageView в контейнер
            filesLayout.addView(view);
        }
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    private void openFullScreenActivity(int position) {
        Intent intent = new Intent(this, FullscreenImageActivity.class);
        intent.putParcelableArrayListExtra("imageUris", new ArrayList<>(selectedImageUris));
        intent.putExtra("position", position);
        startActivity(intent);
    }
}