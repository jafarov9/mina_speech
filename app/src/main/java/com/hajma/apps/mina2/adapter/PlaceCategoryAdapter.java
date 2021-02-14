package com.hajma.apps.mina2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hajma.apps.mina2.R;

import java.util.ArrayList;

public class PlaceCategoryAdapter extends RecyclerView.Adapter<PlaceCategoryAdapter.PlaceCategoryViewHolder>{

    private Context context;
    private ArrayList<Object> list;

    public PlaceCategoryAdapter(Context context, ArrayList<Object> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PlaceCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_place_category, parent, false);
        return new PlaceCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceCategoryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    // place category view holder
    class PlaceCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout lnr_bg_cat_place;
        private ImageView imgPlaceCatIcon;
        private TextView txtPlaceCatName;
        private TextView txtPlaceCatDevize;
        private LinearLayout lnrCategoryForPlace;



        public PlaceCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            lnr_bg_cat_place = itemView.findViewById(R.id.lnr_bg_cat_place);
            imgPlaceCatIcon = itemView.findViewById(R.id.imgPlaceCatIcon);
            txtPlaceCatName = itemView.findViewById(R.id.txtPlaceCatName);
            txtPlaceCatDevize = itemView.findViewById(R.id.txtPlaceCatDevize);
            lnrCategoryForPlace = itemView.findViewById(R.id.lnrCategoryForPlace);

            lnrCategoryForPlace.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
