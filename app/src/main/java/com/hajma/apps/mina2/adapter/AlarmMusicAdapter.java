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

import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.eventbus.DataEvent;
import com.hajma.apps.mina2.retrofit.model.AlarmMusicModel;
import com.hajma.apps.mina2.utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class AlarmMusicAdapter extends RecyclerView.Adapter<AlarmMusicAdapter.ViewHolder>{

    private Context context;
    private ArrayList<AlarmMusicModel> musicModels;
    private int selectedPosition = -1;
    private Util util = new Util();

    public AlarmMusicAdapter(Context context, ArrayList<AlarmMusicModel> musicModels) {
        this.context = context;
        this.musicModels = musicModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_for_alarm_music,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv.setText(musicModels.get(position).getTitle());

        holder.ll.setTag(position);

        holder.ll.setOnClickListener(v -> {
            itemCheckChanged(v);
            util.setSharedPreference(context, C.ALARMMUSIC,musicModels.get(position).getTitle() + "#" + musicModels.get(position).getUri());
            EventBus.getDefault().postSticky(new DataEvent.ChangeAlarmMusicText(1, musicModels.get(position).getTitle()));
        });

        if(selectedPosition == position) {
            holder.iv.setVisibility(View.VISIBLE);
        }else {
            holder.iv.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return musicModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView iv;
        LinearLayout ll;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_alarm_music_text);
            iv = itemView.findViewById(R.id.iv_alarm_music_check);
            ll = itemView.findViewById(R.id.ll_alarm_music);
        }
    }

    private void itemCheckChanged(View v){
        selectedPosition = (Integer) v.getTag();
        notifyDataSetChanged();
    }


}
