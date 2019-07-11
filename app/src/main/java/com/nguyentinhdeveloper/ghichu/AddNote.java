package com.nguyentinhdeveloper.ghichu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.android.tu.loadingdialog.LoadingDailog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.nguyentinhdeveloper.ghichu.database.BaseData;
import com.nguyentinhdeveloper.ghichu.model.ModelNote;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class AddNote extends DeclareVariable implements Runnable {
    private LinearLayout layout2;
    private TextView date;
    private TextView save;
    private LinearLayout layout;
    private EditText subject;
    private EditText note;
    private LinearLayout layout1;
    private ImageView image;
    private ImageView audio;
    private ImageView star;
    private ImageView align;
    private ImageView img;
    private Animation zoom;
    private Boolean isalign = true, isSave = true;
    private String starcheck = "no";
    private String path = "", pathAudio = "";
    private boolean isStar = true;
    private String url_img = "", url_audio = "";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private SeekBar seekBar;
    private boolean wasPlaying = false;
    private FloatingActionButton fab;
    private TextView seekBarHint;
    private LoadingDailog dialogx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bungee.fade(this);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://onlinestore-3ac1a.appspot.com");
        sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);
        id_User_FB = sharedPreferences.getString("id", "");
        mDataBase = FirebaseDatabase.getInstance().getReference();
        baseData = new BaseData(this);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        date = (TextView) findViewById(R.id.date);
        save = (TextView) findViewById(R.id.save);
        layout = (LinearLayout) findViewById(R.id.layout);
        subject = (EditText) findViewById(R.id.subject);
        note = (EditText) findViewById(R.id.note);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        image = (ImageView) findViewById(R.id.image);
        audio = (ImageView) findViewById(R.id.audio);
        star = (ImageView) findViewById(R.id.star);
        align = (ImageView) findViewById(R.id.align);
        img = (ImageView) findViewById(R.id.img);
        zoom = AnimationUtils.loadAnimation(this, R.anim.zoom);
        closeIMG = findViewById(R.id.closeIMG);
        closeAudio = findViewById(R.id.closeAudio);
        fab = findViewById(R.id.button);
        seekBarHint = findViewById(R.id.textView);
        seekBar = findViewById(R.id.seekbar);
        //setGood();
        click();
        star();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                seekBarHint.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                seekBarHint.setVisibility(View.VISIBLE);
                int x = (int) Math.ceil(progress / 1000f);

                if (x < 10)
                    seekBarHint.setText("0:0" + x);
                else
                    seekBarHint.setText("0:" + x);

                double percent = progress / (double) seekBar.getMax();
                int offset = seekBar.getThumbOffset();
                int seekWidth = seekBar.getWidth();
                int val = (int) Math.round(percent * (seekWidth - 2 * offset));
                int labelWidth = seekBarHint.getWidth();
                seekBarHint.setX(offset + seekBar.getX() + val
                        - Math.round(percent * offset)
                        - Math.round(percent * labelWidth / 2));

                if (progress > 0 && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    clearMediaPlayer();
                    fab.setImageDrawable(ContextCompat.getDrawable(AddNote.this, android.R.drawable.ic_media_play));
                    AddNote.this.seekBar.setProgress(0);
                }

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });
    }


    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            img();

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toasty.error(AddNote.this, "Cần cấp quyền\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    PermissionListener permissionlistener1 = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            record();

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toasty.error(AddNote.this, "Cần cấp quyền\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private Integer getHH() {
        Date date = new Date();
        String strDateFormat24 = "HH";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat24);
        return Integer.parseInt(sdf.format(date));
    }

//    private void setGood() {
//
//        if (getHH() >= 6 && getHH() < 12) {
//            closeIMG.setBackground(getResources().getDrawable(R.drawable.custom_gradient_binhminh));
//            findViewById(R.id.layout1).setBackground(getResources().getDrawable(R.drawable.style_toolbar_b));
//            findViewById(R.id.layout2).setBackground(getResources().getDrawable(R.drawable.style_toolbar_b));
//            findViewById(R.id.subject).setBackground(getResources().getDrawable(R.drawable.style_toolbar_b));
//            findViewById(R.id.note).setBackground(getResources().getDrawable(R.drawable.style_toolbar_b));
//        }
//        if (getHH() >= 12 && getHH() < 18) {
//            closeIMG.setBackground(getResources().getDrawable(R.drawable.custom_gradient_hoanghon));
//            findViewById(R.id.layout1).setBackground(getResources().getDrawable(R.drawable.style_toolbar_a));
//            findViewById(R.id.layout2).setBackground(getResources().getDrawable(R.drawable.style_toolbar_a));
//            findViewById(R.id.subject).setBackground(getResources().getDrawable(R.drawable.style_toolbar_a));
//            findViewById(R.id.note).setBackground(getResources().getDrawable(R.drawable.style_toolbar_a));
//        }
//        if (getHH() >= 18 || getHH() < 6) {
//            closeIMG.setBackground(getResources().getDrawable(R.drawable.custom_gradient_a));
//            findViewById(R.id.layout1).setBackground(getResources().getDrawable(R.drawable.style_toolbar));
//            findViewById(R.id.layout2).setBackground(getResources().getDrawable(R.drawable.style_toolbar));
//            findViewById(R.id.subject).setBackground(getResources().getDrawable(R.drawable.style_toolbar));
//            findViewById(R.id.note).setBackground(getResources().getDrawable(R.drawable.style_toolbar));
//        }
//
//    }

    private void click() {
        //editor = sharedPreferences.edit();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date.startAnimation(zoom);
                finish();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.startAnimation(zoom);
                permission();
            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio.startAnimation(zoom);
                permissionAudio();
            }
        });


        align.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alignFun();
            }
        });
        note.setEnabled(false);
        note.setAlpha(0.5f);
        subject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 50) {
                    subject.setError(getString(R.string.err));

                }
                if (s.length() == 0) {
                    note.setEnabled(false);
                    note.setAlpha(0.5f);
                    save.setAlpha(0.5f);
                    save.setEnabled(false);
                } else {

                    note.setAlpha(1f);
                    note.setEnabled(true);
                    if (!note.getText().toString().trim().equals("")) {
                        save.setTextColor(getResources().getColor(R.color.colorAccent));
                        save.setAlpha(1);
                        save.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                date.setText(getDate());
                if (s.length() == 0) {
                    save.setTextColor(getResources().getColor(R.color.black));
                    save.setAlpha(0.5f);
                    save.setEnabled(false);
                    isSave = false;
                } else if (!subject.getText().toString().trim().equals("")) {
                    save.setTextColor(getResources().getColor(R.color.colorAccent));
                    save.setAlpha(1f);
                    save.setEnabled(true);
                }
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (new CheckInternet().isInternetConnection(AddNote.this)) {
                            LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(AddNote.this)
                                    .setMessage("Đang lưu")
                                    .setCancelable(true)
                                    .setCancelOutside(true);
                            dialogx = loadBuilder.create();
                            dialogx.setCanceledOnTouchOutside(false);
                            dialogx.show();
                            if (!path.equals("") && pathAudio.equals("")) {
                                uploadImage(path, 0);
                                Log.e("1", "1");
                            }
                            if (path.equals("") && !pathAudio.equals("")) {
                                Log.e("2", "2");
                                uploadAudio(pathAudio);
                            }
                            if (pathAudio.equals("") && path.equals("")) {
                                Log.e("3", "3");
                                ModelNote model = new ModelNote("" + Calendar.getInstance().getTimeInMillis(), subject.getText().toString().trim(), date.getText().toString().trim(), note.getText().toString(), "", "", starcheck);
                                mDataBase.child(id_User_FB).child("GhiChu").child(model.getId()).setValue(model);
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 1000);
                            }
                            if (!path.equals("") && !pathAudio.equals("")) {
                                Log.e("4", "4");
                                uploadImage(path, 1);
                            }
                        }


                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void alignFun() {
        align.startAnimation(zoom);
        if (isalign) {
            note.setGravity(Gravity.CENTER_HORIZONTAL);
            isalign = false;

        } else {
            note.setGravity(Gravity.TOP | Gravity.LEFT);
            isalign = true;
        }
    }

    private String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
        String datetime = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + "\t" + dateformat.format(c.getTime());
        return datetime;
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.BLACK)
                    .borderWidthDp(0)
                    .cornerRadiusDp(10)
                    .oval(false)
                    .build();
            Picasso.get().load(uri).fit().transform(transformation)
                    .centerCrop().placeholder(R.drawable.noimg)
                    .error(R.drawable.noimg).into(img);
            img.setVisibility(View.VISIBLE);
            closeIMG.setVisibility(View.VISIBLE);
            path = String.valueOf(getRealPathFromURI(uri));
            closeIMG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    path = "";
                    img.setVisibility(View.GONE);
                    closeIMG.setVisibility(View.GONE);
                }
            });

        }
        if (requestCode == 999 && resultCode == RESULT_OK && data != null) {
            final Uri uri = data.getData();
            String[] audio = {"mp3", "wma", "wav"};
//            uploadAudio(getRealPathFromURIAudio(uri));

            Log.e("III", uri.toString());
            if (uri.toString().endsWith(".mp3") || uri.toString().endsWith(".wma") || uri.toString().endsWith(".wav")) {
                //code
                Log.e("URL_AUDIO", getRealPathFromURIAudio(uri));
                closeAudio.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);
                seekBarHint.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playSong(uri);
                    }
                });
                pathAudio = String.valueOf(getRealPathFromURIAudio(uri));
                closeAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pathAudio = "";
                        fab.setVisibility(View.GONE);
                        seekBarHint.setVisibility(View.GONE);
                        seekBar.setVisibility(View.GONE);
                        closeAudio.setVisibility(View.GONE);
                    }
                });

            } else {
                Toasty.error(AddNote.this, "Không đúng định dạng(mp3|wma|wav)").show();
            }

        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public String getRealPathFromURIAudio(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }


    private void permission() {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void permissionAudio() {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener1)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void img() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent, 1000);
    }

    private void record() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(Intent.createChooser(intent, "Lựa chọn Audio dưới 10MB"), 999);
    }

    private void star() {
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star.startAnimation(new AnimationUtils().loadAnimation(AddNote.this, R.anim.zoom_in));
                if (isStar) {
                    star.setImageResource(R.drawable.star_yes);
                    Toasty.success(AddNote.this, "Đánh dấu sao", Toasty.LENGTH_SHORT).show();
                    starcheck = "yes";
                    isStar = false;
                } else {
                    star.setImageResource(R.drawable.star_no);
                    starcheck = "no";
                    isStar = true;
                }
            }
        });
    }

    private void uploadImage(final String file, final int i) {


        Calendar calendar = Calendar.getInstance();
        final StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + "");
        mountainsRef.putFile(Uri.fromFile(new File(file)))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;
                        Uri downloadUrl = urlTask.getResult();
                        url_img = downloadUrl.toString();
                        if (i == 0) {
                            if (!url_img.equals("")) {
                                Log.e("5", "5");
                                ModelNote model = new ModelNote("" + Calendar.getInstance().getTimeInMillis(), subject.getText().toString().trim(), date.getText().toString().trim(), note.getText().toString(), url_img, url_audio, starcheck);
                                dialogx.cancel();
                                finish();
                                mDataBase.child(id_User_FB).child("GhiChu").child(model.getId()).setValue(model);
                            }
                        }
                        if (i != 0 && !downloadUrl.toString().equals("")) {
                            Log.e("6", "6");
                            uploadAudio(pathAudio);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e("Failed", e.getMessage());
                        return;
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                    }
                });


    }

    private void uploadAudio(final String file) {


        Calendar calendar = Calendar.getInstance();
        final StorageReference mountainsRef = storageRef.child("audio" + calendar.getTimeInMillis() + "");
        mountainsRef.putFile(Uri.fromFile(new File(file)))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;
                        Uri downloadUrl = urlTask.getResult();
                        url_audio = downloadUrl.toString();
                        Log.e("URL_AUDIO", url_audio);
                        Log.e("7", "7");
                        ModelNote model = new ModelNote("" + Calendar.getInstance().getTimeInMillis(), subject.getText().toString().trim(), date.getText().toString().trim(), note.getText().toString(), url_img, url_audio, starcheck);
                        dialogx.cancel();
                        finish();
                        mDataBase.child(id_User_FB).child("GhiChu").child(model.getId()).setValue(model);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e("Failed", e.getMessage());
                        return;
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                    }
                });


    }


    @Override
    public void run() {
        int currentPosition = mediaPlayer.getCurrentPosition();
        int total = mediaPlayer.getDuration();


        while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mediaPlayer.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }

            seekBar.setProgress(currentPosition);

        }
    }

    public void playSong(Uri uriAudio) {

        try {


            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                clearMediaPlayer();
                seekBar.setProgress(0);
                wasPlaying = true;
                fab.setImageDrawable(ContextCompat.getDrawable(AddNote.this, android.R.drawable.ic_media_play));
            }


            if (!wasPlaying) {

                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                }

                fab.setImageDrawable(ContextCompat.getDrawable(AddNote.this, android.R.drawable.ic_media_pause));


                File path = android.os.Environment.getExternalStorageDirectory();
                mediaPlayer.setDataSource(getApplicationContext(), uriAudio);
                mediaPlayer.prepare();
                mediaPlayer.setVolume(0.5f, 0.5f);
                mediaPlayer.setLooping(false);
                seekBar.setMax(mediaPlayer.getDuration());

                mediaPlayer.start();
                new Thread(this).start();

            }

            wasPlaying = false;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMediaPlayer();
    }

    private void clearMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }



}
