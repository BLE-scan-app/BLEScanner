package com.wheetam.blescanner.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.wheetam.blescanner.R;
import com.wheetam.blescanner.adapter.Recycler_items;
import com.wheetam.blescanner.model.BLEDevice;
import com.wheetam.blescanner.model.BleScanState;
import com.wheetam.blescanner.model.BleScanner;
import com.wheetam.blescanner.model.SimpleScanCallback;
import com.wheetam.blescanner.receiver.BLEBroadcastReceiver;
import com.wheetam.blescanner.service.BroadcastService;


public class RecyclerActivity extends Fragment implements SimpleScanCallback{
    private final static String TAG = RecyclerActivity.class.getName();
    private static final int IDX_RSSI = 2; // 2,3
    private static final int IDX_TX = 16; // 16,17
    private static final int IDX_ANT = 0; // 0,1

    // For ANT value filtering (Bearing)
    private static final char WEST = '0';
    private static final char SOUTH = '1';
    private static final char NORTH = '2';
    private static final char EAST = '3';

    private char[] old_ant;

    Recycler_items sAdapter;
    List<BLEDevice> mDevices = new ArrayList<BLEDevice>();
    Map<String, BLEDevice> deviceMap = new HashMap<>();

    private Handler mHandler;
    private BleScanner mBleScanner;
    // Stops scanning after 5 minutes
    private static final long SCAN_PERIOD = 30 * 1000;
    private boolean mScanning = false;
    private BearingActivity bearings;

    RecyclerView rvDevices;
    Button scanButton;
    Button stopButton;

    private BLEBroadcastReceiver mMessageReceiver = new BLEBroadcastReceiver();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
        sAdapter = new Recycler_items(getActivity().getApplicationContext(), mDevices);

        old_ant = new char[5];

        setRetainInstance(true);
    }

    private void scanLeDevice(final boolean enable) {

        // Stops scanning after a pre-defined scan period.
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mScanning = false;
//                //updateStopScanButton();
//                stopButton.setVisibility(View.GONE);
//                scanButton.setVisibility(View.VISIBLE);
//                mBleScanner.stopBleScan();
//                mBleScanner = null;
//                getActivity().stopService(new Intent(getActivity().getApplicationContext(), BroadcastService.class));
//
//            }
//        }, SCAN_PERIOD);


        mScanning = true;
        if (mBleScanner == null) {
            mBleScanner = new BleScanner(getActivity().getApplicationContext(), this);
        }

        mBleScanner.startBleScan(); // factory for scanner version
        getActivity().startService(new Intent(getActivity().getApplicationContext(), BroadcastService.class));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_devices, container, false);

        rvDevices = (RecyclerView)rootView.findViewById(R.id.rv_view);
        scanButton = (Button) rootView.findViewById(R.id.scan_btn);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mScanning) {
                    Log.d("TAG", "scanning");
                    mDevices.clear();
                    deviceMap.clear();
                    //updateStartScanButton();
                    scanButton.setVisibility(View.GONE);
                    stopButton.setVisibility(View.VISIBLE);
                    scanLeDevice(true);
                    mScanning = true;
                }
            }
        });

        stopButton = (Button) rootView.findViewById(R.id.stop_btn);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScanning = false;
                stopButton.setVisibility(View.GONE);
                scanButton.setVisibility(View.VISIBLE);
                mBleScanner.stopBleScan();
                mBleScanner = null;
                getActivity().stopService(new Intent(getActivity().getApplicationContext(), BroadcastService.class));
                mDevices.clear();
                deviceMap.clear();
                bearings.clearText();
            }
        });

        rvDevices.setHasFixedSize(true);
        rvDevices.setAdapter(sAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        rvDevices.setLayoutManager(llm);

        return rootView;
    }

    private void updateStartScanButton() {
//        pBar.setVisibility(View.VISIBLE);
        scanButton.setText("Stop Scanning");
//        scanButton.setBackground(getResources().getDrawable(R.drawable.circle_button2));
//        scanButton.setTextColor(getResources().getColor(R.color.gray));
//        scanButton.setTextColor(Color.GRAY);
        scanButton.setClickable(true);
    }


    private void updateStopScanButton() {
//        pBar.setVisibility(View.INVISIBLE);
        scanButton.setText("Start Scanning");
        scanButton.setClickable(true);
//        scanButton.setBackground(getResources().getDrawable(R.drawable.circle_button));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(
                mMessageReceiver, new IntentFilter(BLEBroadcastReceiver.RECEIVE_UPDATE));
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("onpause","App destroyed");
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("ondestroy","App destroyed");
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                .unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onBleScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.d("TAG", "onBleScan callback reached on UI thread :" + "device: " + device + " rssi: " + rssi + " scanR: " + scanRecord);
        BLEDevice d = new BLEDevice(device.getName(), rssi, scanRecord);
        String deviceName=device.getName();
        String dataForBearing = d.getResData();   // BearingActivity로 보낼 문자열

        char getOldAnt = ' ';
        char AntValue = dataForBearing.charAt(IDX_ANT+1);
        String rssi_ = ""+dataForBearing.charAt(IDX_RSSI)+dataForBearing.charAt(IDX_RSSI+1);
        String tx_ = ""+dataForBearing.charAt(IDX_TX)+dataForBearing.charAt(IDX_TX+1);

        // device name filtering
        if(deviceName != null && deviceName.contains("Polar")){
            // POlar를 포함한 이름을 가진 디바이스만 recycler view에 추가

            // list에 추가
            if (!deviceMap.containsKey(deviceName)) {
                Log.i("ant refresh","new scan" + deviceName + " with new ant " + AntValue);

//                getOldAnt = ' ';

                deviceMap.put(deviceName, d);
                mDevices.add(d);
                sAdapter.notifyDataSetChanged();
            } else {  // list에 이미 존재할 경우
                // 해당 deviceName의 이전 ANT 값 가져오기
                if(deviceName.contains("1"))
                    getOldAnt = old_ant[1];
                else if(deviceName.contains("2"))
                    getOldAnt = old_ant[2];
                else if(deviceName.contains("3"))
                    getOldAnt = old_ant[3];
                else if(deviceName.contains("4"))
                    getOldAnt = old_ant[4];

                Log.i("ant refresh","scan again" + deviceName + " with old ant " + getOldAnt);

                 int pos = mDevices.indexOf(deviceMap.get(deviceName));
                //Log.i("for_checking",mDevices.indexOf(deviceMap.get(deviceName))+"");
                mDevices.remove(deviceMap.get(deviceName));
                deviceMap.put(deviceName, d);

                mDevices.add(pos, d); // 같은 위치에 다시 추가하기
                //mDevices.add(d);
                sAdapter.notifyDataSetChanged();
            }

            // 안테나 값 old_ant에 저장하는 부분
            if(deviceName.contains("1"))
                old_ant[1] = AntValue;
            else if(deviceName.contains("2"))
                old_ant[2] = AntValue;
            else if(deviceName.contains("3"))
                old_ant[3] = AntValue;
            else if(deviceName.contains("4"))
                old_ant[4] = AntValue;

            // 이전 ANT 값 변수에 공백이 들어있으면 새로 추가되는 것 / 아니면 기존 것 삭제 작업
            if(getOldAnt != ' ' && (AntValue != getOldAnt)){
                Log.i("ant refresh","removing old " + deviceName + " at old ant " + getOldAnt);
                switch(getOldAnt){
                    case EAST: // East
                        bearings.tv_east_title.setText("");
                        bearings.tv_east_info.setText("");
                        break;
                    case WEST: // West
                        bearings.tv_west_title.setText("");
                        bearings.tv_west_info.setText("");
                        break;
                    case SOUTH: // South
                        bearings.tv_south_title.setText("");
                        bearings.tv_south_info.setText("");
                        break;
                    case NORTH: // North
                        bearings.tv_north_title.setText("");
                        bearings.tv_north_info.setText("");
                        break;
                }
            }


            // Set center first
            // 데이터를 bearing activity에도 추가 (rssi, tx)
            if(deviceName.equals("Polar0")){
                Log.i("ant refresh","set " + deviceName);
                bearings.tv_center_title.setText(deviceName);
                bearings.tv_center_info.setText("Tx: " + Integer.parseInt(tx_, 16));
//                BearingActivity.tv_center_rssi.setText("RSSI : " + rssi_ + "dBm");
//                BearingActivity.tv_center_tx.setText("TX : " + tx_);
            }
            else{ // For POlar1,2,3,4
                Log.i("ant refresh","set new " + deviceName + " at ant " + AntValue);
                switch(AntValue){
                    case EAST: // East
                        bearings.tv_east_title.setText(deviceName);
                        bearings.tv_east_info.setText("Polar0:\n"
                                + "Rx: " + (Integer.parseInt(rssi_, 16)-100) + "dBm\n"
                                + "Tx: " + Integer.parseInt(tx_, 16));
//                        BearingActivity.tv_east_rssi.setText("RSSI : " + rssi_ + "dBm");
//                        BearingActivity.tv_east_tx.setText("TX : " + tx_);
                        break;
                    case WEST: // West
                        bearings.tv_west_title.setText(deviceName);
                        bearings.tv_west_info.setText("Polar0:\n"
                                + "Rx: " + (Integer.parseInt(rssi_, 16)-100) + "dBm\n"
                                + "Tx: " + Integer.parseInt(tx_, 16));
//                        BearingActivity.tv_west_rssi.setText("RSSI : " + rssi_ + "dBm");
//                        BearingActivity.tv_west_tx.setText("TX : " + tx_);
                        break;
                    case SOUTH: // South
                        bearings.tv_south_title.setText(deviceName);
                        bearings.tv_south_info.setText("Polar0:\n"
                                + "Rx: " + (Integer.parseInt(rssi_, 16)-100) + "dBm\n"
                                + "Tx: " + Integer.parseInt(tx_, 16));
//                        BearingActivity.tv_south_rssi.setText("RSSI : " + rssi_ + "dBm");
//                        BearingActivity.tv_south_tx.setText("TX : " + tx_);
                        break;
                    case NORTH: // North
                        bearings.tv_north_title.setText(deviceName);
                        bearings.tv_north_info.setText("Polar0:\n"
                                + "Rx: " + (Integer.parseInt(rssi_, 16)-100) + "dBm\n"
                                + "Tx: " + Integer.parseInt(tx_, 16));
//                        BearingActivity.tv_north_rssi.setText("RSSI : " + rssi_ + "dBm");
//                        BearingActivity.tv_north_tx.setText("TX : " + tx_);
                        break;
                } //switch
            } //else
        } // if - POlar text filter

    } // onBleScan method

    @Override
    public void onBleScanFailed(BleScanState scanState) {

    }
}
