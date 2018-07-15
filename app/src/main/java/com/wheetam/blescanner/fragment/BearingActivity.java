package com.wheetam.blescanner.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wheetam.blescanner.R;

public class BearingActivity extends Fragment{

    private final String TXT_POLAR = "POlar ";

    public static TextView tv_north_title;
    public static TextView tv_north_rssi;
    public static TextView tv_north_tx;

    private static TextView tv_south_title;
    private static TextView tv_south_rssi;
    private static TextView tv_south_tx;

    private static TextView tv_east_title;
    private static TextView tv_east_rssi;
    private static TextView tv_east_tx;

    private static TextView tv_west_title;
    private static TextView tv_west_rssi;
    private static TextView tv_west_tx;

    private static final int IDX_RSSI = 2; // 2,3
    private static final int IDX_TX = 16; // 16,17

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_directions,container,false);

        // North
        tv_north_title = (TextView)rootView.findViewById(R.id.north);
        tv_north_rssi = (TextView)rootView.findViewById(R.id.north_rssi);
        tv_north_tx = (TextView)rootView.findViewById(R.id.north_tx);

        // South
        tv_south_title = (TextView)rootView.findViewById(R.id.south);
        tv_south_rssi = (TextView)rootView.findViewById(R.id.south_rssi);
        tv_south_tx = (TextView)rootView.findViewById(R.id.south_tx);

        // East
        tv_east_title = (TextView)rootView.findViewById(R.id.east);
        tv_east_rssi = (TextView)rootView.findViewById(R.id.east_rssi);
        tv_east_tx = (TextView)rootView.findViewById(R.id.east_tx);

        // West
        tv_west_title = (TextView)rootView.findViewById(R.id.west);
        tv_west_rssi = (TextView)rootView.findViewById(R.id.west_rssi);
        tv_west_tx = (TextView)rootView.findViewById(R.id.west_tx);

        return rootView;
    }

//    public static void setTitle(String title){
//        tv_north_title.setText(title);
//    }
//
//    /**
//     *
//     * @param data : ANT, RSSI, X1, Y1, Z1, X2, Y2, Z2, TX 로 구성
//     */
//    public static void setText(String data){
//        tv_north_rssi.setText(data.charAt(IDX_RSSI)+data.charAt(IDX_RSSI+1));
//        tv_north_tx.setText(data.charAt(IDX_TX)+data.charAt(IDX_TX+1));
//    }
}
