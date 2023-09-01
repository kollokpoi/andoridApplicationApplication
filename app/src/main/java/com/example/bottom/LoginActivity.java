package com.example.bottom;

import static com.example.bottom.MainActivity.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bottom.interfaces.IAPI;
import com.example.bottom.models.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText loginView;
    EditText passwordView;
    Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginView = findViewById(R.id.loginTextView);
        passwordView = findViewById(R.id.passwordTextView);
        loginButton = findViewById(R.id.loginButton);

        loginView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SetButtonVisible(String.valueOf(s),String.valueOf(passwordView.getText()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SetButtonVisible(String.valueOf(loginView.getText()), String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = String.valueOf(loginView.getText());
                String password = String.valueOf(passwordView.getText());

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                IAPI iapi = retrofit.create(IAPI.class);
                Call<ResponseBody> call = iapi.login(username, password);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String responseBody = response.body().string();
                                // Обработка ответа как строки JSON
                                JSONObject jsonObject = new JSONObject(responseBody);
                                Gson gson = new Gson();

                                String access_token = jsonObject.getString("access_token");
                                String username = jsonObject.getString("username");
                                User user = gson.fromJson(jsonObject.getString("user"),User.class);

                                MainActivity.appUser = user;
                                MainActivity.userToken = access_token;

                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (response.code()==401){
                            String notificationText = getString(R.string.unuthorize_notification);
                            Toast.makeText(getApplicationContext(), notificationText, Toast.LENGTH_SHORT).show();
                        } else {
                            String notificationText = getString(R.string.failture_notification);
                            Toast.makeText(getApplicationContext(), notificationText, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        String notificationText = getString(R.string.failture_notification);
                        Toast.makeText(getApplicationContext(), notificationText, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void SetButtonVisible(String l, String p){
        if (l.length() > 0 && p.length()>0) {
            if (loginButton.getVisibility() == View.GONE) {
                loginButton.setVisibility(View.VISIBLE);
                loginButton.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_in_from_side));
            }
        } else {
            if (loginButton.getVisibility() == View.VISIBLE) {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_out_to_side);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        loginButton.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                loginButton.startAnimation(animation);
            }
        }
    }
}