package com.nguyentinhdeveloper.ghichu;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import es.dmoral.toasty.Toasty;

public class CheckInternet {
    public boolean isInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            return true;
        } else {
            Toasty.normal(context, "No Internet", Toast.LENGTH_SHORT, ContextCompat.getDrawable(context, R.drawable.wifi)).show();
            return false;
        }
    }
}
