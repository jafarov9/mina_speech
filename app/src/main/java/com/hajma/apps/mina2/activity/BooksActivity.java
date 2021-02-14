package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.adapter.BookAdapter;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;
import com.hajma.apps.mina2.retrofit.model.BookApiModel;
import com.hajma.apps.mina2.utils.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BooksActivity extends AppCompatActivity {


    private LottieAnimationView lottieBackBooks;
    private JSONObject result;
    private String resultString;
    private RecyclerView rvBooks;
    private MinaInterface minaInterface;
    private ArrayList<BookApiModel> bookList = new ArrayList<>();
    private int pagenumber;
    private int itemcount = 20;
    private int visibleitemcount, totalitemcount, pastvisibleitems;
    private boolean loading= true;
    private BookAdapter adapter;
    private boolean isMyBook;
    private String query = "";
    private SharedPreferences sharedPreferences;
    private boolean isLogin;
    private String token = "";
    private int langID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        String lang = LocaleHelper.getPersistedData(this, "az");
        if(lang.equals("az")) {
            langID = 1;
        }else if(lang.equals("ru")){
            langID = 4;

        }


        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        isLogin = sharedPreferences.getBoolean("isLogin", false);
        token = sharedPreferences.getString("token", "");


        minaInterface = ApiUtils.getMinaInterface();

        resultString = getIntent().getStringExtra("result");
        query = getIntent().getStringExtra("key");


        try {
            if(resultString != null) {
                result = new JSONObject(resultString);
                if(!result.isNull("is_logged")) {
                    isMyBook = result.getBoolean("is_logged");

                    if(!isMyBook) {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                }
            }else {
                result = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //init views
        lottieBackBooks = findViewById(R.id.lottiBackBooks);
        lottieBackBooks.setOnClickListener(v -> onBackPressed());

        rvBooks = findViewById(R.id.rvBooks);

        setupRecyclerView();

    }

    private void setupRecyclerView() {

        adapter = new BookAdapter(this, bookList, isMyBook);
        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvBooks.setLayoutManager(layoutManager);
        rvBooks.setAdapter(adapter);


        //pagination options
        pagenumber = 1;


        if(isInternetAvailable()) {
            loadRecyclerViewData();
        }else {
            //Toast.makeText(SeeAllBooksActivity.this, getResources().getString(R.string.check_your_internet_connection), Toast.LENGTH_LONG).show();
        }


        //add the scrollListener to recycler view and after reaching the end of the list we update the
        // pagenumber inorder to get other set to data ie 11 to 20
        rvBooks.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if(oldScrollY < 0) {


                    visibleitemcount = layoutManager.getChildCount();
                    totalitemcount = layoutManager.getItemCount();
                    pastvisibleitems = ((GridLayoutManager)layoutManager).findFirstVisibleItemPosition();

                    //if loading is true which means there is data to be fetched from the database
                    if(loading) {
                        if((visibleitemcount + pastvisibleitems) >= totalitemcount) {
                            //swipeRefreshLayout.setRefreshing(true);
                            loading = false;
                            pagenumber += 1;
                            Log.e("pagetest","Page: "+pagenumber);
                            if(isInternetAvailable()) {
                                loadOtherRecyclerViewData(pagenumber);
                            }else {
                                //Toast.makeText(getContext(), getResources().getString(R.string.check_your_internet_connection), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        });



    }

    private void loadRecyclerViewData() {

        try {
            JSONArray books = result.getJSONArray("books");

            for(int i = 0;i < books.length();i++) {
                JSONObject j = books.getJSONObject(i);
                BookApiModel book = new BookApiModel(
                        j.getInt("id"),
                        j.getInt("page_count"),
                        j.getInt("year"),
                        j.getString("price"),
                        j.getInt("sound_count"),
                        j.getString("cover"),
                        j.getString("name")
                );

                bookList.add(book);
            }

            if(!bookList.isEmpty()) {
                loading = true;
                //swipeRefreshLayout.setRefreshing(false);
            } else {
                Toast.makeText(this, "No load more data", Toast.LENGTH_LONG).show();
            }

            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadOtherRecyclerViewData(int page) {

        RequestBody languageId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(langID));
        RequestBody device_type = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(2));
        RequestBody device_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(1232323));
        RequestBody key = RequestBody.create(MediaType.parse("text/plain"), query);
        RequestBody sound = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(1232));
        RequestBody latBody = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody lonBody = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody pageBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(page));

        minaInterface.postAskRequest(languageId,
                device_type, device_id, key,
                sound, latBody, lonBody, pageBody, "Bearer "+token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(response.isSuccessful()) {

                    try {
                        String s = response.body().string();

                        Log.e("kjkj", s);

                        JSONArray books
                                = new JSONObject(s)
                                .getJSONObject("success")
                                .getJSONObject("data")
                                .getJSONObject("result")
                                .getJSONArray("books");

                        for(int i = 0;i < books.length();i++) {
                            JSONObject j = books.getJSONObject(i);
                            BookApiModel book = new BookApiModel(
                                    j.getInt("id"),
                                    j.getInt("page_count"),
                                    j.getInt("year"),
                                    j.getString("price"),
                                    j.getInt("sound_count"),
                                    j.getString("cover"),
                                    j.getString("name")
                            );

                            bookList.add(book);
                        }

                        if(!bookList.isEmpty()) {
                            loading = true;
                            //swipeRefreshLayout.setRefreshing(false);
                        } else {
                            Toast.makeText(BooksActivity.this, "No load more data", Toast.LENGTH_LONG).show();
                        }

                        adapter.notifyDataSetChanged();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private boolean isInternetAvailable() {
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
