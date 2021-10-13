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
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sict.ordermonan.R;
import com.sict.ordermonan.chien.model.MonAn;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ThemMonAn extends AppCompatActivity {
    EditText edt_ten,edt_gia,edt_mota;
    Button btn_trove,btn_them;
    ImageView img_add;
    private static final int GalleryPick = 1;
    private static final int CameraPick = 3;
    private  static final int CAMERA_REQUEST_CODE=100;
    String cameraPermissions[];
    private Uri ImageURI;
    private String ten,gia,mota,saveDate,saveTime;
    String FoodRandomKey,downloadimageurl;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_mon_an);

        addControls();
        addEvents();
        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storageReference = FirebaseStorage.getInstance().getReference().child("HinhAnhMonAn");
        progressDialog = new ProgressDialog(ThemMonAn.this);
    }

    private void addControls() {
        edt_ten = (EditText) findViewById(R.id.ten);
        edt_gia = (EditText) findViewById(R.id.gia);
        edt_mota = (EditText) findViewById(R.id.mota);

        btn_trove = (Button) findViewById(R.id.trove);
        btn_them = (Button) findViewById(R.id.them);
        img_add = (ImageView) findViewById(R.id.imd_add);

    }
    public void addEvents(){
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option[]={"Camera","Ảnh từ thư viện"};
                AlertDialog.Builder builder=new AlertDialog.Builder(ThemMonAn.this);
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
        btn_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateFoodData();
            }
        });
        btn_trove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private boolean checkCameraPermission(){
        boolean result= ContextCompat.checkSelfPermission(ThemMonAn.this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(ThemMonAn.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result&&result1;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission(){
        requestPermissions(cameraPermissions,CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                boolean cameraAccepted= grantResults[0]== PackageManager.PERMISSION_GRANTED;
                boolean writeStorageAccepted= grantResults[1]==PackageManager.PERMISSION_GRANTED;
                if(cameraAccepted && writeStorageAccepted){
                     OpenCammmera();
                }else {
                    Toast.makeText(ThemMonAn.this,  "Vui lòng bật máy ảnh và cho phép lưu trữ", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void OpenCammmera(){
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        ImageURI=ThemMonAn.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

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
         img_add.setImageURI(ImageURI);
        }
        else if (requestCode == CameraPick){
            img_add.setImageURI(ImageURI);
        }
    }
    private void ValidateFoodData() {
      mota = edt_mota.getText().toString();
      ten = edt_ten.getText().toString();
      gia = edt_gia.getText().toString();
        if (ImageURI == null) {
            Toast.makeText(this,"Hình ảnh món ăn là bắt buộc",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(ten)){
            Toast.makeText(this,"Vui lòng nhập tên món ăn",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(gia)){
            Toast.makeText(this,"Vui lòng nhập giá món ăn",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(mota)){
            Toast.makeText(this,"Vui lòng nhập mô tả món ăn",Toast.LENGTH_SHORT).show();
        }
        else {
            StoreFoodInformation();
        }
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
             Toast.makeText(ThemMonAn.this,"Có lỗi" + message,Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                //Lấy đường dẫn ảnh từ storage sang database
                Task<Uri> uriTask1=taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask1.isSuccessful());
                Uri dowloadUri=uriTask1.getResult();
                if (uriTask1.isSuccessful()) {
                    downloadimageurl = dowloadUri.toString();
                }
                Toast.makeText(ThemMonAn.this,"Hình ảnh upload thành công",Toast.LENGTH_SHORT).show();
                final Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull final Task<UploadTask.TaskSnapshot> task) throws Exception {
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
                          Toast.makeText(ThemMonAn.this,"Thành công",Toast.LENGTH_SHORT).show();

                          SaveFoodInfoToDatabase();
                      }
                    }
                });
            }
            private void SaveFoodInfoToDatabase() {
                HashMap<String,Object> foodMap =new HashMap<>();
                foodMap.put("ten",ten);
                foodMap.put("gia",gia);
                foodMap.put("mota",mota);
                foodMap.put("hinhanh",downloadimageurl);
                foodMap.put("date",saveDate);
                foodMap.put("time",saveTime);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("MonAn");
                String id =myRef.push().getKey();

                progressDialog.setTitle("Thêm sản phẩm");
                progressDialog.setMessage("Đang thêm sản phẩm...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                myRef.child(id).updateChildren(foodMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ThemMonAn.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(ThemMonAn.this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
