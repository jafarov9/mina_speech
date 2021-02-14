package com.hajma.apps.mina2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.activity.MyDetailedAudioBookActivity;
import com.hajma.apps.mina2.retrofit.model.SoundListApiModel;

import java.util.ArrayList;

public class MySoundListAdapter extends RecyclerView.Adapter<MySoundListAdapter.SoundListCardViewHolder>{

    private ArrayList<SoundListApiModel> soundList;
    private Context context;

    public MySoundListAdapter(ArrayList<SoundListApiModel> soundList, Context context) {
        this.soundList = soundList;
        this.context = context;
    }

    @NonNull
    @Override
    public SoundListCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_my_audio_book, parent, false);

        return new SoundListCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundListCardViewHolder holder, int position) {
        holder.txtMyBookPlaylistName.setText(soundList.get(position).getTitle());
        holder.txtMyBookPlaylistChapter.setText(soundList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return soundList.size();
    }

    //sound list card view holder
    class SoundListCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageButton iButtonMyBookPlaylist;
        private TextView txtMyBookPlaylistName;
        private TextView txtMyBookPlaylistChapter;
        private LinearLayout lnrMyAudioBooksCard;


        public SoundListCardViewHolder(@NonNull View itemView) {
            super(itemView);

            iButtonMyBookPlaylist = itemView.findViewById(R.id.iButtonMyBookPlaylist);
            txtMyBookPlaylistName = itemView.findViewById(R.id.txtMyBookPlaylistName);
            txtMyBookPlaylistChapter = itemView.findViewById(R.id.txtMyBookPlaylistChapter);
            lnrMyAudioBooksCard = itemView.findViewById(R.id.lnrMyAudioBooksCard);
            lnrMyAudioBooksCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int positon = getAdapterPosition();
            String sound = soundList.get(positon).getSound();
            ((MyDetailedAudioBookActivity)context).prepareMediaPlayer(sound);
        }
    }
 }
