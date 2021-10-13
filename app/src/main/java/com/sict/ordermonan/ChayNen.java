package com.sict.ordermonan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class ChayNen extends AppCompatActivity {
    TextView tenphienban;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chay_nen);
        tenphienban = (TextView) findViewById(R.id.tenphienban);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
            tenphienban.setText("Version "+" "+packageInfo.versionName);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   Intent intent = new Intent(ChayNen.this,MainActivity.class);
                   startActivity(intent);
                   finish();
                }
            },2000);
        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
