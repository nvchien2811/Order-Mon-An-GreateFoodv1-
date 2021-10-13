package com.sict.ordermonan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sict.ordermonan.adapter.BanAnAdapter;
import com.sict.ordermonan.chien.model.BanAn;

import java.util.ArrayList;

public class BanAnFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<BanAn> arrayList;
    LinearLayoutManager layoutManager;
    BanAnAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banan,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rcl_banan);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        arrayList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("BanAn");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    BanAn banAn = data.getValue(BanAn.class);

                    banAn.setId(data.getKey());
                    arrayList.add(banAn);
                }
                adapter = new BanAnAdapter(arrayList,getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Load thất bại",Toast.LENGTH_SHORT).show();
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
        inflater.inflate(R.menu.nhacnhobanan,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nhacnho:
                Toast.makeText(getContext(), "Muốn xem bàn đặt, hãy vào giỏ hàng để xem", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
