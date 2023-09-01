package com.example.bottom.elements;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.example.bottom.MainActivity.DIALOG_MESSAGE;
import static com.example.bottom.MainActivity.GROUP_CHAT;
import static com.example.bottom.MainActivity.PRIVATE_CHAT;
import static com.example.bottom.MainActivity.SELF_MESSAGE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bottom.models.Message;
import com.example.bottom.models.MessageData;
import com.example.bottom.models.User;

import java.util.List;

public class MessageItem extends LinearLayout {
    int mode;
    int chatMode;
    Message message;
    TextView messageTextView;
    ImageView userSenderImage;
    TextView userSenderName;
    TextView messageTimeStamp;
    LinearLayout messageBody;
    LinearLayout ImagesBody;

    public MessageItem(Context context, Message message, int userMode, int chatMode) {
        super(context);

        messageTextView = new TextView(context);
        messageTimeStamp = new TextView(context);
        messageBody = new LinearLayout(context);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int bodyWidth =  screenWidth/3;

        LayoutParams mainViewParams = new LayoutParams(bodyWidth,WRAP_CONTENT);
        setOrientation(HORIZONTAL);
        setLayoutParams(mainViewParams);

        messageBody.setOrientation(VERTICAL);

        this.message = message;
        this.mode = userMode;
        this.chatMode = chatMode;

        messageTextView.setText(message.getMessageText());

        List<MessageData> messageData = message.getMessageData();
        if (messageData!=null){
            ImagesBody = new LinearLayout(context);

            if(messageData.size()==1){
                ImageView soloImage = new ImageView(context);
                byte[] imageBytes = messageData.get(0).getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                soloImage.setImageBitmap(bitmap);
                ImagesBody.addView(soloImage);
            }
            else {

            }
        }


        if (userMode == DIALOG_MESSAGE && chatMode == GROUP_CHAT){
            userSenderImage = new ImageView(context);
            userSenderName = new TextView(context);

            createGroupMessageUI();
        }
        else if (userMode == SELF_MESSAGE){

        }
        else if (userMode == DIALOG_MESSAGE){

        }
    }

    private void createGroupMessageUI(){
        User user = message.getUser();

        userSenderName.setText(message.getUser().GetFullName());

        byte[] userImage = user.GetMainImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(userImage, 0, userImage.length);
        userSenderImage.setImageBitmap(bitmap);
    }
}
