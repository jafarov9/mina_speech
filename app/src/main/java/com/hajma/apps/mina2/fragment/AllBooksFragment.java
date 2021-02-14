package com.hajma.apps.mina2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class AllBooksFragment extends Fragment {

    private RecyclerView rvAllBooks;
    private MinaInterface minaInterface;
    private JSONObject result;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_books, container, false);

        try {
            result = new JSONObject(Objects.requireNonNull(getArguments().getString("result")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        minaInterface = ApiUtils.getMinaInterface();


        rvAllBooks = view.findViewById(R.id.rvAllBooks);


        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {




    }
}
