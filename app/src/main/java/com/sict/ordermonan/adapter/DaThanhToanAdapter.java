package com.sict.ordermonan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sict.ordermonan.DaThanhToanThanhCong;
import com.sict.ordermonan.R;
import com.sict.ordermonan.chien.model.HoaDon;

import java.util.ArrayList;

public class DaThanhToanAdapter extends RecyclerView.Adapter<DaThanhToanAdapter.ViewHolder> {
    ArrayList<HoaDon> hoaDons;
    Context context;

    public DaThanhToanAdapter(ArrayList<HoaDon> hoaDons, Context context) {
        this.hoaDons = hoaDons;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_rcl_dathanhtoan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.ngay.setText(hoaDons.get(position).getNgay());
        holder.thoigian.setText(hoaDons.get(position).getThoigian());
        holder.email.setText(hoaDons.get(position).getEmail());
        holder.ngayhoanthanh.setText(hoaDons.get(position).getThanhtoanngay());
        holder.thoigianhoanthanh.setText(hoaDons.get(position).getThanhtoanthoigian());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DaThanhToanThanhCong.class);
                intent.putExtra("DATHANHTOAN",hoaDons.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hoaDons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ngay,thoigian,email,ngayhoanthanh,thoigianhoanthanh;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ngay = (TextView) itemView.findViewById(R.id.ngayhoadon_d);
            thoigian = (TextView) itemView.findViewById(R.id.thoigianhoadon_d);
            email = (TextView) itemView.findViewById(R.id.tenkhach_d);
            ngayhoanthanh = (TextView) itemView.findViewById(R.id.ngayhoadon_d_t);
            thoigianhoanthanh = (TextView) itemView.findViewById(R.id.thoigianhoadon_d_t);
        }
    }
}
