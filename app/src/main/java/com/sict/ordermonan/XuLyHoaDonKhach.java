package com.sict.ordermonan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sict.ordermonan.Admin.XuLyThanhToan;
import com.sict.ordermonan.adapter.XuLyThanhToanAdapter;
import com.sict.ordermonan.chien.model.BanAn;
import com.sict.ordermonan.chien.model.HoaDon;
import com.sict.ordermonan.chien.model.MonAn;

import java.util.ArrayList;

public class XuLyHoaDonKhach extends AppCompatActivity {
    HoaDon hoaDon;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ArrayList<MonAn> arrayList;
    XuLyThanhToanAdapter adapter;
    MonAn monAn;
    TextView banso,tongtien,khuyenmai;
    Button btn_trove,btn_xoa;
    int tong = 0;
    ArrayList<HoaDon> hoaDons;
    ArrayList<BanAn> banAns;
    BanAn banAn;
    int coi = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xu_ly_hoa_don_khach);

        recyclerView  = (RecyclerView) findViewById(R.id.rcl_xulyhoadon_khach);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        khuyenmai = (TextView) findViewById(R.id.xuly_khuyenmai_khach);
        banso = (TextView) findViewById(R.id.xuly_banso_khach);
        tongtien = (TextView) findViewById(R.id.tongtienthanhtoan_ad_khach);
        btn_trove = (Button) findViewById(R.id.trovechuathanhtoan_khach);

        btn_xoa = (Button) findViewById(R.id.xoadonhang_khach);

        Intent intent = getIntent();
        hoaDon = (HoaDon) intent.getSerializableExtra("HOADON_KHACH");//.

        final String idhoadon = hoaDon.getIdhoadon();

        arrayList = new ArrayList<>();
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("HoaDon");
        myRef.child(idhoadon).child("MonAn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    monAn = data.getValue(MonAn.class);

                    monAn.setId(data.getKey());

                    arrayList.add(monAn);
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    int gia = Integer.parseInt(arrayList.get(i).getGia());
                    tong += gia * arrayList.get(i).getSoluong();

                }
                String httong = String.format("%,d", tong);
                tongtien.setText(httong + " " + "đ");
                adapter = new XuLyThanhToanAdapter(arrayList,getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        banso.setText("Bàn ăn :");

        banAns = new ArrayList<>();
        final DatabaseReference banan = FirebaseDatabase.getInstance().getReference("BanAn");
        banan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    banAn = data.getValue(BanAn.class);

                    banAn.setId(data.getKey());
                    banAns.add(banAn);
                }

                for (int l=0;l<banAns.size();l++){
                    final String idban = banAns.get(l).getId();
                    DatabaseReference refhd = FirebaseDatabase.getInstance().getReference("HoaDon");
                    refhd.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                            if (coi>=1){
                                if (dataSnapshot2.child(hoaDon.getIdhoadon()).child("BanAn").child(idban).exists()) {
                                    banso.setText(banso.getText().toString() +" " + dataSnapshot2.child(hoaDon.getIdhoadon()).child("BanAn").child(idban).child("tenbanan").getValue().toString()+",");
                                }
                            }else
                            if (dataSnapshot2.child(hoaDon.getIdhoadon()).child("BanAn").child(idban).exists()){
                                banso.setText("Bạn đặt bàn  "+dataSnapshot2.child(hoaDon.getIdhoadon()).child("BanAn").child(idban).child("tenbanan").getValue().toString()+",");
                                coi++;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       //Xử lý khuyến mãi
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(idhoadon).child("KhuyenMai").exists()){
                    String makhuyemai = dataSnapshot.child(idhoadon).child("KhuyenMai").child("makm").getValue().toString();
                    String sotiengiam = dataSnapshot.child(idhoadon).child("KhuyenMai").child("sotiengiam").getValue().toString();
                    int stg = Integer.parseInt(sotiengiam);
                    String hienstg = String.format("%,d",stg);
                    tong = tong - stg;
                    khuyenmai.setText("Đơn này đã nhập mã khuyến mãi "+makhuyemai+"( Giảm "+hienstg+"đ )");

                    String httong = String.format("%,d", tong);
                    tongtien.setText(httong + " " + "đ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_trove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(XuLyHoaDonKhach.this);

                builder.setTitle("Đơn có chắc chắn muốn hủy đơn hàng này?");
                builder.setView(R.layout.luachondatban);
                final AlertDialog dialog = builder.create();
                dialog.show();

                Button btn_khong = dialog.findViewById(R.id.btn_khong_dat_ban);
                Button btn_co = dialog.findViewById(R.id.btn_co_datban);

                btn_khong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                btn_co.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //set lại tình trạng bàn ăn
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                banAns = new ArrayList<>();
                                final DatabaseReference refBan = FirebaseDatabase.getInstance().getReference("BanAn");
                                refBan.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot8) {
                                        banAns.clear();
                                        for (DataSnapshot databan : dataSnapshot8.getChildren()){
                                            banAn = databan.getValue(BanAn.class);

                                            banAn.setId(databan.getKey());
                                            banAns.add(banAn);
                                        }
                                        for (int m =0;m<banAns.size();m++){
                                            if (dataSnapshot.child(idhoadon).child("BanAn").child(banAns.get(m).getId()).exists()) {
                                                final String idban = dataSnapshot.child(idhoadon).child("BanAn").child(banAns.get(m).getId()).child("id").getValue().toString();

                                                refBan.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.child(idban).exists()) {
                                                            refBan.child(idban).child("tinhtrang").setValue(false);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        myRef.child(idhoadon).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(XuLyHoaDonKhach.this,"Bạn đã hủy đơn hàng này thành công",Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                                finish();
                            }
                        });
                    }
                });

            }
        });
    }
}
