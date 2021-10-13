package com.sict.ordermonan.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sict.ordermonan.Admin.SuaMonAn;
import com.sict.ordermonan.Admin.XoaSuaMonAn;
import com.sict.ordermonan.Admin_QuanLy;
import com.sict.ordermonan.R;
import com.sict.ordermonan.chien.model.MonAn;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class XoaSuaMonAnAdapter extends RecyclerView.Adapter<XoaSuaMonAnAdapter.ViewHolder> {
    ArrayList<MonAn> monAns;
    Context context;


    public XoaSuaMonAnAdapter(ArrayList<MonAn> monAns, Context context) {
        this.monAns = monAns;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_rcl_suaxoa_monan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.ten_monan.setText( monAns.get(position).getTen());
        String gia =  monAns.get(position).getGia();
        int i = Integer.parseInt(gia);
        String str = String.format("%,d",i);
        holder.gia_monan.setText(str +" "+"đ");
        try {
            Picasso.get().load(monAns.get(position).getHinhanh()).into(holder.anh_monan);
        }catch (Exception e){
        }
        holder.ratingBar.setRating(monAns.get(position).getRating());
        holder.btn_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("MonAn");
                myRef.child( monAns.get(position).getId()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        Toast.makeText(context,"Xóa món ăn thành công",Toast.LENGTH_SHORT).show();

                        notifyDataSetChanged();

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return monAns.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView anh_monan;
        TextView ten_monan,gia_monan;
        Button btn_sua,btn_xoa;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            anh_monan = (ImageView) itemView.findViewById(R.id.anh_monan_x);
            ten_monan = (TextView) itemView.findViewById(R.id.ten_monan_x);
            gia_monan = (TextView) itemView.findViewById(R.id.gia_monan_x);
            btn_sua = (Button) itemView.findViewById(R.id.sua_monan);
            btn_xoa = (Button) itemView.findViewById(R.id.xoa_monan);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar_x);

            btn_sua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SuaMonAn.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("SUAMONAN",monAns.get(getPosition()));
                    context.startActivity(intent);
                    notifyDataSetChanged();
                }
            });

        }
    }
}
