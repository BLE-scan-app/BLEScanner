package com.wheetam.blescanner.model;

import android.util.Log;

public class BLEDevice {

    public String name;
    public int rssi;
    public String scanRecord;
    public static char[] resData;
    private final static String TAG = "hex_check";

    private final static int IDX_SCAN_RESPONSE = 30;

    private final static int IDX_ANT = 32;
    private final static int IDX_RSSI = 34;
    private final static int IDX_X1 = 36;
    private final static int IDX_Y1 = 38;
    private final static int IDX_Z1 = 40;
    private final static int IDX_X2 = 42;
    private final static int IDX_Y2 = 44;
    private final static int IDX_Z2 = 46;
    private final static int IDX_TX = 52;

    public BLEDevice(String name, int rssi, byte[] scanRecord){
        this.name = name;
        this.rssi = rssi;
        this.scanRecord = BLEDevice.bytesToHex(scanRecord);
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes){
        char[] hexChars = new char[bytes.length * 2];

        char[] advData = new char[bytes.length];
//        char[] resData = new char[bytes.length];
        resData = new char[bytes.length];

//        Log.i(TAG,"byte length : "+bytes.length);

        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;  //byte to int
            hexChars[j * 2] = hexArray[v >>> 4];  //high nibble
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; //low nibble
            advData[j] = hexChars[j];
//            if(j==31)
//                Log.i(TAG,"Advertising Data: "+ (new String(hexChars)));
        }
        String strHexChars = new String(hexChars);

        for (int k = IDX_SCAN_RESPONSE; k < IDX_SCAN_RESPONSE+62; k++ ){
            resData[k - IDX_SCAN_RESPONSE] = strHexChars.charAt(k);
        }
        String chosenData = new String();
//        for (int k = IDX_ANT; k < IDX_TX+2; k++){
//            chosenData += resData[k];
//        }


        chosenData += "\nANT : "+resData[IDX_ANT]+resData[IDX_ANT+1]
                + "\nRSSI : "+resData[IDX_RSSI]+resData[IDX_RSSI+1]+"dBm"
                + "\nX : "+resData[IDX_X1]+resData[IDX_X1+1] + "\t\tY : "+resData[IDX_Y1]+resData[IDX_Y1+1] + "\t\tZ : "+resData[IDX_Z1]+resData[IDX_Z1+1]
                + "\nX : "+resData[IDX_X2]+resData[IDX_X2+1] + "\t\tY : "+resData[IDX_Y2]+resData[IDX_Y2+1] + "\t\tZ : "+resData[IDX_Z2]+resData[IDX_Z2+1]
                + "\nTX : "+resData[IDX_TX]+resData[IDX_TX+1];



//        Log.i(TAG,"Advertising Data (" + advData.length + ") : "+ (new String(advData)));
        Log.i(TAG,"Scan Response Data (" + resData.length + ") : "+ (new String(resData)));

//        return new String(hexChars);
//        return strHexChars;
//        return new String(resData);
        return chosenData;
    }

    public static String getResData(){
        String res = "";
        res += ""+resData[IDX_ANT]+resData[IDX_ANT+1] + resData[IDX_RSSI]+resData[IDX_RSSI+1]
                + resData[IDX_X1]+resData[IDX_X1+1] + resData[IDX_Y1]+resData[IDX_Y1+1] + resData[IDX_Z1]+resData[IDX_Z1+1]
                + resData[IDX_X2]+resData[IDX_X2+1] + resData[IDX_Y2]+resData[IDX_Y2+1] + resData[IDX_Z2]+resData[IDX_Z2+1]
                + resData[IDX_TX]+resData[IDX_TX+1];

        Log.i(TAG,"Scan Response Data for Bearing (" + resData.length + ") : "+ res);

        return res;
    }
}