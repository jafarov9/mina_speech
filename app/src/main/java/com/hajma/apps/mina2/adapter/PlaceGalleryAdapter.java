package com.hajma.apps.mina2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hajma.apps.mina2.R;

import java.util.ArrayList;

public class PlaceGalleryAdapter extends RecyclerView.Adapter<PlaceGalleryViewHolder> {

    private Context context;
    private ArrayList<Object> list;

    public PlaceGalleryAdapter(Context context, ArrayList<Object> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PlaceGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_place_gallery, parent, false);

        return new PlaceGalleryViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PlaceGalleryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 9;
    }
}

//class place gallery view holder
class PlaceGalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private LinearLayout lnrPlaceGallery;
    private ImageView imgPlaceGalleryPhoto;


    public PlaceGalleryViewHolder(@NonNull View itemView) {
        super(itemView);

        lnrPlaceGallery = itemView.findViewById(R.id.lnrPlaceGallery);
        imgPlaceGalleryPhoto = itemView.findViewById(R.id.imgPlaceGalleryPhoto);

        lnrPlaceGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
