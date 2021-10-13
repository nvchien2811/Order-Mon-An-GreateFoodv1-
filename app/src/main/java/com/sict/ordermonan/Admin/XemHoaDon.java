package com.sict.ordermonan.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.sict.ordermonan.R;

public class XemHoaDon extends AppCompatActivity {
    RadioGroup radioGroup;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_hoa_don);

        toolbar = (Toolbar) findViewById(R.id.toolbar_xemhoadon);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contrainer_hoadon, new FragmentChuaThanhToan()).commit();



        radioGroup = (RadioGroup) findViewById(R.id.group_radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment selectedfragment = null;
                switch (checkedId){
                    case R.id.rd_chuathanhtoan:
                        selectedfragment = new FragmentChuaThanhToan();
                        break;
                    case R.id.rd_dathanhtoan:
                        selectedfragment = new FragmentDaThanhToan();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contrainer_hoadon,selectedfragment).commit();
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
