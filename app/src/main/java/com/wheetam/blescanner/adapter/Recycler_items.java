package com.wheetam.blescanner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wheetam.blescanner.R;
import com.wheetam.blescanner.model.BLEDevice;

import java.util.List;

public class Recycler_items extends RecyclerView.Adapter<Recycler_items.mViewHolder>{

    private String[] mDataset;
    private OnItemClickListener listener;

    public OnItemClickListener getListener() {
        return this.listener;

    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class mViewHolder extends RecyclerView.ViewHolder{
        public CardView mCardView;
        public TextView devName;
        public TextView devPacket;

        public mViewHolder(View v, OnItemClickListener listener){
            super(v);

            mCardView = (CardView)v.findViewById(R.id.cardview);
            devName = (TextView)v.findViewById(R.id.dev_name);
            devPacket = (TextView)v.findViewById(R.id.dev_packet);
        }
    }

    private List<BLEDevice> mDevices;
    private Context mContext;

    public Recycler_items(Context context, List<BLEDevice> devices){

        mDevices = devices;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    /**
     * 리스트 아이템의 xml layout을 view holder 로서 세팅
     *
     * @param parent
     * @param viewType
     * @return
     */
    public Recycler_items.mViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        // create a new view
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycler_items, parent, false);
        // set the view's size, margins, paddings and layout parameters
        mViewHolder vh = new mViewHolder(v, getListener());
        return vh;
    }

    @Override
    public void onBindViewHolder(Recycler_items.mViewHolder holder, final int position) {
        BLEDevice device = mDevices.get(position);
        Log.i("scan result","result : " + device.name);

        if (device != null) {
            TextView name = holder.devName;
            name.setText(device.name);
            TextView record = holder.devPacket;
            record.setText(device.scanRecord);
//            TextView rssi = holder.rssi;
//            rssi.setText(String.valueOf(device.rssi));
        }

//        holder.devName.setText(mDataset[position]);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentValue = "Device Name: " + mDevices.get(position).name +
                        "\tRSSI: " + mDevices.get(position).rssi +
                        "\tScanRecord: " + mDevices.get(position).scanRecord;
                Log.d("CardView", "CardView Clicked: " + currentValue);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

}
