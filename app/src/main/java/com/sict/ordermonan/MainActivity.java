package com.sict.ordermonan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    EditText edt_tendangnhap,edt_matkhau;
    Button btn_dangnhap,btn_dangky,btn_dangnhapgg;
    TextView quenmk;

    public static final String EMAIL = "userNameKey"; //Tạo 1 chuỗi để nhớ Email
    public static final String PASS = "passKey";      //Tạo 1 chuỗi để nhớ Pass
    public static final String REMEMBER = "remember";
    public static final String MyPREFERENCES = "MyPrefs";  //Chuỗi sẽ được lưu
    SharedPreferences sharedPreferences;             //Thư viên nhớ pass
    FirebaseAuth mAuth;
    private CheckBox checkBox;
    ProgressDialog dialog;
    private static final String TAG = "MainActivity";
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addcontrols();
        addEvents();

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        loadData();


    }
    public void saveData(String email,String pass){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL,email);
        editor.putString(PASS,pass);
        editor.putBoolean(REMEMBER,checkBox.isChecked());
        editor.commit();
    }
    private void loadData() {
        if(sharedPreferences.getBoolean(REMEMBER,false)) {
            edt_tendangnhap.setText(sharedPreferences.getString(EMAIL, ""));
            edt_matkhau.setText(sharedPreferences.getString(PASS, ""));
            checkBox.setChecked(true);
        }
        else
            checkBox.setChecked(false);
    }
    private void clearData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    private void addcontrols(){
    edt_tendangnhap = (EditText) findViewById(R.id.edt_tendangnhap);
    edt_matkhau = (EditText) findViewById(R.id.edt_matkhau);

    btn_dangnhap = (Button)findViewById(R.id.btn_dangnhap);
    btn_dangky = (Button) findViewById(R.id.btn_dangky);

    quenmk = (TextView) findViewById(R.id.quenmk);
    checkBox = (CheckBox) findViewById(R.id.checkbox);
    btn_dangnhapgg = (Button) findViewById(R.id.btn_dangnhapgg);

    }

    private void addEvents(){
        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                final String email = edt_tendangnhap.getText().toString().trim();
                final String password = edt_matkhau.getText().toString().trim();
                if (checkBox.isChecked()){
                    saveData(email,password);
                }
                else {
                    clearData();
                }

               if(TextUtils.isEmpty(email)){
                  Toast.makeText(MainActivity.this,"Vui lòng điền email",Toast.LENGTH_SHORT).show();
                  return;
               }
               if (TextUtils.isEmpty(password)){
                  Toast.makeText(MainActivity.this,"Vui lòng nhập mật khẩu",Toast.LENGTH_SHORT).show();
                  return;
              }
              mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      dialog = new ProgressDialog(MainActivity.this);
                      dialog.setTitle("ĐĂNG NHẬP");
                      dialog.setMessage("Vui lòng đợi chúng tôi kiểm tra tài khoản của bạn");
//                      dialog.setCanceledOnTouchOutside(true);
                      dialog.show();
                      if (task.isSuccessful()){
                          FirebaseUser currentUser = mAuth.getCurrentUser();
                          String email=currentUser.getEmail();

                          String admin = "admin@gmail.com";

                          boolean sosanh = email.equals(admin);

                          if (sosanh == true){
                              Toast.makeText(MainActivity.this, "Đăng nhập thành công admin", Toast.LENGTH_SHORT).show();
                              Intent intent = new Intent(MainActivity.this, Admin_QuanLy.class);
                              startActivity(intent);
                              finish();
                              overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);

                          }
                          else {
                              Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                              Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                              startActivity(intent);
                              finish();

                          }

                      }
                          else {
                          Toast.makeText(MainActivity.this,"Đăng nhập thất bại",Toast.LENGTH_SHORT).show();
                          dialog.cancel();
                      }

                  }
              });

            }
        });


         btn_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("Đăng ký");
                dialog.show();
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_dangky);
                final EditText edt_tk = (EditText) dialog.findViewById(R.id.edt_tk);
                final EditText edt_mk = (EditText) dialog.findViewById(R.id.edt_mk);
                final EditText edt_ten = (EditText) dialog.findViewById(R.id.edt_ten);
                final EditText edt_sdt = (EditText) dialog.findViewById(R.id.edt_sdt);
                final EditText edt_diachi = (EditText) dialog.findViewById(R.id.edt_diachi);


                Button btn_dongy = (Button) dialog.findViewById(R.id.btn_dongy);
                Button btn_huy = (Button) dialog.findViewById(R.id.btn_huy);
                btn_dongy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAuth = FirebaseAuth.getInstance();
                        final String email = edt_tk.getText().toString().trim();
                        final String password = edt_mk.getText().toString().trim();
                        final String ten = edt_ten.getText().toString();
                        final String sdt = edt_sdt.getText().toString();
                        final String diachi = edt_diachi.getText().toString();
                        if(TextUtils.isEmpty(email)){
                            Toast.makeText(MainActivity.this,"Vui lòng điền email",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(password)){
                            Toast.makeText(MainActivity.this,"Vui lòng nhập mật khẩu",Toast.LENGTH_SHORT).show();
                            return;
                        }if (TextUtils.isEmpty(ten)){
                            Toast.makeText(MainActivity.this,"Vui lòng nhập tên",Toast.LENGTH_SHORT).show();
                            return;
                        }if (TextUtils.isEmpty(sdt)){
                            Toast.makeText(MainActivity.this,"Vui lòng nhập số điện thoại",Toast.LENGTH_SHORT).show();
                            return;
                        }if (TextUtils.isEmpty(diachi)){
                            Toast.makeText(MainActivity.this,"Vui lòng nhập địa chỉ",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mAuth.createUserWithEmailAndPassword(email ,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    
                                    mAuth= FirebaseAuth.getInstance();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String uid = user.getUid();
                                    HashMap<String,Object> dangky = new HashMap<>();
                                    dangky.put("email",email);
                                    dangky.put("ten",ten);
                                    dangky.put("sdt",sdt);
                                    dangky.put("diachi",diachi);
                                    dangky.put("uid",uid);

                                    DatabaseReference refUsers = FirebaseDatabase.getInstance().getReference("Users");
                                    refUsers.child(uid).updateChildren(dangky);

                                    Toast.makeText(getApplicationContext(),"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                                    dialog.cancel();

                                    edt_tendangnhap.setText(email);
                                    edt_matkhau.setText(password);
                                }

                            }


                        });


                    }
                });

                btn_huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
      quenmk.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              final Dialog dialog = new Dialog(MainActivity.this);
              dialog.setTitle("Quên Mật Khẩu");
              dialog.show();
              dialog.setCancelable(false);
              dialog.setContentView(R.layout.custom_quen_mat_khau);
              final EditText edt_email = (EditText) dialog.findViewById(R.id.edt_quenmatkhau);
              Button btn_dongy = (Button) dialog.findViewById(R.id.btn_dongy_quenmk);
              Button btn_trove = (Button) dialog.findViewById(R.id.btn_trove);

              btn_dongy.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      String email = edt_email.getText().toString().trim();

                      if (TextUtils.isEmpty(email)) {
                          Toast.makeText(getApplication(), "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                          return;
                      }

                      mAuth = FirebaseAuth.getInstance();
                      mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              if (task.isSuccessful()){
                                  Toast.makeText(getApplication(), "Một đường dẫn tạo lại mật khẩu sẽ được gửi về email của bạn", Toast.LENGTH_SHORT).show();
                                  dialog.cancel();
                              }else {
                                  Toast.makeText(getApplication(), "Email không tồn tại...Hãy nhập lại", Toast.LENGTH_SHORT).show();
                              }
                          }
                      });
                  }
              });

              btn_trove.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      dialog.cancel();
                  }
              });

              dialog.show();
          }
      });


     btn_dangnhapgg.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             signInGoogle();
         }
     });
    }

    //Đăng nhập google

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setTitle("ĐĂNG NHẬP");
                    dialog.setMessage("Vui lòng đợi....");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    mAuth= FirebaseAuth.getInstance();
                    FirebaseUser user = mAuth.getCurrentUser();
                    String uid = user.getUid();
                    String email = user.getEmail();
                    HashMap<String,Object> dangnhapgg = new HashMap<>();
                    dangnhapgg.put("uid",uid);
                    dangnhapgg.put("email",email);

                    DatabaseReference refUsers = FirebaseDatabase.getInstance().getReference("Users");
                    refUsers.child(uid).updateChildren(dangnhapgg);

                    Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(intent);

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       if (dialog!=null && dialog.isShowing()){
           dialog.dismiss();
       }
    }

}




