package com.sict.ordermonan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DoiMatKhau extends AppCompatActivity {
    EditText mkcu,mkmoi,nhaplai_mkmoi;
    Button dongy,trove;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);

        mkcu = (EditText) findViewById(R.id.mkcu);
        mkmoi =(EditText) findViewById(R.id.mkmoi);
        dongy = (Button) findViewById(R.id.dongy_doimk);
        trove = (Button) findViewById(R.id.trove_doimk);
        nhaplai_mkmoi = (EditText) findViewById(R.id.nhaplai_mkmoi);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final String email = user.getEmail();



        dongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mkcu.getText().toString()))
                {
                    Toast.makeText(DoiMatKhau.this, "Vui lòng điền mật khẩu cũ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mkmoi.getText().toString())){
                    Toast.makeText(DoiMatKhau.this, "Vui lòng điền mật khẩu mới", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mkmoi.getText().toString().equals(nhaplai_mkmoi.getText().toString())==false){
                    Toast.makeText(DoiMatKhau.this, "Mật khẩu mới bạn nhập 2 lần không trùng khớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                final AuthCredential credential = EmailAuthProvider.getCredential(email,mkcu.getText().toString());
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            user.updatePassword(mkmoi.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(DoiMatKhau.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else {
                                        Toast.makeText(DoiMatKhau.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(DoiMatKhau.this, "Vui lòng điền đúng mật khẩu cũ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
       trove.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
    }
}
