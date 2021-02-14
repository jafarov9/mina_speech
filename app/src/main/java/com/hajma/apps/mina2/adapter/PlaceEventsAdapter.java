package com.hajma.apps.mina2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.activity.PlaceInsideActivity;

import java.util.ArrayList;

public class PlaceEventsAdapter extends RecyclerView.Adapter<PlaceEventsAdapter.PlaceEventsViewHolder>{

    private Context context;
    private ArrayList<Object> list;

    public PlaceEventsAdapter(Context context, ArrayList<Object> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PlaceEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_place_events, parent, false);
        return new PlaceEventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceEventsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    //place events view holder
    class PlaceEventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgPlaceEventCover;
        private TextView txtPlaceEventName;
        private TextView txtPlaceEventTags;
        private TextView txtPlaceEventWhere;
        private LinearLayout lnrPlaceEventsCard;


        public PlaceEventsViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPlaceEventCover = itemView.findViewById(R.id.imgPlaceEventCover);
            txtPlaceEventName = itemView.findViewById(R.id.txtPlaceEventName);
            txtPlaceEventTags = itemView.findViewById(R.id.txtPlaceEventTags);
            txtPlaceEventWhere = itemView.findViewById(R.id.txtPlaceEventWhere);
            lnrPlaceEventsCard = itemView.findViewById(R.id.lnrPlaceEventsCard);

            lnrPlaceEventsCard.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            context.startActivity(new Intent(context, PlaceInsideActivity.class));
        }
    }
}
