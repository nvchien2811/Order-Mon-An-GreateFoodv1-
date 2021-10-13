package com.sict.ordermonan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sict.ordermonan.adapter.MoAnAdapter;
import com.sict.ordermonan.chien.model.MonAn;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    RecyclerView recyclerView;
    MoAnAdapter adapter;
    LinearLayoutManager layoutManager;
    ArrayList<MonAn> arrayList;
    FirebaseAuth mAuth;
    MonAn monAn;
    TextView txt_trongfav;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_favorites,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rcl_monan_yeuthich);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        txt_trongfav = (TextView) view.findViewById(R.id.txt_trongfavorites);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        final String uid=currentUser.getUid();

        arrayList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("YeuThich").child(uid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        monAn = data.getValue(MonAn.class);
                        monAn.setId(data.getKey());

                        arrayList.add(monAn);
                    }

                    if (arrayList.size()<=0){
                        txt_trongfav.setText("Chưa thêm các món ăn yêu thích...");
                    }
                adapter = new MoAnAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
//                Toast.makeText(getContext(), "Load Data Sucess", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Load Data thất bại", Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }
}
