package com.nguyentinhdeveloper.ghichu;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import es.dmoral.toasty.Toasty;

public class LoginFB extends DeclareVariable {
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private CheckBox checkbox;
    private LoginButton loginButton;
    private Button letgo;
    private String facebookUserId = "";
    private TextView dieukhoan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.nguyentinhdeveloper.ghichu",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
               // Toast.makeText(this, Base64.encodeToString(md.digest(), Base64.DEFAULT)+"", Toast.LENGTH_SHORT).show();

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.introduction_view);
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);
        mDataBase = FirebaseDatabase.getInstance().getReference();

        checkbox = (CheckBox) findViewById(R.id.checkbox);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        letgo = findViewById(R.id.letgo);
        dieukhoan = findViewById(R.id.dieukhoan);
        letgo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.letgo));
        checkbox.setChecked(false);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                letgo.setEnabled(true);
                Toasty.error(LoginFB.this,"Lỗi:"+error.toString(),Toasty.LENGTH_LONG).show();
            }
        });
        letgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                letgo.setEnabled(false);
                if (new CheckInternet().isInternetConnection(LoginFB.this)) {
                    if (checkbox.isChecked()) {
                        loginButton.performClick();

                    } else {
                        letgo.setEnabled(true);
                        Toasty.error(LoginFB.this, "Vui lòng đồng ý với điều khoản", Toast.LENGTH_SHORT).show();
                        checkbox.startAnimation(AnimationUtils.loadAnimation(LoginFB.this, R.anim.check));
                    }

                }

            }
        });
        dieukhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginFB.this,DieuKhoanNguoiDung.class));
            }
        });


    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                edit = sharedPreferences.edit();
                                Log.e("USER", "Tên:" + user.getDisplayName());
                                edit.putString("name", user.getDisplayName());
                                edit.putString("id", user.getUid());
                                edit.putString("url_img_user", user.getPhotoUrl()+"?height=500");
                                edit.commit();
                                mDataBase.child(user.getUid()).child("PASSWORD").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue()!=null ){
                                            if (!dataSnapshot.getValue().toString().equals("")){
                                                Log.e("Pass",dataSnapshot.getValue().toString());
                                                edit.putString("PASSWORD", dataSnapshot.getValue().toString());
                                                edit.commit();
                                                startActivity(new Intent(LoginFB.this,LockPassWord.class));
                                                finish();
                                                return;
                                            }
                                        }

                                        Toasty.success(LoginFB.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginFB.this,HomeNote.class));
                                        finish();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.e("Pass","NOPASSS");
                                    }
                                });
                            }

                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

