package com.efeemirteke.guvendemiyim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn1,btn2,btn3,btn4;
    ImageView imgRehber;
    EditText etIsim,etNumara;
    String address;
    Double latitute,longitute;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1=findViewById(R.id.btnGuvende);
        btn2=findViewById(R.id.btnGuvendeDegil);
        btn3=findViewById(R.id.btnVeriTabani);
        btn4=findViewById(R.id.saveNumberButton);
        etIsim=findViewById(R.id.etKisiIsim);
        etNumara=findViewById(R.id.etKisiNumara);
        imgRehber=findViewById(R.id.imgRehber);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //İznin olmaması durumu
                if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    //Kullanıcıdan konum iznine ulaşmak için izin alıyoruz.
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }
                //İzin varsa dataBase methodunda yapılan işlerini buttonumuzun basılma durumunda kullanabiliriz.
                else{
                    //Sms izin kontrolü
                    if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},2);
                    }
                    //Sms iznimiz de varsa artık kullanıcıya mesaj gönderebiliriz.
                    else{
                        Task<Location> lastLocation=fusedLocationProviderClient.getLastLocation();
                        //Listener kullanarak location için gereken verileri alıyoruz.
                        lastLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                //Latitute veya Longitute verilerinin alınamaması durumları için try&catch yapısını kullandık.
                                try{
                                    //Yukarıda verilen location nesnesini kullanarak latitute(enlem) ve longitute(boylam) verilerine ulaşıyoruz.
                                    latitute=location.getLatitude();
                                    longitute=location.getLongitude();
                                    //Geocoder'in tanımlanması
                                    Geocoder geocoder= new Geocoder(MainActivity.this);
                                    //Almış olduğumuz latitute ve longitute değerlerini kullanarak kullanıcının adresine ulaşıyoruz.
                                    userLocation=geocoder.getFromLocation(latitute,longitute,1);
                                    address=userLocation.get(0).getAddressLine(0);
                                    //Veri Tabanı yoksa locationDb adında veri tabanı oluşturuyoruz varsa o veri tabanına ulaşıyoruz.
                                    SQLiteDatabase dataBase=openOrCreateDatabase("locationDb", Context.MODE_PRIVATE,null);
                                    //Sql sorgusu yazarak oluşturduğumuz veri tabanına location adında bir tablo ekliyoruz ve tablonun içinde bulunmasını istediğimiz kolonları yazıyorız.
                                    dataBase.execSQL("CREATE TABLE IF NOT EXISTS location (id INTEGER PRIMARY KEY AUTOINCREMENT,enlem VARCHAR,boylam VARCHAR,adres VARCHAR)");
                                    //Aşağıda insertInto nesnesi rawQuery olarak da yazabilirdik fakat binding(bağlama) yaparak kullanmak daha sağlıklı olacaktır.
                                    String insertInto="INSERT INTO location (enlem,boylam,adres) VALUES(?,?,?)";
                                    SQLiteStatement statment=dataBase.compileStatement(insertInto);
                                    statment.bindString(1,latitute.toString());
                                    statment.bindString(2,longitute.toString());
                                    statment.bindString(3,address);
                                    statment.execute();
                                    SmsManager  smsManager =SmsManager.getDefault();
                                    String mesaj="Güvendeyim Merak Etmeyin Adres : "+address+" Enlem : "+latitute.toString()+" Boylam : "+longitute.toString();
                                    Intent intent=getIntent();
                                    String numara=intent.getStringExtra("numara");
                                    smsManager.sendTextMessage(numara,null,mesaj,null,null);
                                }
                                catch (Exception e){
                                    Toast.makeText(MainActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //İznin olmaması durumu
                if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    //Kullanıcıdan konum iznine ulaşmak için izin alıyoruz.
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }
                //İzin varsa dataBase methodunda yapılan işlerini butonumuzun basılma durumunda kullanabiliriz.
                else{
                    //Sms izin kontrolü
                    if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},2);
                    }
                    //Sms iznimiz de varsa artık kullanıcıya mesaj gönderebiliriz.
                    else{
                        Task<Location> lastLocation=fusedLocationProviderClient.getLastLocation();
                        //Listener kullanarak location için gereken verileri alıyoruz.
                        lastLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                //Latitute veya Longitute verilerinin alınamaması durumları için try&catch yapısını kullandık.
                                try{
                                    //Yukarıda verilen location nesnesini kullanarak latitute(enlem) ve longitute(boylam) verilerine ulaşıyoruz.
                                    latitute=location.getLatitude();
                                    longitute=location.getLongitude();
                                    //Geocoder'in tanımlanması
                                    Geocoder geocoder= new Geocoder(MainActivity.this);
                                    //Almış olduğumuz latitute ve longitute değerlerini kullanarak kullanıcının adresine ulaşıyoruz.
                                    userLocation=geocoder.getFromLocation(latitute,longitute,1);
                                    address=userLocation.get(0).getAddressLine(0);
                                    //Veri Tabanı yoksa locationDb adında veri tabanı oluşturuyoruz varsa o veri tabanına ulaşıyoruz.
                                    SQLiteDatabase dataBase=openOrCreateDatabase("locationDb", Context.MODE_PRIVATE,null);
                                    //Sql sorgusu yazarak oluşturduğumuz veri tabanına location adında bir tablo ekliyoruz ve tablonun içinde bulunmasını istediğimiz kolonları yazıyorız.
                                    dataBase.execSQL("CREATE TABLE IF NOT EXISTS location (id INTEGER PRIMARY KEY AUTOINCREMENT,enlem VARCHAR,boylam VARCHAR,adres VARCHAR)");
                                    //Aşağıda insertInto nesnesi rawQuery olarak da yazabilirdik fakat binding(bağlama) yaparak kullanmak daha sağlıklı olacaktır.
                                    String insertInto="INSERT INTO location (enlem,boylam,adres) VALUES(?,?,?)";
                                    SQLiteStatement statment=dataBase.compileStatement(insertInto);
                                    statment.bindString(1,latitute.toString());
                                    statment.bindString(2,longitute.toString());
                                    statment.bindString(3,address);
                                    statment.execute();
                                    String mesaj="Güvende Değilim Adres : "+address+" Enlem : "+latitute.toString()+" Boylam : "+longitute.toString();
                                    Intent intent=getIntent();
                                    String numara=intent.getStringExtra("numara");
                                    SmsManager smsManager=SmsManager.getDefault();
                                    smsManager.sendTextMessage(numara,null,mesaj,null,null);
                                }
                                catch (Exception e){
                                    Toast.makeText(MainActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
     btn3.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent intent=new Intent(MainActivity.this,database.class);
             startActivity(intent);
         }
     });
     btn4.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            if(TextUtils.isEmpty(etIsim.getText()) || TextUtils.isEmpty(etNumara.getText())){
                Toast.makeText(MainActivity.this, "Alanları Doldurunuz", Toast.LENGTH_SHORT).show();
            }
            else{
                SQLiteDatabase rehberDb=openOrCreateDatabase("rehberDb",MODE_PRIVATE,null);
                rehberDb.execSQL("CREATE TABLE IF NOT EXISTS rehberTable (id INTEGER PRIMARY KEY AUTOINCREMENT,isim VARCHAR,numara VARCHAR)");
                String insertInto="INSERT INTO rehberTable (isim,numara) VALUES (?,?)";
                SQLiteStatement statment=rehberDb.compileStatement(insertInto);
                statment.bindString(1,etIsim.getText().toString());
                statment.bindString(2,etNumara.getText().toString());
                statment.execute();
                Toast.makeText(MainActivity.this, "Kişi Kaydedildi", Toast.LENGTH_SHORT).show();
            }
         }
     });
    imgRehber.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(MainActivity.this,AcilDurumNumara.class);
            startActivity(intent);
        }
    });
    }
}