package com.sict.ordermonan;

import android.Manifest;
import android.app.ProgressDialog;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sict.ordermonan.adapter.SliderAdapter;
import com.sict.ordermonan.chien.model.MonAn;
import com.sict.ordermonan.adapter.MoAnAdapter;
import com.sict.ordermonan.chien.model.Taikhoan;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ArrayList<MonAn> arrayList;
    MoAnAdapter adapter;
    MonAn monAn;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String CALL_PHONE;
    float rating;
    float tong ;
    float ketqua ;
    int dem ;
    Taikhoan taikhoan;
    ArrayList<Taikhoan> taikhoans;

    int image[] = {R.drawable.ba3,R.drawable.ba1,R.drawable.ba2};
    ViewPager viewPager;
    int currentPageCuntr = 0;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseAuth= FirebaseAuth.getInstance();

        recyclerView = (RecyclerView) view.findViewById(R.id.rcl_monan);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        progressDialog = new ProgressDialog(getActivity());

        viewPager = (ViewPager)  view.findViewById(R.id.viewpager);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        CALL_PHONE = Manifest.permission.CALL_PHONE;
        arrayList  = new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("MonAn");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    monAn = data.getValue(MonAn.class);

                    monAn.setId(data.getKey());
                    arrayList.add(monAn);
                }
                adapter = new MoAnAdapter(arrayList,getContext());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Load Data Falied" + databaseError.toString(), Toast.LENGTH_LONG).show();
                Log.d("MYTAG", "onCalled" + databaseError.toString());
            }
        });
        taikhoans = new ArrayList<>();
        DatabaseReference refUsers = FirebaseDatabase.getInstance().getReference("Users");
        refUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              for (DataSnapshot dataUsers : dataSnapshot.getChildren()){
                  taikhoan = dataUsers.getValue(Taikhoan.class);

                  taikhoan.setId(dataUsers.getKey());

                  taikhoans.add(taikhoan);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference refDanhgia = FirebaseDatabase.getInstance().getReference("DanhGia");
        refDanhgia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (int i=0;i<arrayList.size();i++){
                   if (dataSnapshot.child(arrayList.get(i).getId()).exists()){
                       ketqua =0;
                       tong = 0;
                       dem = 0;
                       for (int z=0;z<taikhoans.size();z++){
                           String idusers = taikhoans.get(z).getId();
                           if (dataSnapshot.child(arrayList.get(i).getId()).child(idusers).exists()){
                               dem++;
                               String f = dataSnapshot.child(arrayList.get(i).getId()).child(idusers).child("rating").getValue().toString();
                               rating = Float.parseFloat(f);
                               tong += rating;
                               ketqua = tong/dem;
                           }
                       }
                   }

                   DatabaseReference refMonAn = FirebaseDatabase.getInstance().getReference("MonAn");
                   if (dataSnapshot.child(arrayList.get(i).getId()).exists()) {
                       refMonAn.child(arrayList.get(i).getId()).child("rating").setValue(ketqua);
                       refMonAn.child(arrayList.get(i).getId()).child("soluongdanhgia").setValue(dem);
                   }else if (!dataSnapshot.child(arrayList.get(i).getId()).exists()) {
                       refMonAn.child(arrayList.get(i).getId()).child("soluongdanhgia").setValue(0);
                   }

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        viewPager.setAdapter(new SliderAdapter(image,getActivity()));
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPageCuntr == image.length){
                    currentPageCuntr = 0;

                }
                viewPager.setCurrentItem(currentPageCuntr++,true);
            }
        };
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        },4500,4500);
        return view;
    }

    //Khởi động menu
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    //Tạo menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.seacrh_monan,menu);
        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.dangxuat){
            progressDialog.setTitle("Đăng xuất");
            progressDialog.setMessage("Đang đăng xuất...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            firebaseAuth.signOut();
            mGoogleSignInClient.signOut();
            checkuser();
        }
        else if(id==R.id.lienhe){
            String number = "0705982473";
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" +number));
            if (ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent);
            } else {
                requestPermissions(new String[]{CALL_PHONE}, 1);
        }

        }
        else if(id==R.id.thongtincanhan){
            Intent intent = new Intent(getContext(),ThongTinCaNhan.class);
            startActivity(intent);
        }
        else if (id==R.id.action_doimatkhau){
            Intent intent = new Intent(getContext(),DoiMatKhau.class);
            startActivity(intent);
        }
        else if (id==R.id.action_doiemail){
            Intent intent = new Intent(getContext(),DoiEmail.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void checkuser(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!=null){

        }else {
            startActivity(new Intent(getActivity(),MainActivity.class));
            getActivity().finish();
            Toast.makeText(getActivity(),"Bạn đã đăng xuất thành công",Toast.LENGTH_SHORT).show();
        }
    }


}
