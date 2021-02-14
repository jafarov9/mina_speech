package com.hajma.apps.mina2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.retrofit.model.Alarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>{

    private ArrayList<Alarm> alarms;
    private Context context;

    private OnItemClickListener listener;
    private OnCheckedChangeListener changeListener;



    public AlarmAdapter(ArrayList<Alarm> alarms, Context context) {
        this.alarms = alarms;
        this.context = context;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_for_alarm, parent, false);

        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {

        holder.tvAlarmTimeTitle.setText(alarms.get(position).getTime());
        holder.tvDisabledAlarmTime.setText(alarms.get(position).getTime());
        List<String> temp = alarms.get(position).getRepeat();
        StringBuilder repeatString = new StringBuilder();

        for(int i = 0;i < temp.size();i++) {
            if(i == temp.size() - 1) {
                repeatString.append(temp.get(i)).append(".");
            }else {
                repeatString.append(temp.get(i)).append(", ");
            }
        }

        holder.tvSalutation.setText(repeatString);
        holder.swEnable.setChecked(alarms.get(position).isStatus());
        holder.linearMain.setOnClickListener(v -> {
            if(holder.linearAlarm.getVisibility() == View.VISIBLE){
                holder.linearAlarm.setVisibility(View.GONE);
            }else {
                holder.linearAlarm.setVisibility(View.VISIBLE);
            }
        });

        holder.ivDelete.setOnClickListener(v -> {
            if(listener != null){
                listener.onItemClick(holder.ivDelete, position);
            }
        });

        holder.swEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            changeListener.onCheckedChanged(holder.swEnable, position);
        });


    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    //alarm view holder
    class AlarmViewHolder extends RecyclerView.ViewHolder {

        private TextView tvAlarmTimeTitle;
        private TextView tvSalutation;
        private ImageView ivDelete;
        private ImageView ivEdit;
        private TextView tvDisabledAlarmTime;
        private Switch swEnable;
        private LinearLayout linearMain;
        private LinearLayout linearAlarm;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);

            //init variables
            tvAlarmTimeTitle = itemView.findViewById(R.id.tvAlarmTimeTitle);
            tvSalutation = itemView.findViewById(R.id.tvSalutation);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            linearMain = itemView.findViewById(R.id.linearMain);
            linearAlarm = itemView.findViewById(R.id.linearAlarm);
            tvDisabledAlarmTime = itemView.findViewById(R.id.tvDisabledAlarmTime);
            swEnable = itemView.findViewById(R.id.swEnable);

        }



    }

    public interface OnItemClickListener {
        void onItemClick(ImageView imageView, int position );
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(Switch sw, int position);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener){
        this.changeListener = listener;
    }

    public Alarm getAlarmAt(int position){
        return alarms.get(position);
    }



}
