package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.adapter.PlaceCategoryAdapter;
import com.hajma.apps.mina2.adapter.PlaceGalleryAdapter;
import com.hajma.apps.mina2.adapter.PlaceTagsAdapter;
import com.hajma.apps.mina2.utils.LocaleHelper;

public class PlaceInsideActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton iButtonBackPlaceInside;
    private ImageButton iButtonFavoritePlace;
    private ImageView imgCoverPlaceInside;
    private LinearLayout lnrPlaceWorkTime;
    private LinearLayout lnrPlaceCallNow;
    private LinearLayout lnrPlaceMap;

    private TextView txtAboutThePlace;
    private RecyclerView rvPlaceCategories;
    private RecyclerView rvPlaceTags;
    private RecyclerView rvPlaceGallery;

    private PlaceCategoryAdapter categoryAdapter;
    private PlaceTagsAdapter tagsAdapter;
    private PlaceGalleryAdapter galleryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_inside);

          iButtonBackPlaceInside = findViewById(R.id.iButtonBackPlaceInside);

          iButtonBackPlaceInside.setOnClickListener(this);

          iButtonFavoritePlace = findViewById(R.id.iButtonFavoritePlace);
          imgCoverPlaceInside = findViewById(R.id.imgCoverPlaceInside);
          lnrPlaceWorkTime = findViewById(R.id.lnrPlaceWorkTime);
          lnrPlaceCallNow = findViewById(R.id.lnrPlaceCallNow);
          lnrPlaceMap = findViewById(R.id.lnrPlaceMap);

          txtAboutThePlace = findViewById(R.id.txtAboutThePlace);
          rvPlaceCategories = findViewById(R.id.rvPlaceCategories);
          rvPlaceTags = findViewById(R.id.rvPlaceTags);
          rvPlaceGallery = findViewById(R.id.rvPlaceGallery);


          setupPlaceCategories();

          setupPlaceTags();

          setupPlaceGallery();

    }

    private void setupPlaceGallery() {

        rvPlaceGallery.setLayoutManager(new GridLayoutManager(this, 3));
        galleryAdapter = new PlaceGalleryAdapter(this, null);

        rvPlaceGallery.setAdapter(galleryAdapter);


    }

    private void setupPlaceTags() {

        rvPlaceTags.setLayoutManager(new GridLayoutManager(this, 2));
        tagsAdapter = new PlaceTagsAdapter(this, null);
        rvPlaceTags.setAdapter(tagsAdapter);

    }

    private void setupPlaceCategories() {

        rvPlaceCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new PlaceCategoryAdapter(this, null);
        rvPlaceCategories.setAdapter(categoryAdapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iButtonBackPlaceInside :
                onBackPressed();
                break;
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
