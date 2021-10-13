package com.sict.ordermonan;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class DaThanhToanThanhCong extends AppCompatActivity {
    HoaDon hoaDon;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ArrayList<MonAn> arrayList;
    XuLyThanhToanAdapter adapter;
    MonAn monAn;
    TextView banso,tongtien,khuyenmai;
    Button btn_trove;
    int tong = 0;
    Toolbar toolbar;
    ArrayList<HoaDon> hoaDons;
    ArrayList<BanAn> banAns;
    BanAn banAn;
    int coi =0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da_thanh_toan_thanh_cong);

        recyclerView  = (RecyclerView) findViewById(R.id.rcl_xulyhoadon_tc);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        banso = (TextView) findViewById(R.id.xuly_banso_tc);
        tongtien = (TextView) findViewById(R.id.tongtienthanhtoan_tc);
        btn_trove = (Button) findViewById(R.id.trovedathanhtoan_tc);
        khuyenmai = (TextView) findViewById(R.id.xuly_khuyenmai_tc);
        toolbar = (Toolbar) findViewById(R.id.toolbar_xulythanhtoan_thanhcong);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Hóa đơn");

        Intent intent = getIntent();
        hoaDon = (HoaDon) intent.getSerializableExtra("DATHANHTOAN");

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
                                banso.setText("Đặt bàn "+dataSnapshot2.child(hoaDon.getIdhoadon()).child("BanAn").child(idban).child("tenbanan").getValue().toString()+",");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DaThanhToanThanhCong.this);

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
