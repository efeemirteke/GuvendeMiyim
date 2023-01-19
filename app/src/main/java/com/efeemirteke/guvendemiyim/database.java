package com.efeemirteke.guvendemiyim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;

public class database extends AppCompatActivity {
    ArrayList<dbArray> myArrayList;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        recyclerView=findViewById(R.id.rwDataBase);
        myArrayList=new ArrayList<dbArray>();
        SQLiteDatabase database=this.openOrCreateDatabase("locationDb",MODE_PRIVATE,null);
        Cursor cursor=database.rawQuery("SELECT * FROM location",null);
        int latituteIndex=cursor.getColumnIndex("enlem");
        int longituteIndex=cursor.getColumnIndex("boylam");
        int addressIndex=cursor.getColumnIndex("adres");
            while(cursor.moveToNext()){
                String latitute=cursor.getString(latituteIndex);
                String longitute=cursor.getString(longituteIndex);
                String address=cursor.getString(addressIndex);
                dbArray dbarray= new dbArray(latitute,longitute,address);
                myArrayList.add(dbarray);
            }
        cursor.close();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        rwDataBase rw=new rwDataBase(myArrayList,this);
        recyclerView.setAdapter(rw);
    }
}