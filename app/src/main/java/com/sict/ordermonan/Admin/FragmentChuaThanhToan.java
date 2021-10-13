package com.sict.ordermonan.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.sict.ordermonan.R;
import com.sict.ordermonan.adapter.ChuaThanhToanAdapter;
import com.sict.ordermonan.chien.model.HoaDon;

import java.util.ArrayList;

public class FragmentChuaThanhToan extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ChuaThanhToanAdapter adapter;
    ArrayList<HoaDon>  arrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chuathanhtoan, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rcl_chuathanhtoan);
        layoutManager  = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();



        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("HoaDon");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    HoaDon hoaDon = data.getValue(HoaDon.class);

                    hoaDon.setIdhoadon(data.getKey());
                    if (hoaDon.isTinhtrang()==false ) {
                        arrayList.add(hoaDon);
                    }
                }
                adapter = new ChuaThanhToanAdapter(arrayList,getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}
