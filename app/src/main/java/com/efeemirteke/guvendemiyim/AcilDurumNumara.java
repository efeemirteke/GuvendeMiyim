package com.efeemirteke.guvendemiyim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;

import java.util.ArrayList;

public class AcilDurumNumara extends AppCompatActivity {
    ArrayList<acildurumarray> acilDurum;
    String isim,numara,id;
    RecyclerView rvAcilDurum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acil_durum_numara);
        rvAcilDurum=findViewById(R.id.rvAcilDurum);
        acilDurum=new ArrayList<acildurumarray>();
        SQLiteDatabase rehberDb=openOrCreateDatabase("rehberDb",MODE_PRIVATE,null);
        try{
            Cursor cursor=rehberDb.rawQuery("SELECT * FROM rehberTable",null);
            Integer isimIndx=cursor.getColumnIndex("isim");
            Integer numaraIndx=cursor.getColumnIndex("numara");
            Integer idIndx=cursor.getColumnIndex("id");
            while (cursor.moveToNext()){
                isim=cursor.getString(isimIndx);
                numara=cursor.getString(numaraIndx);
                id=cursor.getString(idIndx);
                acildurumarray myarray=new acildurumarray(isim,numara,id);
                acilDurum.add(myarray);
            }
            cursor.close();
        }
        catch (Exception e){

        }
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvAcilDurum.setLayoutManager(layoutManager);
        acildurumadapter rw=new acildurumadapter(acilDurum,this);
        rvAcilDurum.setAdapter(rw);
    }
}