package com.sict.ordermonan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sict.ordermonan.adapter.ChuaThanhToanAdapter;
import com.sict.ordermonan.adapter.ChuaThanhToanKhachAdapter;
import com.sict.ordermonan.chien.model.HoaDon;

import java.util.ArrayList;

public class FragmentChuaThanhToanKhach extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ChuaThanhToanKhachAdapter adapter;
    ArrayList<HoaDon> arrayList;
    FirebaseAuth mAuth;
    TextView txt_Convert;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chuathanhtoan, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String uid = user.getUid();

        txt_Convert = (TextView) view.findViewById(R.id.txt_convert);
        recyclerView = (RecyclerView) view.findViewById(R.id.rcl_chuathanhtoan);
        layoutManager  = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        txt_Convert.setText("Đang chờ xử lý");
        arrayList = new ArrayList<>();


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("HoaDon");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    arrayList.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        HoaDon hoaDon = data.getValue(HoaDon.class);

                        hoaDon.setIdhoadon(data.getKey());

                        if (dataSnapshot.child(hoaDon.getIdhoadon()).child("tinhtrang").exists()) {
                            if (hoaDon.isTinhtrang() == false && hoaDon.getUid().equals(uid) == true) {
                                if (dataSnapshot.child(hoaDon.getIdhoadon()).child("MonAn").exists()) {
                                    arrayList.add(hoaDon);
                                }
                            }
                        }
                    }
                    adapter = new ChuaThanhToanKhachAdapter(arrayList, getContext());
                    recyclerView.setAdapter(adapter);
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}
