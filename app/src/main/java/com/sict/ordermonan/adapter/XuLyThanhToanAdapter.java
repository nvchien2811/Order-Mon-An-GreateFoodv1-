package com.sict.ordermonan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sict.ordermonan.R;
import com.sict.ordermonan.chien.model.MonAn;

import java.util.ArrayList;

public class XuLyThanhToanAdapter extends RecyclerView.Adapter<XuLyThanhToanAdapter.ViewHolder> {
    ArrayList<MonAn> monAns;
    Context context;

    public XuLyThanhToanAdapter(ArrayList<MonAn> monAns, Context context) {
        this.monAns = monAns;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_rcl_xulythanhtoan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         holder.tenmonan.setText(monAns.get(position).getTen());
         String soluong = String.valueOf(monAns.get(position).getSoluong());
         holder.soluong.setText("số lượng "+"x"+" "+soluong);
         String gia =  monAns.get(position).getGia();
         int i = Integer.parseInt(gia);
         int g = i*monAns.get(position).getSoluong();
         String str = String.format("%,d",g);
         holder.gia.setText(str+" "+"đ");
    }

    @Override
    public int getItemCount() {
        return monAns.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenmonan,soluong,gia;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenmonan = (TextView) itemView.findViewById(R.id.tenmonan_xuly);
            soluong = (TextView) itemView.findViewById(R.id.soluong_xuly);
            gia = (TextView) itemView.findViewById(R.id.gia_xuly);

        }
    }
}
