package com.sict.ordermonan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sict.ordermonan.ChitietMonAn;
import com.sict.ordermonan.HomeFragment;
import com.sict.ordermonan.R;
import com.sict.ordermonan.chien.model.MonAn;
import com.sict.ordermonan.chien.model.Taikhoan;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoAnAdapter extends RecyclerView.Adapter<MoAnAdapter.ViewHolder> implements Filterable {
    ArrayList<MonAn> monAns;
    Context context;
    ArrayList<MonAn> monAnAll;

    float myRating = 0;



    public MoAnAdapter(ArrayList<MonAn> monAns, Context context) {
        this.monAns = monAns;
        this.context = context;
            monAnAll = new ArrayList<>(monAns);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_rcl_home_monan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.ten_monan.setText( monAns.get(position).getTen());
        String gia =  monAns.get(position).getGia();
        int i = Integer.parseInt(gia);
        String str = String.format("%,d",i);
        holder.gia_monan.setText(str +" "+"đ");
        try {
            Picasso.get().load(monAns.get(position).getHinhanh()).into(holder.anh_monan);
        }catch (Exception e){

        }

         holder.ratingBar.setRating(monAns.get(position).getRating());

         int sldanhgia = monAns.get(position).getSoluongdanhgia();
         String sldg = String.valueOf(sldanhgia);
         holder.soluongdanhgia.setText("( "+sldg+" Đánh giá"+" )");

        }

    @Override
    public int getItemCount() {
        return monAns.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        // run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<MonAn> filteredList = new ArrayList<>();
            if(constraint == null|| constraint.length() == 0){
                filteredList.addAll(monAnAll);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(MonAn item : monAnAll){
                    if (item.getTen().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

         FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        //run on a ui thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
         monAns.clear();
         monAns.addAll((ArrayList)results.values);
         notifyDataSetChanged();
        }
    };
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView anh_monan;
        TextView ten_monan,gia_monan,soluongdanhgia;
        RatingBar ratingBar;
        Button btn_chitiet;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            anh_monan = (ImageView) itemView.findViewById(R.id.anh_monan);
            ten_monan = (TextView) itemView.findViewById(R.id.ten_monan);
            gia_monan = (TextView) itemView.findViewById(R.id.gia_monan);
            btn_chitiet = (Button) itemView.findViewById(R.id.btn_chitiet);
            soluongdanhgia = (TextView) itemView.findViewById(R.id.soluongdanhgia);
            btn_chitiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChitietMonAn.class);
                    intent.putExtra("MONAN",monAns.get(getPosition()));
                    context.startActivity(intent);
                }
            });

            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);



        }
        }
    }

