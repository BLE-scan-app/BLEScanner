package com.wheetam.blescanner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecyclerActivity extends Fragment {
    public RecyclerActivity(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_devices, container, false);

        RecyclerView rv = (RecyclerView)rootView.findViewById(R.id.rv_view);
        rv.setHasFixedSize(true);
        Recycler_items adapter = new Recycler_items(new String[]{"test 1", "test 2", "test 3","test 4", "test 5", "test 6"});
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;

//        return super.onCreateView(inflater, container, savedInstanceState);


    }


}
