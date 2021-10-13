package com.sict.ordermonan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sict.ordermonan.chien.model.Taikhoan;

import java.util.ArrayList;
import java.util.HashMap;

public class ThongTinCaNhan extends AppCompatActivity {
    EditText ten,sdt,diachi;
    Button btn_trove,btn_capnhat;
    FirebaseAuth mAuth;
    ArrayList<Taikhoan> arrayList;
    Taikhoan taikhoan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ca_nhan);


        ten = (EditText) findViewById(R.id.edt_ten_u);
        sdt = (EditText) findViewById(R.id.edt_sdt_u);
        diachi = (EditText) findViewById(R.id.edt_diachi_u);

        mAuth = FirebaseAuth.getInstance();

        btn_trove = (Button) findViewById(R.id.btn_trove_tt);
        btn_capnhat = (Button) findViewById(R.id.btn_capnhatcanhan);

        final String uid = mAuth.getUid();


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(uid).exists()){
                    if (dataSnapshot.child(uid).child("ten").exists()) {
                        String ten_t = dataSnapshot.child(uid).child("ten").getValue().toString();
                        String sdt_t = dataSnapshot.child(uid).child("sdt").getValue().toString();
                        String diachi_t = dataSnapshot.child(uid).child("diachi").getValue().toString();

                        ten.setText(ten_t);
                        sdt.setText(sdt_t);
                        diachi.setText(diachi_t);
                    }
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
        btn_capnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tensua = ten.getText().toString();
                String sdtsua = sdt.getText().toString();
                String diachisua = diachi.getText().toString();
                HashMap<String, Object> suatt = new HashMap<>();
                suatt.put("ten",tensua);
                suatt.put("sdt",sdtsua);
                suatt.put("diachi",diachisua);

                reference.child(uid).updateChildren(suatt).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ThongTinCaNhan.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }
}
