package com.sict.ordermonan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DoiEmail extends AppCompatActivity {
    EditText emailmoi,matkhau;
    TextView emailhientai;
    Button trove,dongy;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_email);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        emailhientai = (TextView) findViewById(R.id.emailhientai);
        emailmoi = (EditText) findViewById(R.id.emailmoi_doiemail);
        matkhau = (EditText) findViewById(R.id.nhapmk_doiemail);

        trove = (Button) findViewById(R.id.trove_doiemail);
        dongy = (Button) findViewById(R.id.dongy_doiemail);

        final String email = user.getEmail();

        emailhientai.setText(email);
        trove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AuthCredential credential = EmailAuthProvider.getCredential(email,matkhau.getText().toString());
                if (TextUtils.isEmpty(matkhau.getText().toString())){
                    Toast.makeText(DoiEmail.this, "Vui lòng nhập mật khẩu để xác minh", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(emailmoi.getText().toString())){
                    Toast.makeText(DoiEmail.this, "Vui lòng nhập email mới", Toast.LENGTH_SHORT).show();
                    return;
                }

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        user.updateEmail(emailmoi.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    String uid = user.getUid();
                                    DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("Users");
                                    refUser.child(uid).child("email").setValue(emailmoi.getText().toString());
                                    Toast.makeText(DoiEmail.this, "Đổi email thành công", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else {
                                    Toast.makeText(DoiEmail.this, "Email bạn muốn đổi đã tồn tại", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                    }
                    else {
                        Toast.makeText(DoiEmail.this, "Bạn nhập không đúng mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                    }
                });
            }
        });
    }
}
