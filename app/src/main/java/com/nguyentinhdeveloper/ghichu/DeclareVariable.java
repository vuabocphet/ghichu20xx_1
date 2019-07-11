package com.nguyentinhdeveloper.ghichu;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nguyentinhdeveloper.ghichu.database.BaseData;
import com.nguyentinhdeveloper.ghichu.model.ModelNote;


import java.util.ArrayList;

public class DeclareVariable extends AppCompatActivity {
    public ImageView imageView;
    public LinearLayout linearLayout;
    public TextView good;
    public TextView loichuc;
    public String good_i = "";
    public View layout_view;
    public AdapterRecyclerview adapterRecyclerview;
    public LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    public RecyclerView recyclerView;
    public ArrayList<ModelNote> list = new ArrayList<>();
    public DatabaseReference mDataBase;
    public FirebaseStorage storage;
    public StorageReference storageRef;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public String img_User_FB="";
    public String name_User_FB="";
    public String id_User_FB="";
    public DrawerLayout drawerLayout;
    public ImageView startMenu,add_a,closeIMG,closeAudio;
    public NavigationView navigationView;
    public BaseData baseData;
    public TextView sNote;


}
