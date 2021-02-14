package com.hajma.apps.mina2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.activity.MainActivity;

import java.util.ArrayList;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.SuggestViewHolder> {

    private ArrayList<String> keyList;
    private Context context;
    private String[] colorList = {
            "#2895FF", "#393F44", "#AA3434",
            "#E91E63", "#FFA000", "#4CAF50",
            "#7B1FA2", "#795548", "#455A64",
            "#FF5722"};


    public SuggestionsAdapter(ArrayList<String> keyList, Context context) {
        this.keyList = keyList;
        this.context = context;
    }

    @NonNull
    @Override
    public SuggestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggest_test,parent,false);
        return new SuggestViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SuggestViewHolder holder, int position) {

        holder.tv.setText(keyList.get(position));

        if(position == 0){
            holder.tv.setBackground(context.getDrawable(R.drawable.bg_view_for_suggest));
        }else if(position%2 == 0){
            holder.tv.setBackground(context.getDrawable(R.drawable.bg_for_login_button));
        }else if(position%3 == 0){
            holder.tv.setBackground(context.getDrawable(R.drawable.background_for_tutorial));
        }else {
            holder.tv.setBackground(context.getDrawable(R.drawable.bg_gradient_for_dua_play));
        }

    }

    @Override
    public int getItemCount() {
        return keyList.size();
    }


    //view holder test
    public class SuggestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv;
        private LinearLayout lnrSuggestCard;

        public SuggestViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.suggest_text);
            lnrSuggestCard = itemView.findViewById(R.id.lnrSuggestCard);
            lnrSuggestCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            String key = keyList.get(position);


            ((MainActivity)context).sendApiRequest(key);
        }
    }


}