package com.nguyentinhdeveloper.ghichu;


import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class LockPassWord extends DeclareVariable {
    private PinLockView mPinLockView;
    private String TAG = "TAG";
    private IndicatorDots mIndicatorDots;
    private String PASSWORD = "";
    private TextView passi;
    private Button clear;
    private GridView gridView;
    private AdapterPass adapterPass;
    private List<String> listphone=new ArrayList<>();
    private View p1;
    private View p2;
    private View p3;
    private View p4;
    private int i=0,is=0,ixx=0;
    private String password="",s="",sum="";
    private ImageView delete;
    private TextView quenpass;
    private String hellopass;
    private boolean checkis=false;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock_pass_word);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);
        PASSWORD=sharedPreferences.getString("PASSWORD","");
        hellopass=getIntent().getStringExtra("hellopass");
        gridView=findViewById(R.id.grid);
        passi=findViewById(R.id.passi);
        p1 = (View) findViewById(R.id.p1);
        p2 = (View) findViewById(R.id.p2);
        p3 = (View) findViewById(R.id.p3);
        p4 = (View) findViewById(R.id.p4);
        delete = (ImageView) findViewById(R.id.delete);
        quenpass = (TextView) findViewById(R.id.quenpass);
        listphone.clear();
        listphone.add("1");
        listphone.add("2");
        listphone.add("3");
        listphone.add("4");
        listphone.add("5");
        listphone.add("6");
        listphone.add("7");
        listphone.add("8");
        listphone.add("9");
        listphone.add("10");
        listphone.add("0");
        adapterPass=new AdapterPass(LockPassWord.this,listphone);
        gridView.setAdapter(adapterPass);
        deletepass();
        if (!PASSWORD.equals("")){
            sum=PASSWORD;
            checkis=true;
        }else {
            checkis=false;
            sum="";
        }

    }


    public void click(String txt){
        Vibrator vibratora = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibratora.hasVibrator()) {
            vibratora.vibrate(150);
        }
        Log.e("CHECK",checkis+"");
        passi.setText("Mật khẩu gồm 4 số");
        delete.setVisibility(View.VISIBLE);
        if (!checkis){
            Log.e("P",password.length()+"");
            if (i==0 && password.length()<4){
                p1.setBackground(getResources().getDrawable(R.drawable.custom_pass_1));
                password+=txt;
                s=password;
                Log.e("TAG",password);
                i++;
                return;
            }
            if (i==1 && password.length()<4){
                p2.setBackground(getResources().getDrawable(R.drawable.custom_pass_1));
                password+=txt;
                s=password;
                Log.e("TAG",password);
                i++;
                return;
            }
            if (i==2 && password.length()<4){
                p3.setBackground(getResources().getDrawable(R.drawable.custom_pass_1));
                password+=txt;
                s=password;
                Log.e("TAG",password);
                i++;
                return;
            }
            if (i==3 && password.length()<4){
                p4.setBackground(getResources().getDrawable(R.drawable.custom_pass_1));
                password+=txt;
                s=password;
                if (is==0){
                    AlertDialog.Builder builder=new AlertDialog.Builder(LockPassWord.this);
                    builder.setTitle("Cảnh báo");
                    builder.setMessage("Bạn vấn muốn sử dụng mật khẩu " +password+ " chứ?");
                    builder.setCancelable(false);
                    builder.setIcon(R.drawable.logo_note);
                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int it) {
                            passi.setText("Xác nhận mật khẩu");
                            sum=password;
                            Log.e("SUM",sum+"-"+sum.length());
                            is=1;
                            i=0;
                            s="";
                            password="";
                            p1.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                            p2.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                            p3.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                            p4.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                        }
                    });

                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int it) {
                            is=0;
                            password="";
                            i=0;
                            s="";
                            p1.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                            p2.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                            p3.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                            p4.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                        }
                    });

                    builder.show();
                }else {
                    if (password.equals(sum)){
                        if (new CheckInternet().isInternetConnection(this)){

                            mDataBase.child(sharedPreferences.getString("id","")).child("PASSWORD").setValue(password).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (ixx==0){
                                        Toasty.success(LockPassWord.this,"Cài thành công",Toasty.LENGTH_SHORT).show();
                                        edit=sharedPreferences.edit();
                                        edit.putString("PASSWORD",password);
                                        edit.apply();
                                        finish();
                                    }
                                }
                            });
                            finish();
                        }else {
                            is=0;
                            password="";
                            i=0;
                            s="";
                            p1.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                            p2.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                            p3.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                            p4.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            if (vibrator.hasVibrator()) {
                                vibrator.vibrate(150);
                            }
                        }


                    }else {
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (vibrator.hasVibrator()) {
                            vibrator.vibrate(250);
                        }
                        is=1;
                        i=0;
                        password="";
                        p1.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                        p2.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                        p3.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                        p4.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                        passi.startAnimation(AnimationUtils.loadAnimation(LockPassWord.this,R.anim.zoom));
                        passi.setText("Sai mật khẩu");
                    }
                }
                Log.e("TAG",password);
            }



        }else {

                Log.e("P",password.length()+"");
                if (i==0 && password.length()<4){
                    p1.setBackground(getResources().getDrawable(R.drawable.custom_pass_1));
                    password+=txt;
                    s=password;
                    Log.e("TAG",password);
                    i++;
                    return;
                }
                if (i==1 && password.length()<4){
                    p2.setBackground(getResources().getDrawable(R.drawable.custom_pass_1));
                    password+=txt;
                    s=password;
                    Log.e("TAG",password);
                    i++;
                    return;
                }
                if (i==2 && password.length()<4){
                    p3.setBackground(getResources().getDrawable(R.drawable.custom_pass_1));
                    password+=txt;
                    s=password;
                    Log.e("TAG",password);
                    i++;
                    return;
                }
                if (i==3 && password.length()<4) {
                    p4.setBackground(getResources().getDrawable(R.drawable.custom_pass_1));
                    password += txt;

                }

                if (password.equals(PASSWORD)){

                        startActivity(new Intent(LockPassWord.this,HomeNote.class));
                        finish();


                }else {
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator.hasVibrator()) {
                        vibrator.vibrate(150);
                    }
                    is=1;
                    i=0;
                    password="";
                    p1.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                    p2.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                    p3.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                    p4.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                    passi.startAnimation(AnimationUtils.loadAnimation(LockPassWord.this,R.anim.zoom));
                    passi.setText("Sai mật khẩu");


            }


        }





        }






    private void deletepass(){

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(250);
                }
                delete.startAnimation(AnimationUtils.loadAnimation(LockPassWord.this,R.anim.zoom));
                Log.e("I",i+"");
                Log.e("S",s.length()+"");
                Log.e("SUM",sum.length()+"");
                if (s.length()==1){
                    p1.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                    password="";
                    s="";
                    i=0;
                    Log.e("TAG",password);
                    delete.setVisibility(View.INVISIBLE);
                    return;

                }
                if (s.length()==2){
                    p2.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                    password=s.substring(0,1);
                    s=password;
                    i--;
                    Log.e("TAG",password);
                    return;

                }
                if (s.length()==3){
                    p3.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                    password=s.substring(0,2);
                    s=password;
                    i--;
                    Log.e("TAG",password);
                    return;

                }
                if (s.length()==4){
                    p4.setBackground(getResources().getDrawable(R.drawable.custom_pass));
                    password=s.substring(0,3);
                    s=password;
                    i--;
                    Log.e("TAG",password);
                    return;

                }
            }
        });
    }





}
