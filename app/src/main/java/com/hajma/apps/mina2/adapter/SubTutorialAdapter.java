package com.hajma.apps.mina2.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.activity.MainActivity;


import java.util.ArrayList;

public class SubTutorialAdapter extends RecyclerView.Adapter<SubTutorialAdapter.SubTutorialViewHolder>{

    private Context context;
    private ArrayList<String> subTutorialList;
    private int drawableID;

    public SubTutorialAdapter(Context context, ArrayList<String> subTutorialList, int drawableID) {
        this.context = context;
        this.subTutorialList = subTutorialList;
        this.drawableID = drawableID;
    }

    @NonNull
    @Override
    public SubTutorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_sub_tutorial, parent, false);

        return new SubTutorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubTutorialViewHolder holder, int position) {
        holder.imgSubTutorial.setImageResource(drawableID);
        holder.txtSubTutorialKey.setText(subTutorialList.get(position));
    }

    @Override
    public int getItemCount() {
        return subTutorialList.size();
    }

    //sub tutorial view holder
    class SubTutorialViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout lnrSubTutorial;
        private ImageView imgSubTutorial;
        private TextView txtSubTutorialKey;


        public SubTutorialViewHolder(@NonNull View itemView) {
            super(itemView);

            lnrSubTutorial = itemView.findViewById(R.id.lnrSubTutorial);
            imgSubTutorial = itemView.findViewById(R.id.imgSubTutorial);
            txtSubTutorialKey = itemView.findViewById(R.id.txtSubTutorialKey);
            lnrSubTutorial.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int positon = getAdapterPosition();
            String key = subTutorialList.get(positon);


            /*EventBus.getDefault().postSticky(new DataEvent.SubTutorialEvent(
                    key,
                    1
            ));*/

            Intent intent = new Intent(context, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("tutorialKey", key);
            context.startActivity(intent);
        }
    }
}
