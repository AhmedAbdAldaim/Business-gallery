package com.example.businesgalleryadmin.Ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.businesgalleryadmin.Model.LoveandCommentsModel2;

import com.example.businesgalleryadmin.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Total_love_adapter extends RecyclerView.Adapter<Total_love_adapter.ViewHolder>  {
    private Context context;
    private List<LoveandCommentsModel2> list;


    public Total_love_adapter(List<LoveandCommentsModel2> loveModel2s, Context context) {
        this.context = context;
        this.list=loveModel2s;
    }

    @NonNull
    @Override
    public Total_love_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_bottom_sheet_love,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Total_love_adapter.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{


        public ViewHolder(@NonNull View itemView) {
             super(itemView);
        }
    }
}
