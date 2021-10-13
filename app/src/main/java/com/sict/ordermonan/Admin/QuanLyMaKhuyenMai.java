package com.sict.ordermonan.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sict.ordermonan.R;
import com.sict.ordermonan.adapter.KhuyenMaiAdapter;
import com.sict.ordermonan.chien.model.BanAn;
import com.sict.ordermonan.chien.model.KhuyenMai;

import java.util.ArrayList;

public class QuanLyMaKhuyenMai extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    KhuyenMaiAdapter adapter;
    ArrayList<KhuyenMai> arrayList;
    KhuyenMai khuyenMai;
    FloatingActionButton fab;
    Dialog Dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_ma_khuyen_mai);

        recyclerView = (RecyclerView) findViewById(R.id.rcl_makhuyenmai);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fab = (FloatingActionButton) findViewById(R.id.fab_add_khuyemai);
        arrayList = new ArrayList<>();
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("KhuyenMai");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
               for (DataSnapshot data : dataSnapshot.getChildren()){
                   khuyenMai = data.getValue(KhuyenMai.class);

                   khuyenMai.setId(data.getKey());

                   arrayList.add(khuyenMai);
               }
               adapter = new KhuyenMaiAdapter(arrayList,getApplicationContext());
               recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Dialog = new Dialog(QuanLyMaKhuyenMai.this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.setTitle("Thêm khuyến mãi");
                Dialog.show();
                Dialog.setContentView(R.layout.activity_them_makm);

                final EditText edt_makm = (EditText) Dialog.findViewById(R.id.edt_add_makm);
                final EditText edt_sotiengiam = (EditText) Dialog.findViewById(R.id.edt_add_stdg);
                final EditText edt_tonglonhon  = (EditText) Dialog.findViewById(R.id.edt_add_toithieu);
                Button btn_dongy = (Button) Dialog.findViewById(R.id.btn_dongy_addkhuyenmai);
                Button btn_huy = (Button) Dialog.findViewById(R.id.btn_huyaddkhuyemai);
                btn_huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog.cancel();
                    }
                });

                btn_dongy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String makm = edt_makm.getText().toString().toUpperCase();
                        final String sotiengiam = edt_sotiengiam.getText().toString();
                        final String tonglonhon = edt_tonglonhon.getText().toString();
                        if(TextUtils.isEmpty(makm)){
                            Toast.makeText(QuanLyMaKhuyenMai.this,"Vui lòng điền mã khuyến mãi",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(sotiengiam)){
                            Toast.makeText(QuanLyMaKhuyenMai.this,"Vui lòng nhập số tiền được giảm",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(tonglonhon)){
                            Toast.makeText(QuanLyMaKhuyenMai.this,"Vui lòng nhập mức tối thiểu của hóa đơn",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {

                            String id = myRef.push().getKey();

                           KhuyenMai khuyenMai = new KhuyenMai(id,makm,sotiengiam,tonglonhon);

                            myRef.child(id).setValue(khuyenMai).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(QuanLyMaKhuyenMai.this,"Thêm mã khuyến mãi thành công",Toast.LENGTH_SHORT).show();
                                    Dialog.cancel();
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
