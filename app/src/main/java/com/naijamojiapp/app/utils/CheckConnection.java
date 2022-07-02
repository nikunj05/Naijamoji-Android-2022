package com.naijamojiapp.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckConnection {
    private static final String TAG = "CheckConnection";
    private static CheckConnection checkConnection;
    private Context context;

    public CheckConnection(Context context) {
        this.context = context;
    }


    public static CheckConnection getInstance(Context context) {
        if (checkConnection == null)
            checkConnection = new CheckConnection(context);
        return checkConnection;

    }

    public boolean isConnectingToInternet() {

        try {

            boolean isConected = false;
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)


                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            isConected = true;
                            break;
                        }
            }
            Log.i(TAG, "" + isConected);
            return isConected;

        } catch (Exception e) {
            return false;
        }

    }

}
