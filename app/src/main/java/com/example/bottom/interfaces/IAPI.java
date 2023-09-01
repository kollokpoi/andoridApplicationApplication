package com.example.bottom.interfaces;

import com.example.bottom.MainActivity;
import com.example.bottom.models.Message;
import com.example.bottom.viewmodels.ChatsViewModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IAPI {

    @GET("api/User")
    Call<List<String>> getid();

    @GET("Chats/getChats")
    public Call<ChatsViewModel> getChats();

    @GET("Chats/getChats")
    public Call<ChatsViewModel> GetId();
    @POST("User/login")
    Call<ResponseBody> login(@Query("username")String username, @Query("password")String password);

    @GET("User/getMessages")
    Call<List<Message>> getMessages(@Query("id")int id);
}
