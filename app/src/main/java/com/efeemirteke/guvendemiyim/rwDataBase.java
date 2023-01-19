package com.efeemirteke.guvendemiyim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class rwDataBase extends RecyclerView.Adapter<rwDataBase.myViewHolder>{
    ArrayList<dbArray> arrayList;
    Context context;

    public rwDataBase(ArrayList<dbArray> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.databaselayout,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder( myViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView latitute,longitute,address;
        public myViewHolder(View itemView) {
            super(itemView);
            this.latitute=(TextView) itemView.findViewById(R.id.tvEnlem);
            this.longitute=(TextView) itemView.findViewById(R.id.tvBoylam);
            this.address=(TextView) itemView.findViewById(R.id.tvAddress);
        }
        public void setData(Integer position){
            latitute.setText("Enlem : "+arrayList.get(position).latitute);
            longitute.setText("Boylam : "+arrayList.get(position).longitute);
            address.setText("Adres : "+arrayList.get(position).address);
        }
        @Override
        public void onClick(View view) {

        }
    }
}
