package com.nguyentinhdeveloper.ghichu;

import android.content.Context;

import com.android.tu.loadingdialog.LoadingDailog;

public class Loading {
    private static  LoadingDailog dialogx;
    public void loading(Context context,String message) {
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(context)
                .setMessage(message)
                .setCancelable(true)
                .setCancelOutside(true);
        dialogx = loadBuilder.create();
        dialogx.setCanceledOnTouchOutside(false);
        dialogx.show();
    }
    public void cancelShow(){
        dialogx.cancel();
    }

}
