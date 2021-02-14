package com.hajma.apps.mina2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hajma.apps.mina2.R;

import java.util.ArrayList;

public class PlaceTagsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_SHORT_TAG = 1;
    private static final int VIEW_TYPE_LONG_TAG = 2;

    private Context context;
    private ArrayList<Object> list;

    public PlaceTagsAdapter(Context context, ArrayList<Object> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if(viewType == VIEW_TYPE_LONG_TAG) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_cat_tag_long, parent, false);
            return new PlaceLongTagViewHolder(view);
        }else if(viewType == VIEW_TYPE_SHORT_TAG) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_cat_tag_short, parent, false);
            return new PlaceShortTagViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 12;
    }

    @Override
    public int getItemViewType(int position) {
        if(position % 2 != 0) {
            return VIEW_TYPE_LONG_TAG;
        }else {
            return VIEW_TYPE_SHORT_TAG;
        }
    }
}

//place Short tags viewholder
class PlaceShortTagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private LinearLayout lnrPlaceCatShortTags;
    private LinearLayout lnrPlaceTagShortBg;
    private TextView txtShortTagName;



    public PlaceShortTagViewHolder(@NonNull View itemView) {
        super(itemView);

        lnrPlaceCatShortTags = itemView.findViewById(R.id.lnrPlaceCatShortTags);
        lnrPlaceTagShortBg = itemView.findViewById(R.id.lnrPlaceTagShortBg);
        txtShortTagName = itemView.findViewById(R.id.txtShortTagName);


        lnrPlaceCatShortTags.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}

//place Long tags viewholder
class PlaceLongTagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private LinearLayout lnrPlaceCatLongTags;
    private LinearLayout lnrPlaceTagBg;
    private TextView txtLongTagName;



    public PlaceLongTagViewHolder(@NonNull View itemView) {
        super(itemView);

        lnrPlaceCatLongTags = itemView.findViewById(R.id.lnrPlaceCatLongTags);
        lnrPlaceTagBg = itemView.findViewById(R.id.lnrPlaceTagBg);
        txtLongTagName = itemView.findViewById(R.id.txtLongTagName);


        lnrPlaceCatLongTags.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
