package com.sict.ordermonan;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sict.ordermonan.adapter.GioHangAdapter;
import com.sict.ordermonan.chien.model.BanAn;
import com.sict.ordermonan.chien.model.HoaDon;
import com.sict.ordermonan.chien.model.KhuyenMai;
import com.sict.ordermonan.chien.model.MonAn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class CartFragment extends Fragment {
    RecyclerView recyclerView;
    GioHangAdapter adapter;
    LinearLayoutManager layoutManager;
    ArrayList<MonAn> arrayList;
    FirebaseAuth mAuth;
    MonAn monAn;
    TextView tongtien,bandadat,txt_carttrong;
    Button btn_thanhtoan,btn_km;
    HoaDon hoaDon;
    ArrayList<HoaDon> hoaDons;
    String saveDate,saveTime;
    int tong = 0;
    EditText edt_km;
    KhuyenMai khuyenMai;
    ArrayList<KhuyenMai> khuyenMais;
    ArrayList<KhuyenMai> khuyenMaistinhtoan;
    String MAKM;
    BanAn banAn;
    ArrayList<BanAn> banAns;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        btn_thanhtoan =(Button) view.findViewById(R.id.thanhtoan);
        edt_km = (EditText) view.findViewById(R.id.edt_km);
        btn_km = (Button) view.findViewById(R.id.btn_km);

        recyclerView =(RecyclerView) view.findViewById(R.id.rcl_giohang);
        bandadat =(TextView) view.findViewById(R.id.bandadat);
        txt_carttrong = (TextView) view.findViewById(R.id.txt_carttrong);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        tongtien = view.findViewById(R.id.tongtienthanhtoan);
        mAuth = FirebaseAuth.getInstance();

        arrayList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("GioHang");
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final String uid = firebaseUser.getUid();

        myRef.child(uid).child("MonAn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    monAn = data.getValue(MonAn.class);

                    monAn.setId(data.getKey());
                    arrayList.add(monAn);

                }

                if (arrayList.size()<=0){
                    txt_carttrong.setText("Giỏ hàng trống, vui lòng chọn món ăn...");
                }
                tong = 0;
                for (int i = 0; i < arrayList.size(); i++) {
                        int gia = Integer.parseInt(arrayList.get(i).getGia());
                        tong += gia * arrayList.get(i).getSoluong();
                }

                String httong = String.format("%,d", tong);
                tongtien.setText(httong + " " + "đ");


                adapter = new GioHangAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        khuyenMaistinhtoan = new ArrayList<>();
        //Trừ mã khuyến mãi
        myRef.child(uid).child("KhuyenMai").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot5) {

                if (dataSnapshot5.exists()){
                    khuyenMaistinhtoan.clear();
                    for (DataSnapshot dada : dataSnapshot5.getChildren()){
                        khuyenMai = dada.getValue(KhuyenMai.class);

                        khuyenMai.setId(dada.getKey());

                        khuyenMaistinhtoan.add(khuyenMai);
                    }
                    for (int k = 0 ;k<khuyenMaistinhtoan.size();k++) {
                        if (dataSnapshot5.exists()) {
                            String tonglonhon = khuyenMaistinhtoan.get(k).getTonglonhon();
                            int tlh = Integer.parseInt(tonglonhon);
                            String idmamkm = khuyenMaistinhtoan.get(k).getId();
                            MAKM = khuyenMaistinhtoan.get(k).getMakm();
                            String trukm = dataSnapshot5.child(idmamkm).child("sotiengiam").getValue().toString();
                            int itrukm = Integer.parseInt(trukm);
                            if (tong < tlh) {
                                itrukm = 0;
                                myRef.child(uid).child("KhuyenMai").removeValue();
                                Toast.makeText(getActivity(),"Mã của bạn bị xóa vì không đủ điều kiện",Toast.LENGTH_SHORT).show();
                            }

                            tong = tong - itrukm;
                        }
                    }
                    edt_km.setHint("Bạn đã nhập mã khuyến mãi "+MAKM +"\nBạn muốn dùng mã khác hãy nhập lại" );
                    String httong = String.format("%,d", tong);
                    tongtien.setText(httong + " " + "đ");
                }else {

                    edt_km.setHint("Nhập mã khuyến mãi (nếu có)");
                    String httong = String.format("%,d", tong);
                    tongtien.setText(httong + " " + "đ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Check đã đặt bàn????
        hoaDons = new ArrayList<>();
        DatabaseReference kiemtraban = FirebaseDatabase.getInstance().getReference("HoaDon");
        kiemtraban.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                hoaDons.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    hoaDon = data.getValue(HoaDon.class);

                    hoaDon.setIdhoadon(data.getKey());
                    hoaDons.add(hoaDon);
                }

                bandadat.setText("Bàn ăn :");

                banAns = new ArrayList<>();
                final DatabaseReference banan = FirebaseDatabase.getInstance().getReference("BanAn");
                banan.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                        for (DataSnapshot data : dataSnapshot1.getChildren()){
                            banAn = data.getValue(BanAn.class);

                            banAn.setId(data.getKey());
                            banAns.add(banAn);
                        }
                        int coi = 0;
                        for (int l=0;l<banAns.size();l++){
                            String idban = banAns.get(l).getId();
                            if (coi++>1){
                                if (dataSnapshot.child(hoaDon.getIdhoadon()).child("BanAn").child(idban).exists() && hoaDon.isDadathang()==false && hoaDon.getUid().equals(uid)==true) {
                                    bandadat.setText(bandadat.getText().toString() +" " + dataSnapshot.child(hoaDon.getIdhoadon()).child("BanAn").child(idban).child("tenbanan").getValue().toString()+",");
                                }
                            }else
                            if (dataSnapshot.child(hoaDon.getIdhoadon()).child("BanAn").child(idban).exists() && hoaDon.isDadathang()==false && hoaDon.getUid().equals(uid)==true){
                                bandadat.setText("Bạn đã đặt bàn "+dataSnapshot.child(hoaDon.getIdhoadon()).child("BanAn").child(idban).child("tenbanan").getValue().toString());
                                coi++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Nút thanh toán
        btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Bạn có chắc chắn muốn thanh toán");
                builder.setView(R.layout.luachondatban);

                final AlertDialog dialog = builder.create();
                dialog.show();

                Button btn_khong = dialog.findViewById(R.id.btn_khong_dat_ban);
                Button btn_co = dialog.findViewById(R.id.btn_co_datban);

                btn_khong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                btn_co.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tong==0){
                            Toast.makeText(getContext(),"Vui lòng chọn món ăn",Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            return;
                        }
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef1 = database.getReference("HoaDon");
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        final String uid = firebaseUser.getUid();
                        final String email = firebaseUser.getEmail();
                        hoaDons = new ArrayList<>();
                        Calendar calendar = Calendar.getInstance();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                        saveDate = dateFormat.format(calendar.getTime());

                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
                        saveTime = timeFormat.format(calendar.getTime());


                        // Chạy 1 lần Single Value Listener
                        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {

                                    hoaDon = data.getValue(HoaDon.class);

                                    hoaDon.setIdhoadon(data.getKey());
                                    hoaDons.add(hoaDon);
                                }

                                final String idhoadon = hoaDon.getIdhoadon();

                                // neu ma a muon chay 1 lan thi dung singlevaluelistenner



                                if(hoaDon.getUid().equals(uid)==false || hoaDon.getUid().equals(uid)==true  && hoaDon.isDadathang()==true){
                                    Toast.makeText(getContext(), "Vui lòng chọn bàn ăn trước khi thanh toán ", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                                else if (hoaDon.getUid().equals(uid)==true && hoaDon.isTinhtrang()==false && hoaDon.isDadathang()==false ){

                                    //Đưa món ăn vào
                                    HashMap<String, Object> hoadon = new HashMap<>();
                                    for (int i = 0; i < arrayList.size(); i++) {
                                        String id = arrayList.get(i).getId();
                                        hoadon.put(id, arrayList.get(i));
                                    }

                                    //Đưa mã khuyến mãi vào
                                    DatabaseReference refGiohag = FirebaseDatabase.getInstance().getReference("GioHang");
                                    khuyenMaistinhtoan = new ArrayList<>();
                                    refGiohag.child(uid).child("KhuyenMai").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            khuyenMaistinhtoan.clear();
                                            for (DataSnapshot d : dataSnapshot.getChildren()){
                                                khuyenMai = d.getValue(KhuyenMai.class);
                                                khuyenMai.setId(d.getKey());

                                                khuyenMaistinhtoan.add(khuyenMai);
                                            }
                                            HashMap<String,Object> khuyenmaih = new HashMap<>();
                                            for (int l=0;l<khuyenMaistinhtoan.size();l++){
                                                String iii = khuyenMaistinhtoan.get(l).getId();
                                                khuyenmaih.put("KhuyenMai",khuyenMaistinhtoan.get(l));
                                            }
                                            myRef1.child(idhoadon).updateChildren(khuyenmaih);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    boolean tinhtrang = false;
                                    myRef1.child(idhoadon).child("tongtien").setValue(tong);
                                    myRef1.child(idhoadon).child("tinhtrang").setValue(tinhtrang);
                                    myRef1.child(idhoadon).child("ngay").setValue(saveDate);
                                    myRef1.child(idhoadon).child("thoigian").setValue(saveTime);
                                    myRef1.child(idhoadon).child("email").setValue(email);
                                    myRef1.child(idhoadon).child("dadathang").setValue(true );

                                    myRef1.child(idhoadon).child("MonAn").updateChildren(hoadon).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getContext(), "Vui lòng đợi nhân viên của chúng tôi đến xác nhận thanh toán", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                            Intent intent = new Intent(getContext(),ThongBaoThanhCong.class);
                                            startActivity(intent);
                                        }
                                    });
                                    myRef.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            adapter.notifyDataSetChanged();
                                            tong = 0;
                                            String httong = String.format("%,d", tong);
                                            tongtien.setText(httong + " " + "đ");

                                        }
                                    });
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }
        });


        btn_km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String maduocnhap = edt_km.getText().toString().toUpperCase();

                if (TextUtils.isEmpty(maduocnhap)){
                    Toast.makeText(getContext(),"Hãy nhập mã khuyến mãi",Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseReference refKhuyenMai = FirebaseDatabase.getInstance().getReference("KhuyenMai");
                khuyenMais = new ArrayList<>();
                refKhuyenMai.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     for (DataSnapshot datakm : dataSnapshot.getChildren()){
                         khuyenMai = datakm.getValue(KhuyenMai.class);

                         khuyenMai.setId(datakm.getKey());

                         khuyenMais.add(khuyenMai);
                     }

                     for (int g=0;g<khuyenMais.size();g++){
                         String idkm = khuyenMais.get(g).getId();
                         String tonglonhon = khuyenMais.get(g).getTonglonhon();
                         int tlh = Integer.parseInt(tonglonhon);
                         String makm = dataSnapshot.child(idkm).child("makm").getValue().toString();
                         if (dataSnapshot.child(idkm).child("makm").exists() && makm.equals(maduocnhap)==true){

                             if (tong<tlh){
                                 String hientonglh = String.format("%,d",tlh);
                                 Toast.makeText(getContext(),"Chỉ áp dụng cho đơn hàng lớn hơn "+hientonglh+"đ",Toast.LENGTH_SHORT).show();
                                 return;
                             }

                             final HashMap<String,Object> khuyenmai = new HashMap<>();
                             khuyenmai.put(idkm,khuyenMais.get(g));

                             myRef.child(uid).child("KhuyenMai").addListenerForSingleValueEvent(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                     if (dataSnapshot.exists()){

                                         myRef.child(uid).child("KhuyenMai").removeValue();
                                         myRef.child(uid).child("KhuyenMai").updateChildren(khuyenmai).addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 Toast.makeText(getContext(),"Nhập mã khuyến mãi "+maduocnhap+" "+"thành công!",Toast.LENGTH_SHORT).show();
                                                 edt_km.setText("");
                                             }
                                         });

                                     }else {

                                         myRef.child(uid).child("KhuyenMai").updateChildren(khuyenmai).addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 Toast.makeText(getContext(),"Nhập mã khuyến mãi "+maduocnhap+" "+"thành công!",Toast.LENGTH_SHORT).show();
                                                 edt_km.setText("");
                                             }
                                         });
                                     }
                                 }

                                 @Override
                                 public void onCancelled(@NonNull DatabaseError databaseError) {

                                 }
                             });


                         }
                         else {
                             Toast.makeText(getContext(),"Mã khuyến mãi không tồn tại",Toast.LENGTH_SHORT).show();
                             edt_km.setText("");
                         }
                     }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return view;
    }

    //Khởi động menu
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_lichsu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.lichsuhoadon){
          Intent intent = new Intent(getContext(),LichSuHoaDon.class);
          startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }
}
