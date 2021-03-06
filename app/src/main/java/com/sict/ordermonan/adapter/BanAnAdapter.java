package com.sict.ordermonan.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.sict.ordermonan.R;
import com.sict.ordermonan.chien.model.BanAn;
import com.sict.ordermonan.chien.model.HoaDon;

import java.util.ArrayList;
import java.util.HashMap;

public class BanAnAdapter extends RecyclerView.Adapter<BanAnAdapter.ViewHolder> {
    ArrayList<BanAn> banAns;
    Context context;
    ArrayList<HoaDon> hoaDons;
    HoaDon hoaDon;

    public BanAnAdapter(ArrayList<BanAn> banAns, Context context) {
        this.banAns = banAns;
        this.context = context;
    }

    @NonNull
    @Override
    public BanAnAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_rcl_banan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BanAnAdapter.ViewHolder holder, final int position) {
          holder.tenbanan.setText(banAns.get(position).getTenBanAn());
          holder.soghe.setText("("+banAns.get(position).getSoghe()+" "+"gh???"+")");


          if(banAns.get(position).isTinhtrang()==false){
            holder.img_banan.setImageResource(R.drawable.banoff);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(final View v) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    final String uid = firebaseUser.getUid();
                    DatabaseReference check = FirebaseDatabase.getInstance().getReference("HoaDon");
                    hoaDons = new ArrayList<>();
                    check.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            hoaDons.clear();
                          for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                              hoaDon = dataSnapshot1.getValue(HoaDon.class);

                              hoaDon.setIdhoadon(dataSnapshot1.getKey());

                              hoaDons.add(hoaDon);
                          }
                          int dem =0;
                            for (int i = 0; i < hoaDons.size(); i++) {
                                if (hoaDons.get(i).getUid().equals(uid) == true && hoaDons.get(i).isTinhtrang() == false) {
                                        dem++;
                                }
                            }
                            if (dem>=3){
                                Toast.makeText(context,"B???n ??ang c?? 3 h??a ????n ch??a thanh to??n,b???n kh??ng ???????c ?????t th??m n???a",Toast.LENGTH_SHORT).show();
                                return;
                            }else if (dataSnapshot.child(hoaDon.getIdhoadon()).child("BanAn").exists() && dataSnapshot.child(hoaDon.getIdhoadon()).child("uid").getValue().toString().equals(uid)==true && hoaDon.isDadathang()==false){
                                Toast.makeText(context, "B???n mu???n ?????i b??n h??y h???y b??n tr?????c ????", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                builder.setTitle("C?? ph???i b???n mu???n gh??p th??m b??n n??y");
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
                                        String id = banAns.get(position).getId();
                                        String tenbanan = banAns.get(position).getTenBanAn();
                                        String soghe = banAns.get(position).getSoghe();

                                        HashMap<String,Object> banangiohang1 =new HashMap<>();
                                        banangiohang1.put("id",id);
                                        banangiohang1.put("tenbanan",tenbanan);
                                        banangiohang1.put("soghe",soghe);

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("HoaDon");
                                        reference.child(hoaDon.getIdhoadon()).child("BanAn").child(id).updateChildren(banangiohang1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                dialog.cancel();
                                            }
                                        });
                                        String idban = banAns.get(position).getId();
                                        DatabaseReference myref3 = FirebaseDatabase.getInstance().getReference("BanAn").child(idban);
                                        myref3.child("tinhtrang").setValue(true);
                                    }
                                });
                            }
                            else {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                DatabaseReference myRef = database.getReference("HoaDon").child(uid);
                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child("BanAn").exists()){
                                            Toast.makeText(context,"B???n ???? ?????t b??n r???i",Toast.LENGTH_SHORT).show();
                                        }else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setTitle("B???n c?? ch???c ch???n mu???n ?????t b??n n??y?");
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
                                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                                    String uid = firebaseUser.getUid();
                                                    String email = firebaseUser.getEmail();

                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference myRef = database.getReference("HoaDon");

                                                    String idhoadon = myRef.push().getKey();
                                                    String id = banAns.get(position).getId();
                                                    String tenbanan = banAns.get(position).getTenBanAn();
                                                    String soghe = banAns.get(position).getSoghe();

                                                    HashMap<String,Object> banangiohang =new HashMap<>();
                                                    banangiohang.put("id",id);
                                                    banangiohang.put("tenbanan",tenbanan);
                                                    banangiohang.put("soghe",soghe);

                                                    myRef.child(idhoadon).child("email").setValue(email);
                                                    myRef.child(idhoadon).child("uid").setValue(uid);
                                                    myRef.child(idhoadon).child("dadathang").setValue(false);
                                                    myRef.child(idhoadon).child("BanAn").child(id).updateChildren(banangiohang).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(context,"B???n ???? ?????t b??n n??y",Toast.LENGTH_SHORT).show();
                                                            dialog.cancel();
                                                        }
                                                    });

                                                    //set l???i t??nh tr???ng b??n ??n
                                                    String idban = banAns.get(position).getId();
                                                    DatabaseReference myref3 = database.getReference("BanAn").child(idban);
                                                    myref3.child("tinhtrang").setValue(true);
                                                }
                                            });
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


                }
            });

        }//B??n c?? ng?????i ?????t
        else if(banAns.get(position).isTinhtrang()==true){
            holder.img_banan.setImageResource(R.drawable.banan);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    final String uid = firebaseUser.getUid();

                    final DatabaseReference check = FirebaseDatabase.getInstance().getReference("HoaDon");
                    hoaDons = new ArrayList<>();
                    check.addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            hoaDons.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                hoaDon = dataSnapshot1.getValue(HoaDon.class);

                                hoaDon.setIdhoadon(dataSnapshot1.getKey());

                                hoaDons.add(hoaDon);
                            }
                            for (int z=0;z<hoaDons.size();z++){
                                final String idhd = hoaDons.get(z).getIdhoadon();
                                String hdbanan = dataSnapshot.child(idhd).child("BanAn").child(banAns.get(position).getId()).child("id").toString();
                                String lay = "DataSnapshot { key = id, value = ";
                                if (dataSnapshot.child(idhd).child("BanAn").exists()){
                                    if (hdbanan.equals(lay+banAns.get(position).getId()+" }")==true) {
                                        if (hoaDons.get(z).isDadathang() == false) {
                                            if (hoaDons.get(z).getUid().equals(uid) == true) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                builder.setTitle("B???n mu???n h???y ?????t b??n n??y?");
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
                                                        check.child(idhd).child("BanAn").child(banAns.get(position).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(context, "H???y ?????t b??n n??y th??nh c??ng", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                        check.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                if (dataSnapshot.child(idhd).exists()){
                                                                    if (dataSnapshot.child(idhd).child("BanAn").exists()){

                                                                    }else {
                                                                        check.child(idhd).removeValue();
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                        //set l???i t??nh tr???ng b??n ??n
                                                        String idban = banAns.get(position).getId();
                                                        DatabaseReference myref3 = FirebaseDatabase.getInstance().getReference("BanAn").child(idban);
                                                        myref3.child("tinhtrang").setValue(false);

                                                        dialog.cancel();
                                                    }
                                                });
                                            }
                                            else {
                                                Toast.makeText(context, "B???n ???? ?????t ????n h??ng v???i b??n n??y.Vui l??ng h???y ????n h??ng", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    else {
                                        Toast.makeText(context,"B??n n??y ???? c?? ng?????i ?????t ",Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });




        }
    }

    @Override
    public int getItemCount() {
        return banAns.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_banan;
        TextView tenbanan,soghe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_banan = (ImageView) itemView.findViewById(R.id.anh_ban_an);
            tenbanan = (TextView) itemView.findViewById(R.id.tenbanan);
            soghe = (TextView) itemView.findViewById(R.id.soghe);

        }
    }
}
