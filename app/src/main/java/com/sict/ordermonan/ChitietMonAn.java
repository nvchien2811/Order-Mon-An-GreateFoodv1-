package com.sict.ordermonan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.sict.ordermonan.chien.model.MonAn;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;

public class ChitietMonAn extends AppCompatActivity {
    ImageView anhmonan;
    TextView motamonan,tenmonan,giamonan;
    Button btn_add_cart;
    EditText edt_soluong;
    MonAn  monAn;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    RatingBar ratingBar;
    float myRating = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_mon_an);


        anhmonan = (ImageView) findViewById(R.id.anh_monan);
        motamonan = (TextView) findViewById(R.id.motamonan);
        tenmonan = (TextView) findViewById(R.id.ten_monan_c);
        giamonan = (TextView) findViewById(R.id.gia_monan_c);

        toolbar = (Toolbar) findViewById(R.id.toolbar3);
        btn_add_cart = (Button) findViewById(R.id.btn_add_cart);
        edt_soluong = (EditText) findViewById(R.id.edt_solhuong);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar_c);

        btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatHang();
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("GREAT FOOD");
        Intent intent = getIntent();
        monAn = (MonAn)intent.getSerializableExtra("MONAN");//.
        if(monAn != null){
            Picasso.get().load(monAn.getHinhanh()).into(anhmonan);
            motamonan.setText(monAn.getMota());
            tenmonan.setText(monAn.getTen());
            String gia = monAn.getGia();
            int i = Integer.parseInt(gia);
            String str = String.format("%,d",i);

            giamonan.setText(str+" "+"đ");
        }else {
            Toast.makeText(this,"Có lỗi khi load dữ liệu",Toast.LENGTH_SHORT).show();
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference Refdanhgia = database.getReference("DanhGia");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        final String uid=currentUser.getUid();


        Refdanhgia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.child(monAn.getId()).child(uid).child("rating").exists()){


                   String a = dataSnapshot.child(monAn.getId()).child(uid).child("rating").getValue().toString();

                   float b =Float.parseFloat(a);

                   ratingBar.setRating(b);

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                float ran = (float) rating;
                myRating = (float) ratingBar.getRating();

                String mess = null;

                if (ran == 1) {
                    Refdanhgia.child(monAn.getId()).child(uid).child("rating").setValue( 1);
                }
                else  if(ran == 1.5){

                    Refdanhgia.child(monAn.getId()).child(uid).child("rating").setValue(1.5);
                }
                else if (ran == 2) {

                    Refdanhgia.child(monAn.getId()).child(uid).child("rating").setValue(2);
                }
                else if (ran == 2.5) {

                    Refdanhgia.child(monAn.getId()).child(uid).child("rating").setValue(2.5);
                }else if (ran == 3) {

                    Refdanhgia.child(monAn.getId()).child(uid).child("rating").setValue(3);
                }
                else if (ran == 3.5) {

                    Refdanhgia.child(monAn.getId()).child(uid).child("rating").setValue(3.5);
                }  else if (ran == 4) {

                    Refdanhgia.child(monAn.getId()).child(uid).child("rating").setValue(4);
                }else if (ran == 4.5) {

                    Refdanhgia.child(monAn.getId()).child(uid).child("rating").setValue(4.5);
                }else if (ran == 5) {

                    Refdanhgia.child(monAn.getId()).child(uid).child("rating").setValue(5);
                }

                Toast.makeText(ChitietMonAn.this,"Cảm ơn bạn đã đánh giá",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DatHang() {
        String soluong = edt_soluong.getText().toString();
        if(TextUtils.isEmpty(soluong)){
            Toast.makeText(ChitietMonAn.this,"Vui lòng nhập số lượng bạn muốn đặt",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            final String uid=currentUser.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("GioHang");

            HashMap<String,Object> giohang =new HashMap<>();
            String ten = monAn.getTen();
            String gia = monAn.getGia();
            String hinhanh = monAn.getHinhanh();
            float rating = monAn.getRating();
            String id = monAn.getId();
            String mota = monAn.getMota();
            int soluong_monan = Integer.parseInt(soluong);

            giohang.put("ten",ten);
            giohang.put("gia",gia);
            giohang.put("hinhanh",hinhanh);
            giohang.put("rating",rating);
            giohang.put("mota",mota);
            giohang.put("soluong",soluong_monan);

            myRef.child(uid).child("MonAn").child(id).updateChildren(giohang).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(ChitietMonAn.this, "Thêm món ăn vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                    edt_soluong.setText("");
                    finish();
                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.yeuthich,menu);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        final String uid=currentUser.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("YeuThich");
        final String id = monAn.getId();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child(uid).child(id).exists()){
                    menu.getItem(1).setVisible(false);
                    menu.getItem(0).setVisible(true);
                }else {
                    menu.getItem(1).setVisible(true);
                    menu.getItem(0).setVisible(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case  R.id.yeuthich:
                themvaoyeuthich();
                return true;
            case R.id.dayeuthich:
                xoayeuthich();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void xoayeuthich() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("YeuThich");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        final String uid=currentUser.getUid();
        String id = monAn.getId();

        myRef.child(uid).child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(ChitietMonAn.this,"Xóa khỏi món ăn yêu thích thành công",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void themvaoyeuthich() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("YeuThich");
        HashMap<String,Object> yeuthich =new HashMap<>();
        String ten = monAn.getTen();
        String gia = monAn.getGia();
        String hinhanh = monAn.getHinhanh();
        float rating = monAn.getRating();
        String id = monAn.getId();
        String mota = monAn.getMota();
        int soluongdanhgia = monAn.getSoluongdanhgia();

        yeuthich.put("ten",ten);
        yeuthich.put("gia",gia);
        yeuthich.put("hinhanh",hinhanh);
        yeuthich.put("rating",rating);
        yeuthich.put("soluongdanhgia",soluongdanhgia);
        yeuthich.put("mota",mota);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        final String uid=currentUser.getUid();

        myRef.child(uid).child(id).updateChildren(yeuthich).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ChitietMonAn.this, "Thêm vào danh sách yêu thích thành công", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
