package com.hajma.apps.mina2.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class StaggeredListDecoration extends RecyclerView.ItemDecoration{

    public StaggeredListDecoration() {

    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).leftMargin = 5;
        ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).rightMargin = 5;
        ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).topMargin = 5;
        ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).bottomMargin= 5;
    }


}
