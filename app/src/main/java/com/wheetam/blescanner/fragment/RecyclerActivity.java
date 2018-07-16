package com.wheetam.blescanner.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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
    private static final String EAST = "30";
    private static final String WEST = "31";
    private static final String SOUTH = "32";
    private static final String NORTH = "33";

    Recycler_items sAdapter;
    List<BLEDevice> mDevices = new ArrayList<BLEDevice>();
    Map<String, BLEDevice> deviceMap = new HashMap<>();

    private Handler mHandler;
    private BleScanner mBleScanner;
    // Stops scanning after 30 minutes.
    private static final long SCAN_PERIOD = 1800 * 1000;
    private boolean mScanning = false;

    RecyclerView rvDevices;
    Button scanButton;

    private BLEBroadcastReceiver mMessageReceiver = new BLEBroadcastReceiver();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
        sAdapter = new Recycler_items(getActivity().getApplicationContext(), mDevices);

        setRetainInstance(true);
    }

    private void scanLeDevice(final boolean enable) {

        // Stops scanning after a pre-defined scan period.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanning = false;
                updateStopScanButton();
                mBleScanner.stopBleScan();
                mBleScanner = null;
                getActivity().stopService(new Intent(getActivity().getApplicationContext(), BroadcastService.class));

            }
        }, SCAN_PERIOD);


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
                    updateStartScanButton();
                    scanLeDevice(true);
                    mScanning = true;
                }
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
        scanButton.setText(R.string.Scanning);
//        scanButton.setBackground(getResources().getDrawable(R.drawable.circle_button2));
//        scanButton.setTextColor(getResources().getColor(R.color.gray));
//        scanButton.setTextColor(Color.GRAY);
        scanButton.setClickable(false);
    }


    private void updateStopScanButton() {
//        pBar.setVisibility(View.INVISIBLE);
        scanButton.setText(R.string.StartScan);
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
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBleScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.d("TAG", "onBleScan callback reached on UI thread :" + "device: " + device + " rssi: " + rssi + " scanR: " + scanRecord);
        BLEDevice d = new BLEDevice(device.getName(), rssi, scanRecord);
        String deviceName=device.getName();
        String dataForBearing = d.getResData();   // BearingActivity로 보낼 문자열

        // device name filtering
        if(deviceName != null && deviceName.contains("POlar")){
            // POlar를 포함한 이름을 가진 디바이스만 recycler view에 추가
            if (!deviceMap.containsKey(deviceName)) {
                deviceMap.put(deviceName, d);
                mDevices.add(d);
                sAdapter.notifyDataSetChanged();
            } else {
                //in 15 seconds the rssi can change
                mDevices.remove(deviceMap.get(deviceName));
                deviceMap.put(deviceName, d);
                mDevices.add(d);
                sAdapter.notifyDataSetChanged();
            }

            char AntValue = dataForBearing.charAt(IDX_ANT+1);
            String rssi_ = ""+dataForBearing.charAt(IDX_RSSI)+dataForBearing.charAt(IDX_RSSI+1);
            String tx_ = ""+dataForBearing.charAt(IDX_TX)+dataForBearing.charAt(IDX_TX+1);

            // Set center first
            // 데이터를 bearing activity에도 추가 (rssi, tx)
            if(deviceName.equals("POlar0")){
                BearingActivity.tv_center_rssi.setText("RSSI : " + rssi_ + "dBm");
                BearingActivity.tv_center_tx.setText("TX : " + tx_);
            }
            else{ // For POlar1,2,3,4
                switch(AntValue){
                    case '1': // East
                        BearingActivity.tv_east_title.setText(deviceName);
                        BearingActivity.tv_east_rssi.setText("RSSI : " + rssi_ + "dBm");
                        BearingActivity.tv_east_tx.setText("TX : " + tx_);
                        break;
                    case '2': // West
                        BearingActivity.tv_west_title.setText(deviceName);
                        BearingActivity.tv_west_rssi.setText("RSSI : " + rssi_ + "dBm");
                        BearingActivity.tv_west_tx.setText("TX : " + tx_);
                        break;
                    case '3': // South
                        BearingActivity.tv_south_title.setText(deviceName);
                        BearingActivity.tv_south_rssi.setText("RSSI : " + rssi_ + "dBm");
                        BearingActivity.tv_south_tx.setText("TX : " + tx_);
                        break;
                    case '4': // North
                        BearingActivity.tv_north_title.setText(deviceName);
                        BearingActivity.tv_north_rssi.setText("RSSI : " + rssi_ + "dBm");
                        BearingActivity.tv_north_tx.setText("TX : " + tx_);
                        break;
                } //switch
            } //else
        } // if - POlar text filter

    } // onBleScan method

    @Override
    public void onBleScanFailed(BleScanState scanState) {

    }
}
