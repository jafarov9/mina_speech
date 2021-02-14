package com.hajma.apps.mina2.fragment;

import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.adapter.AlarmMusicAdapter;
import com.hajma.apps.mina2.retrofit.model.AlarmMusicModel;

import java.util.ArrayList;

public class AlarmMusicFragment extends Fragment {


    private ImageButton iButtonBackAlarmMusic;
    private RecyclerView rv_alarm_music;
    private ArrayList<AlarmMusicModel> musicModels;
    private AlarmMusicAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view
                = inflater.inflate(R.layout.alarm_music_fragment, container, false);

        rv_alarm_music = view.findViewById(R.id.rv_alarm_music);
        iButtonBackAlarmMusic = view.findViewById(R.id.iButtonBackAlarmMusic);

        listRingtone();

        return view;
    }

    public void listRingtone() {
        musicModels = new ArrayList<>();

        RingtoneManager manager = new RingtoneManager(getContext());
        manager.setType(RingtoneManager.TYPE_ALARM);
        // manager.setType(RingtoneManager.TYPE_RINGTONE);//For Get System Ringtone
        Cursor cursor = manager.getCursor();

        while (cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            Uri musicUri = manager.getRingtoneUri(cursor.getPosition());
            //String uri = manager.getRingtoneUri(cursor.getPosition()).toString();

            //String ringtoneName= cursor.getString(cursor.getColumnIndex("title"));
            musicModels.add(new AlarmMusicModel(title,musicUri));

            //Log.e("All Data", "getNotifications: "+ title+"-=---"+uri+"------"+ringtoneName);
            // Do something with the title and the URI of ringtone
        }

        loadRv(musicModels);
    }

    private void loadRv(ArrayList<AlarmMusicModel> musicModels) {

        adapter = new AlarmMusicAdapter(getContext(), musicModels);
        rv_alarm_music.setAdapter(adapter);
        rv_alarm_music.setLayoutManager(new LinearLayoutManager(getContext()));

    }


}
