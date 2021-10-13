package com.sict.ordermonan.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
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
import com.sict.ordermonan.MainActivity;
import com.sict.ordermonan.R;
import com.sict.ordermonan.adapter.CNBanAnAdapter;
import com.sict.ordermonan.chien.model.BanAn;

import java.util.ArrayList;
import java.util.HashMap;

public class CapNhatBanAn extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<BanAn> arrayList;
    LinearLayoutManager layoutManager;
    CNBanAnAdapter adapter;
    FloatingActionButton floatingActionButton;
    Dialog Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_ban_an);

        floatingActionButton =(FloatingActionButton)findViewById(R.id.fab_add_banan);

        recyclerView = (RecyclerView) findViewById(R.id.rcl_capnhatbanan);
        Dialog = new Dialog(CapNhatBanAn.this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);

        arrayList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("BanAn");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    BanAn banAn = data.getValue(BanAn.class);

                     banAn.setId(data.getKey());
                    arrayList.add(banAn);
                }
                adapter = new CNBanAnAdapter(arrayList,getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CapNhatBanAn.this,"Có lỗi khi load bàn ăn",Toast.LENGTH_SHORT).show();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.setTitle("Thêm món ăn");
                Dialog.show();
                Dialog.setContentView(R.layout.activity_them_ban_an);
                final EditText edt_tenbaan = (EditText) Dialog.findViewById(R.id.edt_tenban);
                final EditText edt_soghe = (EditText) Dialog.findViewById(R.id.edt_soghe);
                Button btn_dongy = (Button) Dialog.findViewById(R.id.btn_dongy_addban);
                Button btn_huy = (Button) Dialog.findViewById(R.id.btn_huyaddban);

                btn_huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      Dialog.cancel();
                    }
                });
                btn_dongy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String tenbanan = edt_tenbaan.getText().toString();
                        final String soghe = edt_soghe.getText().toString();
                        if(TextUtils.isEmpty(tenbanan)){
                            Toast.makeText(CapNhatBanAn.this,"Vui lòng điền tên bàn",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(soghe)){
                            Toast.makeText(CapNhatBanAn.this,"Vui lòng điền số ghế",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            boolean tinhtrang = false;
                            String id = myRef.push().getKey();

                            HashMap<String,Object> banAn = new HashMap<>();
                            banAn.put("id",id);
                            banAn.put("tinhtrang",tinhtrang);
                            banAn.put("TenBanAn",tenbanan);
                            banAn.put("soghe",soghe);

                            myRef.child(id).setValue(banAn).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(CapNhatBanAn.this,"Thêm bàn ăn thành công",Toast.LENGTH_SHORT).show();
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
