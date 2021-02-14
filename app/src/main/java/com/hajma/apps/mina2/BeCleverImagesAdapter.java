package com.hajma.apps.mina2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hajma.apps.mina2.utils.PicassoCache;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BeCleverImagesAdapter extends RecyclerView.Adapter<BeCleverImagesAdapter.BeCleverImageHolder>{


    private Context context;
    private ArrayList<String> imageList;

    public BeCleverImagesAdapter(Context context, ArrayList<String> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public BeCleverImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_be_clever_images, parent, false);

        return new BeCleverImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeCleverImageHolder holder, int position) {
        Picasso.get()
                .load(imageList.get(position).replace("http:", "https:"))
                //.resize(600, 600)
                .into(holder.imgCardImages);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }



    class BeCleverImageHolder extends RecyclerView.ViewHolder {

        private ImageView imgCardImages;

        public BeCleverImageHolder(@NonNull View itemView) {
            super(itemView);

            imgCardImages = itemView.findViewById(R.id.imgCardImages);
        }
    }
}
