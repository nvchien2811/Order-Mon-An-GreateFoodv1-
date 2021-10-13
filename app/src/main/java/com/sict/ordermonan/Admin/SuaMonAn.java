package com.sict.ordermonan.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sict.ordermonan.Admin_QuanLy;
import com.sict.ordermonan.R;
import com.sict.ordermonan.adapter.XoaSuaMonAnAdapter;
import com.sict.ordermonan.chien.model.MonAn;
import com.sict.ordermonan.chien.model.Taikhoan;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SuaMonAn extends AppCompatActivity {
    Button btntrove,btn_sua;
    ImageView img_sua;
    EditText ten_sua,gia_sua,mota_sua;
    MonAn monAn;
    private static final int GalleryPick = 1;
    private static final int CameraPick = 3;
    private  static final int CAMERA_REQUEST_CODE=100;
    String cameraPermissions[];
    private Uri ImageURI;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    String mota,ten,gia,saveDate,saveTime;
    String FoodRandomKey,downloadimageurl;
    Taikhoan taikhoan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_mon_an);

        addControls();
        addEvents();

        storageReference = FirebaseStorage.getInstance().getReference().child("HinhAnhMonAn");
        progressDialog = new ProgressDialog(SuaMonAn.this);

        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        Intent intent = getIntent();
        monAn = (MonAn)intent.getSerializableExtra("SUAMONAN");//.
        if(monAn != null){
            Picasso.get().load(monAn.getHinhanh()).into(img_sua);
            mota_sua.setText(monAn.getMota());
            ten_sua.setText(monAn.getTen());
            gia_sua.setText(monAn.getGia());
        }else {
            Toast.makeText(this,"Có lỗi khi load dữ liệu",Toast.LENGTH_SHORT).show();
        }

    }

    private void addEvents() {
        img_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option[]={"Camera","Ảnh từ thư viện"};
                AlertDialog.Builder builder=new AlertDialog.Builder(SuaMonAn.this);
                builder.setTitle("Lựa chọn của bạn");
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            if (!checkCameraPermission()){
                                requestCameraPermission();
                            }
                            if (checkCameraPermission()){
                                OpenCammmera();
                            }

                        }
                        else if(which==1){
                            OpenGalley();
                        }

                    }
                });
                builder.create().show();
            }
        });
        btntrove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateFoodData();
            }
        });
    }
    private void addControls() {
        img_sua = (ImageView) findViewById(R.id.img_sua);
        ten_sua = (EditText) findViewById(R.id.ten_sua);
        gia_sua = (EditText) findViewById(R.id.gia_sua);
        mota_sua = (EditText) findViewById(R.id.mota_sua);

        btntrove = (Button) findViewById(R.id.trove_s);
        btn_sua = (Button) findViewById(R.id.sua);
    }
    private boolean checkCameraPermission(){
        boolean result= ContextCompat.checkSelfPermission(SuaMonAn.this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(SuaMonAn.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result&&result1;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission(){
        requestPermissions(cameraPermissions,CAMERA_REQUEST_CODE);
    }
    private void OpenCammmera(){
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        ImageURI=SuaMonAn.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,ImageURI);
        startActivityForResult(cameraIntent,CameraPick);


    }
    private void OpenGalley() {
        Intent galleryintent = new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,GalleryPick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryPick && resultCode== RESULT_OK && data != null){
            ImageURI = data.getData();
            img_sua.setImageURI(ImageURI);
        }else if (requestCode == CameraPick){
            img_sua.setImageURI(ImageURI);
        }
    }
    private void ValidateFoodData() {
        mota = mota_sua.getText().toString();
        ten = ten_sua.getText().toString();
        gia = gia_sua.getText().toString();
        if(TextUtils.isEmpty(ten)){
            Toast.makeText(this,"Vui lòng nhập tên món ăn",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(gia)){
            Toast.makeText(this,"Vui lòng nhập giá món ăn",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(mota)){
            Toast.makeText(this,"Vui lòng nhập mô tả món ăn",Toast.LENGTH_SHORT).show();
        }
        else if (ImageURI!=null){
            StoreFoodInformation();
        }else if(ImageURI==null){
            SaveFoodInfoToDatabase2();
        }
    }

    private void SaveFoodInfoToDatabase2() {
        final String id = monAn.getId();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("MonAn");

        myRef.child(id).child("ten").setValue(ten);
        myRef.child(id).child("gia").setValue(gia);
        myRef.child(id).child("mota").setValue(mota);

        progressDialog.setTitle("Sửa món ăn");
        progressDialog.setMessage("Đang sửa món ăn...");
        progressDialog.show();

        final ArrayList<Taikhoan> taikhoans;
        taikhoans = new ArrayList<>();
        DatabaseReference refUser = database.getReference("Users");
        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taikhoans.clear();
                for (DataSnapshot data2 :dataSnapshot.getChildren()){
                    taikhoan = data2.getValue(Taikhoan.class);
                    taikhoan.setId(data2.getKey());

                    taikhoans.add(taikhoan);

                }
                for (int i = 0; i < taikhoans.size(); i++) {
                    final  String idtaikhoan = taikhoans.get(i).getId();
                    final DatabaseReference refYeuThich = database.getReference("YeuThich");
                    refYeuThich.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(idtaikhoan).child(id).exists()){
                                refYeuThich.child(idtaikhoan).child(id).child("ten").setValue(ten);
                                refYeuThich.child(idtaikhoan).child(id).child("gia").setValue(gia);
                                refYeuThich.child(idtaikhoan).child(id).child("mota").setValue(mota);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    final  DatabaseReference refGioHang = database.getReference("GioHang");
                    refGioHang.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(idtaikhoan).child(id).exists()){
                                refGioHang.child(idtaikhoan).child(id).child("ten").setValue(ten);
                                refGioHang.child(idtaikhoan).child(id).child("gia").setValue(gia);
                                refGioHang.child(idtaikhoan).child(id).child("mota").setValue(mota);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(SuaMonAn.this,"Sửa món ăn thành công",Toast.LENGTH_LONG).show();
        finish();


    }

    private void StoreFoodInformation() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        saveDate = dateFormat.format(calendar.getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
        saveTime = timeFormat.format(calendar.getTime());

        FoodRandomKey = saveDate + saveTime;
        final StorageReference filepath = storageReference.child(ImageURI.getLastPathSegment() + FoodRandomKey +".jpg");
        final UploadTask uploadTask = filepath.putFile(ImageURI);
        progressDialog.setTitle("Upload Ảnh");
        progressDialog.setMessage("Hình ảnh đang được upload");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(SuaMonAn.this,"Có lỗi" + message,Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask1=taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask1.isSuccessful());
                Uri dowloadUri=uriTask1.getResult();
                if (uriTask1.isSuccessful()) {
                    downloadimageurl = dowloadUri.toString();
                }
                Toast.makeText(SuaMonAn.this,"Hình ảnh upload thành công",Toast.LENGTH_SHORT).show();
                final Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }

//                        downloadimageurl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SuaMonAn.this,"Thành công",Toast.LENGTH_SHORT).show();

                            SaveFoodInfoToDatabase();
                        }
                    }
                });
            }

            private void SaveFoodInfoToDatabase() {
                final String id = monAn.getId();
                String img = monAn.getHinhanh();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("MonAn");

                myRef.child(id).child("ten").setValue(ten);
                myRef.child(id).child("gia").setValue(gia);
                myRef.child(id).child("mota").setValue(mota);
                myRef.child(id).child("hinhanh").setValue(downloadimageurl);


                final ArrayList<Taikhoan> taikhoans;
                taikhoans = new ArrayList<>();
                DatabaseReference refUser = database.getReference("Users");
                refUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        taikhoans.clear();
                        for (DataSnapshot data2 :dataSnapshot.getChildren()){
                            taikhoan = data2.getValue(Taikhoan.class);
                            taikhoan.setId(data2.getKey());

                            taikhoans.add(taikhoan);

                        }
                        for (int i = 0; i < taikhoans.size(); i++) {
                            final  String idtaikhoan = taikhoans.get(i).getId();
                            final DatabaseReference refYeuThich = database.getReference("YeuThich");
                            refYeuThich.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(idtaikhoan).child(id).exists()){
                                        refYeuThich.child(idtaikhoan).child(id).child("ten").setValue(ten);
                                        refYeuThich.child(idtaikhoan).child(id).child("gia").setValue(gia);
                                        refYeuThich.child(idtaikhoan).child(id).child("mota").setValue(mota);
                                        refYeuThich.child(idtaikhoan).child(id).child("hinhanh").setValue(downloadimageurl);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            final  DatabaseReference refGioHang = database.getReference("GioHang");
                            refGioHang.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(idtaikhoan).child(id).exists()){
                                        refGioHang.child(idtaikhoan).child(id).child("ten").setValue(ten);
                                        refGioHang.child(idtaikhoan).child(id).child("gia").setValue(gia);
                                        refGioHang.child(idtaikhoan).child(id).child("mota").setValue(mota);
                                        refGioHang.child(idtaikhoan).child(id).child("hinhanh").setValue(downloadimageurl);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Toast.makeText(SuaMonAn.this,"Sửa món ăn thành công",Toast.LENGTH_LONG).show();
                finish();


            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
