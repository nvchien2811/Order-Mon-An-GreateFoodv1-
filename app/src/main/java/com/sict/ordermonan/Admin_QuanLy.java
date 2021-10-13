package com.sict.ordermonan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sict.ordermonan.Admin.CapNhatBanAn;
import com.sict.ordermonan.Admin.QuanLyMaKhuyenMai;
import com.sict.ordermonan.Admin.ThemMonAn;
import com.sict.ordermonan.Admin.XemHoaDon;
import com.sict.ordermonan.Admin.XoaSuaMonAn;

public class Admin_QuanLy extends AppCompatActivity {
    Button btn_banan,btn_monan,btn_hoadon,btn_xoasuamonan,btn_chuongtrinhkm;
    androidx.appcompat.widget.Toolbar toolbar;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__quan_ly);
        addControls();
        addEvents();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Admin_QuanLy.this);
        setSupportActionBar(toolbar);
    }
    public void addControls(){
        btn_banan = (Button) findViewById(R.id.capnhat_banan);
        btn_monan = (Button) findViewById(R.id.capnhat_monan);
        btn_hoadon = (Button) findViewById(R.id.hoadon);
        btn_xoasuamonan = (Button) findViewById(R.id.xoa_sua_monan);
        btn_chuongtrinhkm = (Button) findViewById(R.id.chuongtrinhkm);
        toolbar = ( androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_admin);
    }


    public void addEvents(){
      btn_monan.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(Admin_QuanLy.this, ThemMonAn.class);
              startActivity(intent);
          }
      });
      btn_xoasuamonan.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(Admin_QuanLy.this, XoaSuaMonAn.class);
              startActivity(intent);
          }
      });
      btn_banan.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(Admin_QuanLy.this, CapNhatBanAn.class);
              startActivity(intent);
          }
      });
      btn_hoadon.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(Admin_QuanLy.this, XemHoaDon.class);
              startActivity(intent);
          }
      });
      btn_chuongtrinhkm.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(Admin_QuanLy.this, QuanLyMaKhuyenMai.class);
              startActivity(intent);
          }
      });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_dangxuat_admin:
                progressDialog.setTitle("Đăng xuất");
                progressDialog.setMessage("Đang đăng xuất....");
                progressDialog.show();
                mAuth.signOut();
                checkuser();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    private void checkuser(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser!=null){

        }else {
            startActivity(new Intent(Admin_QuanLy.this,MainActivity.class));
            finish();
            Toast.makeText(Admin_QuanLy.this,"Bạn đã đăng xuất thành công",Toast.LENGTH_SHORT).show();
        }
    }
}
