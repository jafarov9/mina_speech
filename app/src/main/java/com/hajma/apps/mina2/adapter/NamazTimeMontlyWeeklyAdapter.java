package com.hajma.apps.mina2.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.retrofit.model.NamazTimeWeeklyModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NamazTimeMontlyWeeklyAdapter extends RecyclerView.Adapter<NamazTimeMontlyWeeklyAdapter.NamazTimeWeeklyViewHolder>{

    private Context context;
    private ArrayList<NamazTimeWeeklyModel> list;

    public NamazTimeMontlyWeeklyAdapter(Context context, ArrayList<NamazTimeWeeklyModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NamazTimeWeeklyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_for_weekly_namaz, parent, false);

        return new NamazTimeWeeklyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NamazTimeWeeklyViewHolder holder, int position) {

        holder.dateWeeklyNamaz.setText(changeFormat(list.get(position).getDate()));
        holder.subhWeeklyNamaz.setText(list.get(position).getSubh());
        holder.zohrWeeklyNamaz.setText(list.get(position).getZohr());
        holder.esrWeeklyNamaz.setText(list.get(position).getEsr());
        holder.shamWeeklyNamaz.setText(list.get(position).getSham());
        holder.xiftenWeeklyNamaz.setText(list.get(position).getXiften());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //view holder
    class NamazTimeWeeklyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout weekly_namaz;
        private TextView dateWeeklyNamaz, subhWeeklyNamaz,
                zohrWeeklyNamaz, esrWeeklyNamaz,
                shamWeeklyNamaz, xiftenWeeklyNamaz;





        public NamazTimeWeeklyViewHolder(@NonNull View itemView) {
            super(itemView);

            weekly_namaz = itemView.findViewById(R.id.weekly_namaz);
            dateWeeklyNamaz = itemView.findViewById(R.id.dateWeeklyNamaz);
            subhWeeklyNamaz = itemView.findViewById(R.id.subhWeeklyNamaz);
            zohrWeeklyNamaz = itemView.findViewById(R.id.zohrWeeklyNamaz);
            esrWeeklyNamaz = itemView.findViewById(R.id.esrWeeklyNamaz);
            shamWeeklyNamaz = itemView.findViewById(R.id.shamWeeklyNamaz);
            xiftenWeeklyNamaz = itemView.findViewById(R.id.xiftenWeeklyNamaz);


        }
    }

    //change date format method
    String changeFormat(String time) {
        String format = "yyyy-MM-dd";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = null;
        try {
            d = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern("dd.MM");
        return sdf.format(d);
    }

}
