package com.efeemirteke.guvendemiyim;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.service.controls.actions.ModeAction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Visibility;

import java.util.ArrayList;

public class acildurumadapter extends RecyclerView.Adapter<acildurumadapter.myViewHolder> {
    ArrayList<acildurumarray> acildurumarray;
    Context context;

    public acildurumadapter(ArrayList<acildurumarray> acildurumarray, Context context) {
        this.acildurumarray = acildurumarray;
        this.context = context;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.acildurumlayout,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        holder.setData(position);
    }
    @Override
    public int getItemCount() {
        return acildurumarray.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView isim;
        TextView numara;
        ImageView imgSms,imgRemove,imgCall;
        public myViewHolder(View itemView) {
            super(itemView);
            this.isim=(TextView) itemView.findViewById(R.id.tvKisiIsim);
            this.numara=(TextView) itemView.findViewById(R.id.tvKisiNumara);
            this.imgCall=(ImageView) itemView.findViewById(R.id.imgCall);
            this.imgSms=(ImageView) itemView.findViewById(R.id.imgSms);
            this.imgRemove=(ImageView) itemView.findViewById(R.id.imgRemove);
        }
        public void setData(Integer position){
            isim.setText(acildurumarray.get(position).isim);
            numara.setText(acildurumarray.get(position).numara);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,MainActivity.class);
                    intent.putExtra("numara",acildurumarray.get(position).numara);
                    context.startActivity(intent);
                    Toast.makeText(context,acildurumarray.get(position).isim+" Kişisi Seçildi", Toast.LENGTH_SHORT).show();
                }
            });
            imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+acildurumarray.get(position).numara));
                    context.startActivity(intent);
                }
            });
            imgSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setData(Uri.parse("sms:"+acildurumarray.get(position).numara));
                    context.startActivity(intent);
                }
            });
            imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SQLiteDatabase veritabani=context.openOrCreateDatabase("rehberDb",Context.MODE_PRIVATE,null);
                    veritabani.delete("rehberTable","id="+acildurumarray.get(position).id,null);
                    acildurumarray.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,acildurumarray.size());
                }
            });

        }
        @Override
        public void onClick(View view) {

        }
    }
}
