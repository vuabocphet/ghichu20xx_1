package com.nguyentinhdeveloper.ghichu;


import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class LockPassWord extends DeclareVariable {
    private PinLockView mPinLockView;
    private String TAG = "TAG";
    private IndicatorDots mIndicatorDots;
    private String PASSWORD = "";
    private TextView passi;
    private Button clear;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock_pass_word);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);
        password=sharedPreferences.getString("PASSWORD","");
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        if (!password.equals("")){
            PASSWORD=password;
        }
        clear = findViewById(R.id.clear);
        clear.setVisibility(View.GONE);
        passi = findViewById(R.id.passi);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);

    }

    private void confirm() {
        clear.setVisibility(View.VISIBLE);
        passi.setText("Xác nhận lại mật khẩu");
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PASSWORD = "";
                passi.setText("Mật khẩu gồm 4 số");
                clear.setVisibility(View.GONE);
                mPinLockView.setShowDeleteButton(false);
                mPinLockView.clearFocus();
            }
        });
    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.d(TAG, "Pin complete: " + pin);

            if (!pin.equals("")) {
                if (PASSWORD.equals("")){
                    PASSWORD = pin;
                    mPinLockView.setShowDeleteButton(false);
                    if (password.equals("")){
                        confirm();
                    }
                }else {
                    if (password.equals("")){
                        confirm();
                    }
                    if (pin.equals(PASSWORD)) {
                        passi.setText("Trùng khớp");
                        if (new CheckInternet().isInternetConnection(LockPassWord.this)){
                            if (password.equals("")){
                                edit = sharedPreferences.edit();
                                edit.putString("pass", pin);
                                edit.apply();
                                Toasty.success(LockPassWord.this, "Đã xong", Toasty.LENGTH_SHORT).show();
                                mDataBase.child(sharedPreferences.getString("id","")).child("PASSWORD").setValue(pin);
                                finish();
                            }else {
                                Toasty.success(LockPassWord.this, "Mật khẩu đúng", Toasty.LENGTH_SHORT).show();
                                startActivity(new Intent(LockPassWord.this,HomeNote.class));
                                finish();
                            }

                        }else {
                            Toasty.success(LockPassWord.this, "Không cài được", Toasty.LENGTH_SHORT).show();
                        }

                    } else {
                        mPinLockView.setShowDeleteButton(false);
                        passi.setText("Mật khẩu không khớp,Xin thử lại");
                        passi.startAnimation(AnimationUtils.loadAnimation(LockPassWord.this, R.anim.zoom));
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (vibrator.hasVibrator()) {
                            vibrator.vibrate(250); // for 500 ms
                        }
                    }
                }

            }

        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {

            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);


        }
    };


}
