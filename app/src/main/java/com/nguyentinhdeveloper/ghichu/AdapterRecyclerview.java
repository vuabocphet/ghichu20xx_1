package com.nguyentinhdeveloper.ghichu;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.nguyentinhdeveloper.ghichu.model.ModelNote;
import com.nguyentinhdeveloper.ghichu.viewhodel.HodelRecycelerview;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AdapterRecyclerview extends RecyclerView.Adapter<HodelRecycelerview> {
    private HomeNote context;
    private ArrayList<ModelNote> list;
    private String img_User_FB;
    private boolean wasPlaying = false;
    private HodelRecycelerview hodelRecycelerview;

    public AdapterRecyclerview(HomeNote context, ArrayList<ModelNote> list, String img_User_FB) {
        this.context = context;
        this.list = list;
        this.img_User_FB = img_User_FB;
    }

    @NonNull
    @Override
    public HodelRecycelerview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HodelRecycelerview(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_recyclerview_style, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final HodelRecycelerview holder, int position) {

        final ModelNote note = list.get(position);
        hodelRecycelerview = holder;

        holder.stt.setText(String.valueOf(position + 1));
        if (getHH() >= 6 && getHH() < 12) {
            //holder.layout.setBackground(context.getResources().getDrawable(R.drawable.style_toolbar_c));
            holder.progressBar.setBackground(context.getResources().getDrawable(R.drawable.custom_gradient_binhminh));
            holder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.custom_gradient_binhminh));
        }
        if (getHH() >= 12 && getHH() < 18) {

            // holder.layout.setBackground(context.getResources().getDrawable(R.drawable.style_toolbar_d));
            holder.progressBar.setBackground(context.getResources().getDrawable(R.drawable.custom_gradient_hoanghon));
            holder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.custom_gradient_hoanghon));
        }
        if (getHH() >= 18 || getHH() < 6) {

            //holder.layout.setBackground(context.getResources().getDrawable(R.drawable.style_toolbar_e));
            holder.progressBar.setBackground(context.getResources().getDrawable(R.drawable.custom_gradient_a));
            holder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.custom_gradient_a));
        }

        holder.subject.setText(note.getSubject());
        holder.txtDate.setText(note.getDate());
        holder.txtNote.setText(note.getNode());


        if (note.getStar().equals("no")) {
            holder.danhdau.setVisibility(View.GONE);
        } else {
            holder.danhdau.setVisibility(View.VISIBLE);
        }

        if (note.getImg().isEmpty() || note.getImg().equals("") || !note.getImg().startsWith("http")) {
            holder.img.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
        } else {
            holder.img.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(note.getImg()).fit().transform(boderIMG(0, 8))
                    .centerCrop()
                    .error(R.drawable.noimg).into(holder.img, new Callback() {
                @Override
                public void onSuccess() {
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {

                }
            });


        }
        if (note.getAudio() != null) {
            if (!note.getAudio().equals("")) {
                holder.button.setVisibility(View.VISIBLE);
                holder.textView.setVisibility(View.GONE);
                holder.sumtextView.setVisibility(View.GONE);
                holder.seekbar.setVisibility(View.VISIBLE);
            }

        } else {
            holder.button.setVisibility(View.GONE);
            holder.sumtextView.setVisibility(View.GONE);
            holder.textView.setVisibility(View.GONE);
            holder.seekbar.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        if (list.isEmpty()) return 0;
        else return list.size();
    }

    private Transformation boderIMG(int boderW, int boderConer) {
        return new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(boderW)
                .cornerRadiusDp(boderConer)
                .oval(false)
                .build();

    }

    public void deleteItem(int i, String sụbect) {
        context.delete(list.get(i).getId(), sụbect, i);
    }

    public void deleteItems(int i) {
        list.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, getItemCount());
    }


    private Integer getHH() {
        Date date = new Date();
        String strDateFormat24 = "HH";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat24);
        return Integer.parseInt(sdf.format(date));
    }




   
}

