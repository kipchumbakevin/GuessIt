package com.guess.guessit;

import com.guess.guessit.models.MessagesModel;
import com.guess.guessit.models.QuestionsModel;
import com.guess.guessit.models.UsersModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderInterface {
    @FormUrlEncoded
    @POST("api/reg")
    Call<MessagesModel> regUser(
            @Field("username")String usern,
            @Field("pin")String pin

    );
    @FormUrlEncoded
    @POST("api/getuser")
    Call<UsersModel> fetchUser(
            @Field("username")String us

    );
    @FormUrlEncoded
    @POST("api/earnfromquiz")
    Call<MessagesModel> earnQ(
            @Field("username")String us,
            @Field("id")String id

    );
    @FormUrlEncoded
    @POST("api/earnfromguess")
    Call<MessagesModel> earnG(
            @Field("username")String us,
            @Field("key")String key

    );
    @FormUrlEncoded
    @POST("api/freecoins")
    Call<MessagesModel> free(
            @Field("username")String us

    );

    @GET("api/getquestions")
    Call<List<QuestionsModel>> getQ();
}
