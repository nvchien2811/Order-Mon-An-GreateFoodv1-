package com.sict.ordermonan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ThongBaoThanhCong extends AppCompatActivity {
    Button trove,xemhoadon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao_thanh_cong);
        trove = (Button) findViewById(R.id.trove_thanhcong);

        xemhoadon = (Button) findViewById(R.id.diden_xemhoadon);

        trove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        xemhoadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThongBaoThanhCong.this,LichSuHoaDon.class);
                startActivity(intent);
            }
        });
    }
}
