package com.nguyentinhdeveloper.ghichu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Screen extends DeclareVariable {
    private LinearLayout linearLayout;
    private ProgressBar pr;
    private TextView textView;
    private String password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        textView=findViewById(R.id.textView);
        pr = (ProgressBar) findViewById(R.id.pr);
        if (getHH() >= 6 && getHH() < 12) {
            linearLayout.setBackground(getResources().getDrawable(R.drawable.custom_gradient_binhminh));
            pr.setBackground(getResources().getDrawable(R.drawable.custom_gradient_binhminh));
            textView.setText(getString(R.string.morning));
        }
        if (getHH() >= 12 && getHH() < 18) {
            linearLayout.setBackground(getResources().getDrawable(R.drawable.custom_gradient_hoanghon));
            pr.setBackground(getResources().getDrawable(R.drawable.custom_gradient_hoanghon));
            textView.setText(getString(R.string.afternoon));
        }
        if (getHH() >= 18 || getHH() < 6) {
            linearLayout.setBackground(getResources().getDrawable(R.drawable.custom_gradient_a));
            pr.setBackground(getResources().getDrawable(R.drawable.custom_gradient_a));
            textView.setText(getString(R.string.evening));
        }
        img_User_FB=sharedPreferences.getString("url_img_user","");
        id_User_FB=sharedPreferences.getString("id","");
        name_User_FB=sharedPreferences.getString("name","");
        password=sharedPreferences.getString("PASSWORD","");
        Log.e("MATKHAU",password);
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!password.equals("")){
                    startActivity(new Intent(Screen.this,LockPassWord.class));
                    finish();
                    return;
                }
                if (id_User_FB.equals("")){
                    startActivity(new Intent(Screen.this,LoginFB.class));
                    finish();
                }
                else {
                    startActivity(new Intent(Screen.this,HomeNote.class));
                    finish();
                }
            }
        },2500);
    }
    private Integer getHH() {
        Date date = new Date();
        String strDateFormat24 = "HH";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat24);
        return Integer.parseInt(sdf.format(date));
    }
}
