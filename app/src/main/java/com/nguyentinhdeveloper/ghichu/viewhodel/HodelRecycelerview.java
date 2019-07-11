package com.nguyentinhdeveloper.ghichu.viewhodel;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nguyentinhdeveloper.ghichu.R;


public class HodelRecycelerview extends RecyclerView.ViewHolder {
    public TextView stt;
    public TextView subject;
    public TextView txtDate;
    public ImageView danhdau;
    public ImageView menu;
    public TextView txtNote;
    public ImageView img;
    public RelativeLayout layout;
    public ProgressBar progressBar;
    public RelativeLayout relativeLayout;
    public SeekBar seekbar;
    public TextView textView,sumtextView;
    public ImageView button;








    public HodelRecycelerview(@NonNull View itemView) {
        super(itemView);
        stt = itemView.findViewById(R.id.stt);
        subject =  itemView.findViewById(R.id.subject);
        txtDate = itemView.findViewById(R.id.txtDate);
        danhdau = itemView.findViewById(R.id.danhdau);
        menu =  itemView.findViewById(R.id.menu);
        txtNote =  itemView.findViewById(R.id.txtNote);
        img =  itemView.findViewById(R.id.img);
        layout =  itemView.findViewById(R.id.idUser);
        relativeLayout =  itemView.findViewById(R.id.layout);
        progressBar=itemView.findViewById(R.id.aaa);
        seekbar = (SeekBar) itemView.findViewById(R.id.seekbar);
        textView = (TextView) itemView.findViewById(R.id.textView);
        sumtextView = (TextView) itemView.findViewById(R.id.sumtextView);
        button = (ImageView) itemView.findViewById(R.id.button);
    }
}
