package com.hajma.apps.mina2.retrofit;

import com.hajma.apps.mina2.retrofit.model.Alarm;
import com.hajma.apps.mina2.retrofit.model.ContactsModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface MinaInterface {

    //ask api request
    @Headers({"Accept: application/json"})
    @POST("/api/ask")
    @Multipart
    Call<ResponseBody> postAskRequest(@Part("languageId") RequestBody languageId,
                                      @Part("device_type") RequestBody device_type,
                                      @Part("device_id") RequestBody device_id,
                                      @Part("key") RequestBody key,
                                      @Part("sound") RequestBody sound,
                                      @Part("lat") RequestBody lat,
                                      @Part("lon") RequestBody lon,
                                      @Part("page") RequestBody page,
                                      @Header("Authorization") String token);


    //get subtutorial keys
    @GET("/api/tutorial")
    Call<ResponseBody> getSubTutorials(@Query("function") String function, @Query("languageId") int languageId);


    //teach me api
    @Headers({"Accept: application/json"})
    @Multipart
    @POST("/api/teach-me")
    Call<ResponseBody> postTeachMe(@Part("id") RequestBody idBody, @Part MultipartBody.Part sound);


    //register method
    @Multipart
    @POST("/api/register")
    Call<ResponseBody> postRegister(@Part("email") RequestBody email,
                                    @Part("username") RequestBody username,
                                    @Part("name") RequestBody name,
                                    @Part("gender") RequestBody gender,
                                    @Part("password") RequestBody password,
                                    @Part("c_password") RequestBody c_password,
                                    @Part("mobile") RequestBody mobile);

    //verify method
    @Headers({"Accept: application/json"})
    @Multipart
    @POST("/api/verify")
    Call<ResponseBody> postVerify(@Part("code") RequestBody code,
                                      @Header("Authorization") String token);


    //resend sms method
    @Headers({"Accept: application/json"})
    @POST("/api/resend-sms-for-verify")
    Call<ResponseBody> postResendSms(@Header("Authorization") String token);

    //login method
    @Multipart
    @POST("/api/login")
    Call<ResponseBody> postLogin(@Part("email") RequestBody email,
                                 @Part("password") RequestBody password);


    //forgot password email
    @GET("/api/send-random-code-to-email")
    Call<ResponseBody> forgotPasswordWithEmail(@Query("email") String email);

    //forgot password phone
    @GET("/api/send-random-code")
    Call<ResponseBody> forgotPasswordWithPhone(@Query("mobile") String mobile);


    //reset password
    @GET("/api/reset-password")
    Call<ResponseBody> resetPassword(@Query("code") String code,
                                               @Query("password") String password);



    //detailed book api
    @Headers({"Accept: application/json"})
    @GET("/api/get-book-detailed")
    Call<ResponseBody> getBookDetailed(@Query("languageId") String languageId,
                                       @Query("bookId") String bookId,
                                       @Header("Authorization") String token);

    //profile method
    @Headers({"Accept: application/json"})
    @POST("/api/profile")
    Call<ResponseBody> postProfile(@Header("Authorization") String token);

    //change profile picture method
    @Headers({"Accept: application/json"})
    @Multipart
    @POST("/api/change-profile-image")
    Call<ResponseBody> postChangeProfilePicture(@Part MultipartBody.Part part,
                                                @Header("Authorization") String token);

    //alarm api's


    //get alarm details api
    @Headers({"Accept: application/json"})
    @POST("/api/get-alarm")
    Call<ResponseBody> getAlarmDetails(@Header("Authorization") String token);




    @Headers({"Accept: application/json"})
    @POST("/api/set-one-alarm")
    Call<ResponseBody> setOneAlarm(@Body Alarm alarm,
                                   @Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @POST("/api/get-alarm")
    Call<ResponseBody> getAllAlarm(@Header("Authorization") String token);


    //slide suggest api
    @Headers({"Accept: application/json"})
    @GET("/api/key-words")
    Call<ResponseBody> getSlideSuggests(@Query("languageId") String langID);


}
