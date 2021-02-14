package com.hajma.apps.mina2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.retrofit.model.NewsApiModel;
import com.hajma.apps.mina2.utils.PicassoCache;

import java.util.List;

public class NewsAdapter extends PagerAdapter {

    private List<NewsApiModel> newsList;
    private LayoutInflater layoutInflater;
    private Context context;
    private boolean isSound;

    public NewsAdapter(List<NewsApiModel> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_view_pager_news, container, false);

        ImageView imgNewsAudioPlay;
        TextView txtAudioNewsTitle;
        TextView txtAudioNewsCategory;
        LinearLayout lnrHorAudioNews;
        TextView txtNewsContent;

        imgNewsAudioPlay = view.findViewById(R.id.imgNewsAudioPlay);
        txtAudioNewsTitle = view.findViewById(R.id.txtAudioNewsTitle);
        txtAudioNewsCategory = view.findViewById(R.id.txtAudioNewsCategory);
        lnrHorAudioNews = view.findViewById(R.id.lnrHorAudioNews);
        txtNewsContent = view.findViewById(R.id.txtNewsContent);


        if(!newsList.get(position).getSound().equals("")) {
            isSound = true;
        }else {
            isSound = false;
        }

        if (isSound) {
            lnrHorAudioNews.setVisibility(View.VISIBLE);
        } else {
            lnrHorAudioNews.setVisibility(View.GONE);
        }


        txtAudioNewsTitle.setText(newsList.get(position).getTitle());
        txtNewsContent.setText(newsList.get(position).getContent());
        txtAudioNewsCategory.setText(newsList.get(position).getCategory_name());

        //load image from url
        PicassoCache.getPicassoInstance(context)
                .load(newsList.get(position).getCover()
                        .replace("http:", "https:"))
                .resize(300, 300)
                .into(imgNewsAudioPlay);

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
