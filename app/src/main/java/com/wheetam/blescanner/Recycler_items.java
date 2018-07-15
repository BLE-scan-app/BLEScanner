package com.wheetam.blescanner;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Recycler_items extends RecyclerView.Adapter<Recycler_items.mViewHolder>{

    private String[] mDataset;

    public static class mViewHolder extends RecyclerView.ViewHolder{
        public CardView mCardView;
        public TextView devName;
        public TextView devPacket;

        public mViewHolder(View v){
            super(v);

            mCardView = (CardView)v.findViewById(R.id.cardview);
            devName = (TextView)v.findViewById(R.id.dev_name);
            devPacket = (TextView)v.findViewById(R.id.dev_packet);
        }
    }

    public Recycler_items(String[] myDataset){
        mDataset = myDataset;
    }

    /**
     * 리스트 아이템의 xml layout을 view holder 로서 세팅
     *
     * @param parent
     * @param viewType
     * @return
     */
    public Recycler_items.mViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_items, parent, false);
        // set the view's size, margins, paddings and layout parameters
        mViewHolder vh = new mViewHolder(v);
        return vh;
    }


    /**
     *
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, final int position) {
        holder.devName.setText(mDataset[position]);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentValue = mDataset[position];
                Log.d("CardView", "CardView Clicked: " + currentValue);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
