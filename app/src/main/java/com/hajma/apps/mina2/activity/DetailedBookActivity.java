package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;
import com.hajma.apps.mina2.retrofit.model.AuthorApiModel;
import com.hajma.apps.mina2.retrofit.model.CategoryApiModel;
import com.hajma.apps.mina2.retrofit.model.DetailedBookApiModel;
import com.hajma.apps.mina2.utils.LocaleHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailedBookActivity extends AppCompatActivity {

    private LottieAnimationView lottiBackDetailedBooks;
    private ImageView imgDtBookPicture;
    private TextView txtDtBookName;
    private TextView txtDetailedBookAuthor;
    private Button btnDetailedBookBuy;
    private ProgressBar pbLoadingProcess;
    private TextView txtDetailedPublisherDescription;
    private TextView txtDetailedGenre, txtDetailedReleased,
            txtDetailedBookLength, txtDetailedBookSeller, txtDetailedBookLanguage;
    private int bookId;
    private String token = "";
    private MinaInterface minaInterface;
    private SharedPreferences sharedPreferences;
    private DetailedBookApiModel dtBook;
    private ArrayList<CategoryApiModel> bookCategories = new ArrayList<>();
    private ArrayList<AuthorApiModel> bookAuthors = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_book);

        minaInterface = ApiUtils.getMinaInterface();

        if(getIntent() != null) {
            bookId = getIntent().getIntExtra("bookId", 0);
        }

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");


        //init views
        lottiBackDetailedBooks = findViewById(R.id.lottiBackDetailedBooks);
        imgDtBookPicture = findViewById(R.id.imgDtBookPicture);
        txtDtBookName = findViewById(R.id.txtDtBookName);
        txtDetailedBookAuthor = findViewById(R.id.txtDetailedBookAuthor);
        btnDetailedBookBuy = findViewById(R.id.btnDetailedBookBuy);
        pbLoadingProcess = findViewById(R.id.pbLoadingProcess);
        txtDetailedPublisherDescription = findViewById(R.id.txtDetailedPublisherDescription);
        txtDetailedGenre = findViewById(R.id.txtDetailedGenre);
        txtDetailedReleased = findViewById(R.id.txtDetailedReleased);
        txtDetailedBookLength = findViewById(R.id.txtDetailedBookLength);
        txtDetailedBookSeller = findViewById(R.id.txtDetailedBookSeller);
        txtDetailedBookLanguage = findViewById(R.id.txtDetailedBookLanguage);


        pbLoadingProcess.setIndeterminate(true);

        lottiBackDetailedBooks.setOnClickListener(v -> onBackPressed());

        btnDetailedBookBuy.setOnClickListener(v -> {
            Intent intent = null;


            try {
                intent = new Intent("com.hajma.apps.hajmabooks.action");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("bookIdSplash", bookId);
                intent.putExtra("keySplash", "dtFrag");
                startActivity(intent);
            } catch (ActivityNotFoundException ingored) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/details?id=" + "com.hajma.apps.hajmabooks")));
            }
        });


        loadDetailedBook(1, bookId);

    }


    private void loadDetailedBook(int langId, int bookId) {


        String langID = String.valueOf(langId);
        String bookID = String.valueOf(bookId);

        minaInterface.getBookDetailed(langID, bookID, "Bearer "+token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()) {
                    try {
                        String s = response.body().string();

                        JSONObject data = new JSONObject(s).getJSONObject("success").getJSONObject("data");
                        dtBook = new DetailedBookApiModel();
                        dtBook.setId(data.getInt("id"));
                        dtBook.setImage(data.getString("image"));
                        dtBook.setName(data.getString("name"));
                        dtBook.setContent(data.getString("content"));
                        dtBook.setEpub(data.getString("epub"));
                        dtBook.setPage_count(data.getInt("page_count"));
                        dtBook.setHas_sound(data.getBoolean("has_sound"));
                        dtBook.setPrice(data.getString("price"));
                        dtBook.setSeller(data.getString("seller"));
                        dtBook.setYear(data.getInt("year"));
                        dtBook.setBook_language(data.getString("book_language"));

                        //get book authors
                        JSONArray authors = data.getJSONArray("authors");
                        for(int i = 0;i < authors.length();i++) {

                            JSONObject a = authors.getJSONObject(i);
                            AuthorApiModel author = new AuthorApiModel();
                            author.setId(a.getInt("id"));
                            author.setName(a.getString("name"));

                            bookAuthors.add(author);
                        }


                        //get book categories
                        JSONArray categories = data.getJSONArray("categries");
                        for(int i = 0;i < categories.length();i++) {

                            JSONObject c = categories.getJSONObject(i);
                            CategoryApiModel category = new CategoryApiModel();
                            category.setId(c.getInt("id"));
                            category.setName(c.getString("name"));

                            bookCategories.add(category);
                        }

                        loadViews();

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

    private void loadViews() {

        if(dtBook != null) {
            Picasso.get()
                    .load(dtBook.getImage()
                            .replace("http:", "https:"))
                    .into(imgDtBookPicture);

            txtDtBookName.setText(dtBook.getName());
            StringBuilder str = new StringBuilder();
            for(int i = 0;i < bookAuthors.size(); i++) {
                str.append(bookAuthors.get(i).getName() + "\n");
            }

            StringBuilder str2 = new StringBuilder();
            for(int i = 0;i < bookCategories.size(); i++) {
                str2.append(bookCategories.get(i).getName() + "\n");
            }

            txtDetailedGenre.setText(str2.toString());
            txtDetailedBookAuthor.setText(txtDetailedBookAuthor.getText() + str.toString());
            txtDetailedBookLanguage.setText(dtBook.getBook_language());
            txtDetailedBookSeller.setText(dtBook.getSeller());
            txtDetailedBookLength.setText(""+dtBook.getPage_count());
            txtDetailedPublisherDescription.setText(dtBook.getContent());
            txtDetailedReleased.setText(""+dtBook.getYear());

            if(!dtBook.getEpub().equals("") || dtBook.getPrice().equals("0.00")) {

                if(dtBook.isHas_sound()) {
                    btnDetailedBookBuy.setText(getResources().getString(R.string._listen));
                    btnDetailedBookBuy.setBackgroundResource(R.drawable.btn_free_background);
                } else {

                    btnDetailedBookBuy.setText(getResources().getString(R.string._free));
                    btnDetailedBookBuy.setBackgroundResource(R.drawable.btn_free_background);

                }

            }else {
                btnDetailedBookBuy.setText(btnDetailedBookBuy.getText() + dtBook.getPrice());
            }

            txtDetailedBookLanguage.setText(dtBook.getBook_language());

            pbLoadingProcess.setIndeterminate(false);
            pbLoadingProcess.setVisibility(View.GONE);
            btnDetailedBookBuy.setVisibility(View.VISIBLE);

        }


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

}
