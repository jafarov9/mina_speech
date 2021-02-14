package com.hajma.apps.mina2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.hajma.apps.mina2.R;

import java.util.ArrayList;

public class CalendarEventsAdapter extends RecyclerView.Adapter<CalendarEventsAdapter.CalendarEventViewHolder>{

    private Context context;
    private ArrayList<Object> list;

    public CalendarEventsAdapter(Context context, ArrayList<Object> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CalendarEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_calendar_events, parent, false);

        return new CalendarEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarEventViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    //calendar event view holder
    class CalendarEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgCalendarEvents;
        private TextView txtCalendarEventName;
        private AppCompatRatingBar ratingBarCalendarEvent;
        private TextView txtGoingCalendarEvent;
        private TextView txtLocationCalendarEvent;
        private LinearLayout lnrCalendarEvents;


        public CalendarEventViewHolder(@NonNull View itemView) {
            super(itemView);


            imgCalendarEvents = itemView.findViewById(R.id.imgCalendarEvents);
            txtCalendarEventName = itemView.findViewById(R.id.txtCalendarEventName);
            ratingBarCalendarEvent = itemView.findViewById(R.id.ratingBarCalendarEvent);
            txtGoingCalendarEvent = itemView.findViewById(R.id.txtGoingCalendarEvent);
            txtLocationCalendarEvent = itemView.findViewById(R.id.txtLocationCalendarEvent);
            lnrCalendarEvents = itemView.findViewById(R.id.lnrCalendarEvents);


            lnrCalendarEvents.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
