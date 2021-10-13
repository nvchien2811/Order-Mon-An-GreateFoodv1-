package com.sict.ordermonan.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sict.ordermonan.R;
import com.sict.ordermonan.adapter.MoAnAdapter;
import com.sict.ordermonan.adapter.XuLyThanhToanAdapter;
import com.sict.ordermonan.chien.model.BanAn;
import com.sict.ordermonan.chien.model.HoaDon;
import com.sict.ordermonan.chien.model.MonAn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.zip.Inflater;

public class XuLyThanhToan extends AppCompatActivity {
     HoaDon hoaDon;
     RecyclerView recyclerView;
     LinearLayoutManager layoutManager;
     ArrayList<MonAn> arrayList;
     XuLyThanhToanAdapter adapter;
     MonAn monAn;
     TextView banso,tongtien,khuyenmai;
     int tong = 0;
     Button btn_trove,btn_xacnhan,btn_xoa;
     String saveDate,saveTime;
     BanAn banAn;
     Toolbar toolbar;
     ArrayList<HoaDon> hoaDons;
     ArrayList<BanAn> banAns;
    int coi = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xu_ly_thanh_toan);

        recyclerView  = (RecyclerView) findViewById(R.id.rcl_xulyhoadon);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        banso = (TextView) findViewById(R.id.xuly_banso);
        khuyenmai = (TextView) findViewById(R.id.xuly_khuyenmai);
        tongtien = (TextView) findViewById(R.id.tongtienthanhtoan_ad);
        btn_trove = (Button) findViewById(R.id.trovechuathanhtoan);
        btn_xacnhan = (Button) findViewById(R.id.xacnhanthanhtoan);
        btn_xoa = (Button) findViewById(R.id.xoadonhang);


        toolbar = (Toolbar) findViewById(R.id.toolbar_xulythanhtoan);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Hóa đơn");
        Intent intent = getIntent();
        hoaDon = (HoaDon) intent.getSerializableExtra("HOADON");//.

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
                                banso.setText("Khách đặt bàn  "+dataSnapshot2.child(hoaDon.getIdhoadon()).child("BanAn").child(idban).child("tenbanan").getValue().toString()+",");
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
        btn_xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(XuLyThanhToan.this);

                builder.setTitle("Đơn hàng này đã hoàn thành thanh toán có phải không?");
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
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                        saveDate = dateFormat.format(calendar.getTime());

                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
                        saveTime = timeFormat.format(calendar.getTime());

                        HashMap<String,Object> hoanthanh =new HashMap<>();
                        hoanthanh.put("thanhtoanngay",saveDate);
                        hoanthanh.put("thanhtoanthoigian",saveTime);

                        myRef.child(idhoadon).updateChildren(hoanthanh);
                        myRef.child(idhoadon).child("tinhtrang").setValue(true);

                        //set lại tình trạng bàn ăn
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                banAns = new ArrayList<>();
                                final DatabaseReference refBan = FirebaseDatabase.getInstance().getReference("BanAn");
                                refBan.addValueEventListener(new ValueEventListener() {
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

                                                refBan.addValueEventListener(new ValueEventListener() {
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

                        Toast.makeText(XuLyThanhToan.this,"Đơn hàng đã hoàn thành",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        });
        btn_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(XuLyThanhToan.this);

                builder.setTitle("Bạn có chắc chắn muốn xóa đơn hàng này");
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
                                refBan.addValueEventListener(new ValueEventListener() {
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

                                                refBan.addValueEventListener(new ValueEventListener() {
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
                                Toast.makeText(XuLyThanhToan.this,"Bạn đã xóa đơn hàng này thành công",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }
                });




            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.thongtinkhach,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.thongtinkhachdat:
                Toast.makeText(this, "Thông tin khách", Toast.LENGTH_SHORT).show();
                thongtinkhachdat();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void thongtinkhachdat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(XuLyThanhToan.this);

        builder.setView(R.layout.activity_thong_tin_ca_nhan);

        final AlertDialog dialog = builder.create();

        dialog.show();
        Button capnhat = (Button) dialog.findViewById(R.id.btn_capnhatcanhan);
        Button trove = (Button) dialog.findViewById(R.id.btn_trove_tt);
        TextView txt= (TextView) dialog.findViewById(R.id.txt_convert_thongtincanhan);
        final EditText edt_ten = (EditText) dialog.findViewById(R.id.edt_ten_u);
        final EditText edt_sdt = (EditText) dialog.findViewById(R.id.edt_sdt_u);
        final EditText edt_diachi = (EditText) dialog.findViewById(R.id.edt_diachi_u);
        capnhat.setVisibility(View.INVISIBLE);
        txt.setText("Thông tin cá nhân khách hàng");
        trove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        final String idkhachhang = hoaDon.getUid();

        DatabaseReference refU = FirebaseDatabase.getInstance().getReference("Users");
        refU.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(idkhachhang).exists()){
                    edt_ten.setText(dataSnapshot.child(idkhachhang).child("ten").getValue().toString());
                    edt_diachi.setText(dataSnapshot.child(idkhachhang).child("diachi").getValue().toString());
                    edt_sdt.setText(dataSnapshot.child(idkhachhang).child("sdt").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
