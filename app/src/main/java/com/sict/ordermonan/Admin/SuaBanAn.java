package com.sict.ordermonan.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sict.ordermonan.R;
import com.sict.ordermonan.chien.model.BanAn;

public class SuaBanAn extends AppCompatActivity {
    EditText edt_tenbanan,edt_soghe;
    Button dongy,huy;
    BanAn banAn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_ban_an);

        addControls();
        addEvents();

        Intent intent = getIntent();
        banAn = (BanAn) intent.getSerializableExtra("SUABANAN");
        if (banAn!=null){
            edt_tenbanan.setText(banAn.getTenBanAn());
            edt_soghe.setText(banAn.getSoghe());
        }
    }

    private void addEvents() {
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenbanan = edt_tenbanan.getText().toString();
                String soghe = edt_soghe.getText().toString();
                String id = banAn.getId();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("BanAn");

                myRef.child(id).child("TenBanAn").setValue(tenbanan);
                myRef.child(id).child("soghe").setValue(soghe);

                finish();
                Toast.makeText(SuaBanAn.this,"Sửa bàn ăn thành công",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addControls() {
        edt_tenbanan = (EditText) findViewById(R.id.edt_tenban_u);
        edt_soghe = (EditText) findViewById(R.id.edt_soghe_u);

        dongy = (Button) findViewById(R.id.btn_dongy_updateban);
        huy = (Button) findViewById(R.id.btn_huyupdateban);
    }
}
