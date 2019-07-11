package com.nguyentinhdeveloper.ghichu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tu.loadingdialog.LoadingDailog;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.nguyentinhdeveloper.ghichu.database.BaseData;
import com.nguyentinhdeveloper.ghichu.model.ModelNote;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

public class HomeNote extends DeclareVariable implements NavigationView.OnNavigationItemSelectedListener {

    View header;
    ImageView urlfb;
    TextView namefb;
    TextView sum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.e("H", getHH() + "");
        Log.e("H", getMM() + "");
        mapped();
        setGood();
        new Thread(new RunAble()).start();
        addRecyclerview();
        if (new CheckInternet().isInternetConnection(this)) {
            getList();
        } else {
            //code...
            list.clear();
            list = new GetDataSQLite().GetData(this);
            addRecyclerview();
            sNote.setText(list.size() + "\tGhi Chú");
            sum.setText(list.size() + "\tGhi Chú");
        }

    }


    private void mapped() {

        baseData = new BaseData(this);


//        anh xa view
        imageView = (ImageView) findViewById(R.id.imageView);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        layout_view = findViewById(R.id.layout_view);
        good = (TextView) findViewById(R.id.textView);
        loichuc = (TextView) findViewById(R.id.loichuc);
        recyclerView = findViewById(R.id.recyclerView);
        drawerLayout = findViewById(R.id.layoutDrawer);
        startMenu = findViewById(R.id.starMenu);
        add_a = findViewById(R.id.add_a);
        sNote = findViewById(R.id.sNote);
        sNote.setText("0 Ghi Chú");
        startMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.letgo));
        add_a.startAnimation(AnimationUtils.loadAnimation(this, R.anim.xoay));
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        urlfb = (ImageView) header.findViewById(R.id.urlfb);
        namefb = (TextView) header.findViewById(R.id.namefb);
        sum = (TextView) header.findViewById(R.id.sum);
        sum.setText("0 Ghi Chú");

//        firebase

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://onlinestore-3ac1a.appspot.com");
        mDataBase = FirebaseDatabase.getInstance().getReference();


//        dataoff
        sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);
        img_User_FB = sharedPreferences.getString("url_img_user", "");
        id_User_FB = sharedPreferences.getString("id", "");
        name_User_FB = sharedPreferences.getString("name", "");

        Picasso.get().load(img_User_FB).fit().transform(boderIMG(0, 50))
                .centerCrop()
                .error(R.drawable.noimg).into(urlfb);
        namefb.setText(name_User_FB);


        startMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });


        findViewById(R.id.addnote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeNote.this, AddNote.class));
            }
        });

    }


    private Integer getHH() {
        Date date = new Date();
        String strDateFormat24 = "HH";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat24);
        return Integer.parseInt(sdf.format(date));
    }


    private Integer getMM() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }


    private void setGood() {

        if (getHH() >= 6 && getHH() < 12) {
            imageView.setImageResource(R.drawable.binhminh_img);
            good.setText(getString(R.string.morning));
            loichuc.setText(getString(R.string.loichuc_1));
            good_i = getString(R.string.loichuc_1);
            findViewById(R.id.addnote).setBackground(getResources().getDrawable(R.drawable.custom_gradient_binhminh));
        }
        if (getHH() >= 12 && getHH() < 18) {
            imageView.setImageResource(R.drawable.hoanghon_img);
            good.setText(getString(R.string.afternoon));
            loichuc.setText(getString(R.string.loichuc_2));
            good_i = getString(R.string.loichuc_2);
            findViewById(R.id.addnote).setBackground(getResources().getDrawable(R.drawable.custom_gradient_hoanghon));
        }
        if (getHH() >= 18 || getHH() < 6) {
            imageView.setImageResource(R.drawable.chaobuoitoi_img);
            good.setText(getString(R.string.evening));
            loichuc.setText(getString(R.string.loichuc_3));
            good_i = getString(R.string.loichuc_3);
            findViewById(R.id.addnote).setBackground(getResources().getDrawable(R.drawable.custom_gradient_a));
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.introduc:
                break;
            case R.id.comment:
                break;
            case R.id.share:
                break;
            case R.id.pass:
                String password = sharedPreferences.getString("PASSWORD", "");
                if (password.equals("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("Cảnh báo");
                    alertDialog.setIcon(R.drawable.logo_note);
                    alertDialog.setMessage("-Khóa ứng dụng:\n+Bảo mật tốt hơn\n+Tránh người khác truy cập ghi chú của bạn\n+Bạn chắc chắn phải nhớ mật khẩu này,một khi đã cài mật khẩu thì khi bạn xóa hết dữ liệu của ứng dụng hay gỡ cài đặt thì khi bạn đăng nhập ứng dụng vẫn sẽ hỏi mật khẩu của bạn\n\t\tCòn đợi gì nữa bắt đầu thôi:))");
                    alertDialog.setPositiveButton("Bắt đầu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(HomeNote.this, LockPassWord.class));
                        }
                    });
                    alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alertDialog.show();
                } else {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("Cảnh báo");
                    alertDialog.setIcon(R.drawable.logo_note);
                    alertDialog.setMessage("Mật khẩu đang được bật,bạn có muốn tắt không?");
                    alertDialog.setPositiveButton("Tắt", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final Dialog dialoga = new Dialog(HomeNote.this);
                            dialoga.setContentView(R.layout.dialog);
                            final EditText pass;
                            Button offpass;
                            pass = (EditText) dialoga.findViewById(R.id.password);
                            offpass = (Button) dialoga.findViewById(R.id.offpass);
                            offpass.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (pass.getText().toString().trim().equals("")) {
                                        pass.setError("Không được để trống");
                                        return;
                                    }
                                    if (new CheckInternet().isInternetConnection(HomeNote.this)) {
                                        if (pass.getText().toString().trim().equals(sharedPreferences.getString("PASSWORD", ""))) {
                                            //mDataBase.child(sharedPreferences.getString("id", "")).child("PASSWORD").setValue("");
                                            edit = sharedPreferences.edit();
                                            edit.putString("PASSWORD", "");
                                            edit.putString("pass", "");
                                            edit.commit();
                                            dialoga.dismiss();
                                        } else {
                                            Toasty.error(HomeNote.this, "Không khớp", Toasty.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                            });


                            dialoga.show();
                        }
                    });
                    alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alertDialog.show();

                }

                break;
            case R.id.logout:
                if (new CheckInternet().isInternetConnection(HomeNote.this)) {
                    new Loading().loading(this, "Đang đăng xuất");
                    LoginManager.getInstance().logOut();
                    edit = sharedPreferences.edit();
                    edit.putString("name", "");
                    edit.putString("id", "");
                    edit.putString("url_img_user", "");
                    edit.putString("PASSWORD", "");
                    edit.putString("pass", "");
                    edit.commit();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            startActivity(new Intent(HomeNote.this, LoginFB.class));
                            finish();
                        }
                    }, 1000);

                }
                break;
            case R.id.exit:
                finish();
                Toasty.success(this, "Good bye", Toasty.LENGTH_LONG).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    class RunAble implements Runnable {
        int seconds;

        public RunAble() {

        }

        @Override
        public void run() {
            for (int i = 0; true; i++) {
                Handler handler = new Handler(Looper.getMainLooper());
                final int intI = i;
                handler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        if (loichuc.getText().toString().equals(getString(R.string.loichuc))) {
                            loichuc.setText(good_i);

                        } else {
                            loichuc.setText(getString(R.string.loichuc));
                        }
                        loichuc.startAnimation(AnimationUtils.loadAnimation(HomeNote.this, R.anim.animation_nhapnhay));


                    }
                });


                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }


    }


    private void addRecyclerview() {
        adapterRecyclerview = new AdapterRecyclerview(this, list, img_User_FB);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterRecyclerview);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (new CheckInternet().isInternetConnection(HomeNote.this)) {
                    adapterRecyclerview.deleteItem(viewHolder.getAdapterPosition(), list.get(viewHolder.getAdapterPosition()).getSubject());
                } else {
                    adapterRecyclerview.notifyDataSetChanged();
                }
            }
        }).attachToRecyclerView(recyclerView);
    }


    private void getList() {
        list.clear();
        baseData.deleteTABLE();
        mDataBase.child(id_User_FB).child("GhiChu").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e("sizelist", list.size() + "");
                ModelNote noteModel = dataSnapshot.getValue(ModelNote.class);
                list.add(0, noteModel);
                Log.e("SQL", baseData.insert(noteModel) + "");
                sNote.setText(list.size() + "\tGhi Chú");
                sum.setText(list.size() + "\tGhi Chú");
                adapterRecyclerview.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void delete(final String id, String subject, final int i1) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có muốn xóa ghi chú\t" + subject + "\tkhông?");
        builder.setIcon(getResources().getDrawable(R.drawable.logo_note));
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {

                mDataBase.child(id_User_FB).child("GhiChu").child(id).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            adapterRecyclerview.deleteItems(i1);
                            Toasty.success(HomeNote.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            sNote.setText(list.size() + "\tGhi Chú");
                            sum.setText(list.size() + "\tGhi Chú");
                        } else {
                            Toasty.error(HomeNote.this, "Thất bại:" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapterRecyclerview.notifyDataSetChanged();
            }
        });
        builder.show();

    }

    private Transformation boderIMG(int boderW, int boderConer) {
        return new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(boderW)
                .cornerRadiusDp(boderConer)
                .oval(false)
                .build();

    }


}
