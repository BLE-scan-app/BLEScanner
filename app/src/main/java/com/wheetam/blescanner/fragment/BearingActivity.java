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

    private final String TXT_POLAR = "POlar";

    public static TextView tv_north_title;
    public static TextView tv_north_info;
    public static TextView tv_north_rssi;
    public static TextView tv_north_tx;

    public static TextView tv_south_title;
    public static TextView tv_south_info;
    public static TextView tv_south_rssi;
    public static TextView tv_south_tx;

    public static TextView tv_east_title;
    public static TextView tv_east_info;
    public static TextView tv_east_rssi;
    public static TextView tv_east_tx;

    public static TextView tv_west_title;
    public static TextView tv_west_info;
    public static TextView tv_west_rssi;
    public static TextView tv_west_tx;

    public static TextView tv_center_title;
    public static TextView tv_center_info;
    public static TextView tv_center_rssi;
    public static TextView tv_center_tx;

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
        tv_north_info = (TextView)rootView.findViewById(R.id.north_info);
//        tv_north_rssi = (TextView)rootView.findViewById(R.id.north_rssi);
//        tv_north_tx = (TextView)rootView.findViewById(R.id.north_tx);

        // South
        tv_south_title = (TextView)rootView.findViewById(R.id.south);
        tv_south_info = (TextView)rootView.findViewById(R.id.south_info);
//        tv_south_rssi = (TextView)rootView.findViewById(R.id.south_rssi);
//        tv_south_tx = (TextView)rootView.findViewById(R.id.south_tx);

        // East
        tv_east_title = (TextView)rootView.findViewById(R.id.east);
        tv_east_info = (TextView)rootView.findViewById(R.id.east_info);
//        tv_east_rssi = (TextView)rootView.findViewById(R.id.east_rssi);
//        tv_east_tx = (TextView)rootView.findViewById(R.id.east_tx);

        // West
        tv_west_title = (TextView)rootView.findViewById(R.id.west);
        tv_west_info = (TextView)rootView.findViewById(R.id.west_info);
//        tv_west_rssi = (TextView)rootView.findViewById(R.id.west_rssi);
//        tv_west_tx = (TextView)rootView.findViewById(R.id.west_tx);

        // Center
        tv_center_title = (TextView)rootView.findViewById(R.id.center);
        tv_center_info = (TextView)rootView.findViewById(R.id.center_info);
//        tv_center_rssi = (TextView)rootView.findViewById(R.id.center_rssi);
//        tv_center_tx = (TextView)rootView.findViewById(R.id.center_tx);

        return rootView;
    }

    public static void clearText(){
        tv_north_title.setText("");
        tv_north_info.setText("");

        tv_south_title.setText("");
        tv_south_info.setText("");

        tv_east_title.setText("");
        tv_east_info.setText("");

        tv_west_title.setText("");
        tv_west_info.setText("");

        tv_center_title.setText("");
        tv_center_info.setText("");
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
