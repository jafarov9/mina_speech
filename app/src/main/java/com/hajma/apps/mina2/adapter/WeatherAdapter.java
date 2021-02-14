package com.hajma.apps.mina2.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.retrofit.model.WeatherListModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private Context context;
    private ArrayList<WeatherListModel> list;
    private boolean isHourlyWeather;
    private String[] days = new String[] { "Sunday", "Monday", "Tuesday",
                                "Wednesday", "Thursday",
                                "Friday", "Saturday" };


    public void setHourlyWeather(boolean hourlyWeather) {
        isHourlyWeather = hourlyWeather;
    }

    public WeatherAdapter(Context context, ArrayList<WeatherListModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_weather, parent, false);

        return new WeatherViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {

        weatherImage(list.get(position).getIcon(), context, holder.imgWeatherCoverItem);
        holder.txtWeatherItemTemp.setText((int) list.get(position).getTemp() + "\u2103");

        if(isHourlyWeather) {
            //String dt_txt = list.get(position).getDate();
            //String tempDate = dt_txt.substring(dt_txt.length() - 8, dt_txt.length() - 3);
            //holder.txtWeatherItemDate.setText(tempDate);
            holder.txtWeatherItemDate.setText(changeFormatOther(list.get(position).getDateLong()));
        }else {
            holder.txtWeatherItemDate.setText(changeFormatWeek(list.get(position).getDateLong()));
        }
    }

    @Override
    public int getItemCount() {
        if(list.size() > 0) {
            return isHourlyWeather ? 8 : list.size();
        }else {
            return list.size();
        }
    }

    //Weather view holder
    class WeatherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtWeatherItemDate;
        private TextView txtWeatherItemTemp;
        private ImageView imgWeatherCoverItem;
        private CardView cardWeatherItem;


        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            txtWeatherItemDate = itemView.findViewById(R.id.txtWeatherItemDate);
            txtWeatherItemTemp = itemView.findViewById(R.id.txtWeatherItemTemp);
            imgWeatherCoverItem = itemView.findViewById(R.id.imgWeatherCoverItem);
            cardWeatherItem = itemView.findViewById(R.id.cardWeatherItem);

            cardWeatherItem.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

        }
    }

    //set weather image
    private void weatherImage(String data, Context context, ImageView imageView){
        Glide.with(context)
                .load("file:///android_asset/" + data +".png")
                .into(imageView);
    }

    private String changeFormatWeek(long time) {
        Date date = new Date(time * 1000);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());


        Log.e("uyuy", sdf.format(date));

        return sdf.format(date);
    }

    public String changeFormatOther(long time) {
        Date date = new Date(time * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("H a", Locale.getDefault());
        return sdf.format(date);
    }


}
