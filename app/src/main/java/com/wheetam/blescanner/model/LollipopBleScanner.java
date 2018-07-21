package com.wheetam.blescanner.model;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LollipopBleScanner extends BleScanner.BaseBleScanner {

    private final static String TAG = LollipopBleScanner.class.getName();


    private BluetoothLeScanner mBluetoothScanner = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private SimpleScanCallback mScanCallback = null;
    private ScanSettings.Builder mScanSettings = null;


    public LollipopBleScanner(SimpleScanCallback callback) {
        mScanCallback = callback;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            mBluetoothScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }




    }

    @SuppressWarnings(value = {"deprecation"})
    @Override
    public void onStartBleScan(long timeoutMillis) {
        //long delay = timeoutMillis == 0 ? defaultTimeout : timeoutMillis;
        if (mBluetoothScanner != null && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            try {
                mScanSettings = new ScanSettings.Builder();
                mScanSettings.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
                ScanSettings scanSettings = mScanSettings.build();

                mBluetoothScanner.startScan(null, scanSettings, scanCallback);
                Log.i("lollipop_ver","Low latency mode on");
                //mBluetoothScanner.startScan(scanCallback);
                isScanning = true;
            } catch (Exception e) {
                isScanning = false;
                Log.e(TAG, e.toString());
            }
            //timeoutHandler.postDelayed(timeoutRunnable, delay);
        } else {
            mScanCallback.onBleScanFailed(BleScanState.BLUETOOTH_OFF);
        }
        Log.d(TAG, "mBluetoothScanner.startScan()");
    }

    @SuppressWarnings(value = {"deprecation"})
    @Override
    public void onStartBleScan() {
        if (mBluetoothScanner != null && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            try {
                mScanSettings = new ScanSettings.Builder();
                mScanSettings.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
                ScanSettings scanSettings = mScanSettings.build();

                mBluetoothScanner.startScan(null, scanSettings, scanCallback);
                Log.i("lollipop_ver","Low latency mode on 222");
                isScanning = true;
            } catch (Exception e) {
                isScanning = false;
                mScanCallback.onBleScanFailed(BleScanState.BLUETOOTH_OFF);
                Log.e(TAG, e.toString());
            }
        } else {
            mScanCallback.onBleScanFailed(BleScanState.BLUETOOTH_OFF);
        }
        Log.d(TAG, "mBluetoothScanner.startScan()");
    }

    @SuppressWarnings(value = {"deprecation"})
    @Override
    public void onStopBleScan() {
        isScanning = false;
        if (mBluetoothScanner != null && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            try {
                mBluetoothScanner.stopScan(scanCallback);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        Log.d(TAG, "mBluetoothScanner.stopScan()");
    }


    @Override
    public void onBleScanFailed(BleScanState scanState) {
        mScanCallback.onBleScanFailed(scanState);
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(final int callbackType, final ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d(TAG, "onScanResult: " + callbackType + " ScanResult:" + result);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (result.getScanRecord() != null) {
                        mScanCallback.onBleScan(result.getDevice(), result.getRssi(), result.getScanRecord().getBytes());
                    }
                }
            });

        }


        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d(TAG, "onBatchScanResults()");
        }

        @Override
        public void onScanFailed(int errorCode) {
            //error code 3 or 1 todo
            if (errorCode != 3 && errorCode != 1) {
                Log.d(TAG, "onScanFailed: " + errorCode);
                //mScanCallback.onBleScanFailed(BleScanState.newInstance(errorCode));
            }
        }
    };
}
