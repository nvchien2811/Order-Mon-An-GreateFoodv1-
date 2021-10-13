package com.sict.ordermonan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.sict.ordermonan.Admin.FragmentChuaThanhToan;
import com.sict.ordermonan.Admin.FragmentDaThanhToan;

public class LichSuHoaDon extends AppCompatActivity {
    RadioGroup radioGroup;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_hoa_don);

        toolbar = (Toolbar) findViewById(R.id.toolbar_lichsuhoadon);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contrainer_licsuhoadon, new FragmentChuaThanhToanKhach()).commit();
        radioGroup = (RadioGroup) findViewById(R.id.group_radio_u);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment selectedfragment = null;
                switch (checkedId){
                    case R.id.rd_chuathanhtoan_u:
                        selectedfragment = new FragmentChuaThanhToanKhach();
                        break;
                    case R.id.rd_dathanhtoan_u:
                        selectedfragment = new FragmentDaThanhToanKhach();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contrainer_licsuhoadon,selectedfragment).commit();
            }
        });


    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
