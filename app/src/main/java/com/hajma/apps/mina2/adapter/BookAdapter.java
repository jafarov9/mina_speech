package com.hajma.apps.mina2.adapter;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.activity.DetailedBookActivity;
import com.hajma.apps.mina2.activity.MyDetailedAudioBookActivity;
import com.hajma.apps.mina2.retrofit.model.BookApiModel;
import com.hajma.apps.mina2.utils.PicassoCache;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder>{

    private Context context;
    private ArrayList<BookApiModel> bookList;
    private boolean isMyBook = false;

    public BookAdapter(Context context, ArrayList<BookApiModel> bookList, boolean isMyBook) {
        this.context = context;
        this.bookList = bookList;
        this.isMyBook = isMyBook;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_see_all_books, parent, false);

        return new BookViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        boolean isFree = bookList.get(position).getPrice().equals("0.00");

        holder.txtSeeAllBooksName.setText(bookList.get(position).getName());


        if(isMyBook) {

            if(bookList.get(position).getSound_count() > 0) {
                holder.btnPriceSeeAllBooks.setBackgroundResource(R.drawable.btn_read_background);
                holder.btnPriceSeeAllBooks.setTextColor(Color.parseColor("#FFFFFF"));
                holder.btnPriceSeeAllBooks.setText(context.getResources().getString(R.string._listen));
            }else {
                holder.btnPriceSeeAllBooks.setBackgroundResource(R.drawable.btn_read_background);
                holder.btnPriceSeeAllBooks.setTextColor(Color.parseColor("#FFFFFF"));
                holder.btnPriceSeeAllBooks.setText(context.getResources().getString(R.string._read));
            }
        }else if (!isFree) {
            holder.btnPriceSeeAllBooks.setBackgroundResource(R.drawable.btn_price_background);
            holder.btnPriceSeeAllBooks.setTextColor(Color.parseColor("#2C6DE8"));
            holder.btnPriceSeeAllBooks.setText("$"+bookList.get(position).getPrice());
        }else {

            if(bookList.get(position).getSound_count() > 0) {
                holder.btnPriceSeeAllBooks.setBackgroundResource(R.drawable.btn_read_background);
                holder.btnPriceSeeAllBooks.setTextColor(Color.parseColor("#FFFFFF"));
                holder.btnPriceSeeAllBooks.setText(context.getResources().getString(R.string._listen));
            } else {
                holder.btnPriceSeeAllBooks.setBackgroundResource(R.drawable.btn_free_background);
                holder.btnPriceSeeAllBooks.setTextColor(Color.parseColor("#FFFFFF"));
                holder.btnPriceSeeAllBooks.setText(context.getResources().getString(R.string._free));
            }
        }

        PicassoCache.getPicassoInstance(context)
                .load(bookList.get(position).getCover().replace("http:", "https:"))
                .into(holder.imgSeeAllBooks);



    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    //book view holder
    class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgSeeAllBooks;
        private TextView txtSeeAllBooksName;
        private Button btnPriceSeeAllBooks;
        private CardView cardSeeAll;


        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSeeAllBooks = itemView.findViewById(R.id.imgSeeAllBooks);
            txtSeeAllBooksName = itemView.findViewById(R.id.txtSeeAllBooksName);
            btnPriceSeeAllBooks = itemView.findViewById(R.id.btnPriceSeeAllBooks);
            cardSeeAll = itemView.findViewById(R.id.cardSeeAll);
            cardSeeAll.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            int bookId = bookList.get(position).getId();
            int soundCount = bookList.get(position).getSound_count();
            String price = bookList.get(position).getPrice();



            if(isMyBook) {
                //book is my
                Intent intent = new Intent(context, MyDetailedAudioBookActivity.class);
                intent.putExtra("bookId", bookId);
                context.startActivity(intent);
            }else {
                //if book is audio and free
                if(soundCount > 0 && price.equals("0.00")) {

                    Intent intent = new Intent(context, MyDetailedAudioBookActivity.class);
                    intent.putExtra("bookId", bookId);
                    context.startActivity(intent);

                }else {
                    Intent intent = new Intent(context, DetailedBookActivity.class);
                    intent.putExtra("bookId", bookId);
                    context.startActivity(intent);
                }
            }

        }
    }
}
