package com.sict.ordermonan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sict.ordermonan.Admin.XuLyThanhToan;
import com.sict.ordermonan.R;
import com.sict.ordermonan.chien.model.HoaDon;

import java.util.ArrayList;

public class ChuaThanhToanAdapter extends RecyclerView.Adapter<ChuaThanhToanAdapter.ViewHolder> {
    ArrayList<HoaDon> hoaDons;
    Context context;

    public ChuaThanhToanAdapter(ArrayList<HoaDon> hoaDons, Context context) {
        this.hoaDons = hoaDons;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_rcl_chuathanhtoan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
      holder.ngay.setText(hoaDons.get(position).getNgay());
      if (hoaDons.get(position).getThoigian()==null){
          holder.thoigian.setText("Đơn hàng đặt bàn");
      }
      else if (hoaDons.get(position).getThoigian()!=null) {
          holder.thoigian.setText(hoaDons.get(position).getThoigian());
      }
      holder.email.setText(hoaDons.get(position).getEmail());

      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(context, XuLyThanhToan.class);
              intent.putExtra("HOADON",hoaDons.get(position));
              context.startActivity(intent);
          }
      });
    }

    @Override
    public int getItemCount() {
        return hoaDons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ngay,thoigian,email;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ngay = (TextView) itemView.findViewById(R.id.ngayhoadon);
            thoigian = (TextView) itemView.findViewById(R.id.thoigianhoadon);
            email = (TextView) itemView.findViewById(R.id.tenkhach);
        }
    }
}
