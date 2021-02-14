package com.hajma.apps.mina2.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.activity.TodayDuaActivity;
import com.hajma.apps.mina2.eventbus.DataEvent;
import com.hajma.apps.mina2.retrofit.model.DuaListApiModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class DuaListAdapter extends RecyclerView.Adapter<DuaListAdapter.DuaViewHolder> {

    private Context context;
    private ArrayList<DuaListApiModel> list;

    public DuaListAdapter(Context context, ArrayList<DuaListApiModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DuaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_dua_list, parent, false);

        return new DuaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DuaViewHolder holder, int position) {
        holder.txtDuaListTitle.setText(list.get(position).getTitle());
        holder.txtDuaListSubTitle.setText(list.get(position).getContent().substring(0, 12));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //dua view holder
    class DuaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout lnrDuaListCard;
        private ImageButton iButtonPlayDuaListCard;
        private TextView txtDuaListTitle;
        private TextView txtDuaListSubTitle;


        public DuaViewHolder(@NonNull View itemView) {
            super(itemView);

            iButtonPlayDuaListCard = itemView.findViewById(R.id.iButtonPlayDuaListCard);
            txtDuaListTitle = itemView.findViewById(R.id.txtDuaListTitle);
            txtDuaListSubTitle = itemView.findViewById(R.id.txtDuaListSubTitle);
            lnrDuaListCard = itemView.findViewById(R.id.lnrDuaListCard);
            lnrDuaListCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            DuaListApiModel dua = list.get(position);

            EventBus.getDefault().postSticky(new DataEvent.DuaModelClass(
                    1,
                    dua.getId(),
                    dua.getDay(),
                    dua.getTitle(),
                    dua.getContent()
            ));

            Intent intent = new Intent(context, TodayDuaActivity.class);
            context.startActivity(new Intent(context, TodayDuaActivity.class));
        }
    }
}
