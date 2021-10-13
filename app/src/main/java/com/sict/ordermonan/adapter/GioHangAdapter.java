package com.sict.ordermonan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sict.ordermonan.CartFragment;
import com.sict.ordermonan.R;
import com.sict.ordermonan.chien.model.HoaDon;
import com.sict.ordermonan.chien.model.MonAn;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHolder> {
    ArrayList<MonAn> monAns;
    Context context;
    FirebaseAuth mAuth;
    ArrayList<HoaDon> hoaDons;
    HoaDon hoaDon;
    public GioHangAdapter(ArrayList<MonAn> monAns, Context context) {
        this.monAns = monAns;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_rcl_giohang,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if (monAns.get(position).getId().equals("KhuyenMai")==true){

        }
        holder.giamsoluong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                final String uid = user.getUid();
                final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("GioHang");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(uid).child("MonAn").child(monAns.get(position).getId()).exists()){
                            String stsoluong = dataSnapshot.child(uid).child("MonAn").child(monAns.get(position).getId()).child("soluong").getValue().toString();
                            int sl = Integer.parseInt(stsoluong);

                            if (sl==1){
                                Toast.makeText(context,"Nút xóa ở bên phải",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            int kq = sl - 1;

                            myRef.child(uid).child("MonAn").child(monAns.get(position).getId()).child("soluong").setValue(kq);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        holder.tangsoluong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                final String uid = user.getUid();
                final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("GioHang");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(uid).child("MonAn").child(monAns.get(position).getId()).exists()){
                            String stsoluong = dataSnapshot.child(uid).child("MonAn").child(monAns.get(position).getId()).child("soluong").getValue().toString();
                            int sl = Integer.parseInt(stsoluong);

                            int kq = sl + 1;

                            myRef.child(uid).child("MonAn").child(monAns.get(position).getId()).child("soluong").setValue(kq);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        String gia =  monAns.get(position).getGia();

        int i = Integer.parseInt(gia);
        int ss = monAns.get(position).getSoluong();
        int tong = i * ss;
        String tongss = String.format("%,d",tong);


        String htsl =String.valueOf(ss);
        Picasso.get().load(monAns.get(position).getHinhanh()).into(holder.anhmonan);
        holder.tenmonan.setText(monAns.get(position).getTen());

        holder.giamonan.setText(tongss+" "+"đ");
        holder.soluong.setText(htsl);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();
                String id = monAns.get(position).getId();
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("GioHang");

                myRef.child(uid).child("MonAn").child(id).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        Toast.makeText(context,"Xóa món ăn khỏi giỏ hàng thành công",Toast.LENGTH_SHORT).show();

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
        ImageView anhmonan,delete;
        TextView tenmonan,giamonan,soluong,dadatbanhaychua;
        ImageButton giamsoluong,tangsoluong;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            anhmonan = (ImageView) itemView.findViewById(R.id.anh_monan_g);
            tenmonan = (TextView) itemView.findViewById(R.id.ten_monan_g);
            giamonan = (TextView) itemView.findViewById(R.id.gia_monan_g);
            soluong = (TextView) itemView.findViewById(R.id.soluongcuamonan);
            delete = (ImageView) itemView.findViewById(R.id.delete_item_giohang);
            giamsoluong = (ImageButton) itemView.findViewById(R.id.giamsoluong);
            tangsoluong = (ImageButton) itemView.findViewById(R.id.tangsoluong);
        }
    }
}
