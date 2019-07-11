package com.nguyentinhdeveloper.ghichu;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import com.nguyentinhdeveloper.ghichu.database.BaseData;
import com.nguyentinhdeveloper.ghichu.model.ModelNote;

import java.util.ArrayList;

public class GetDataSQLite {
    protected ArrayList<ModelNote> GetData(Context context){

        ArrayList<ModelNote> list=new ArrayList<>();

        list.clear();

        BaseData db=new BaseData(context);

        Cursor cursor=db.getData();

        if (cursor!=null && cursor.moveToFirst()){
            do {
                ModelNote model=new ModelNote(cursor.getInt(0)+"",cursor.getString(5),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(6),cursor.getString(7));
                list.add(0,model);
            }while (cursor.moveToNext());
            Log.e("SIZE",list.size()+"");
        }
        return list;
    }
    protected ArrayList<String> getI_D(Context context){
        ArrayList<String> listID=new ArrayList<>();
        listID.clear();
        BaseData db=new BaseData(context);

        Cursor cursor=db.getDataa();

        if (cursor!=null && cursor.moveToFirst()){
            do {
                listID.add(0, cursor.getString(1));
            }while (cursor.moveToNext());

        }
        return listID;
    }
}
