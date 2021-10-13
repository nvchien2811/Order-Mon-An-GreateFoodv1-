package com.sict.ordermonan.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sict.ordermonan.Admin.QuanLyMaKhuyenMai;
import com.sict.ordermonan.R;
import com.sict.ordermonan.chien.model.KhuyenMai;

import java.util.ArrayList;
import java.util.HashMap;

public class KhuyenMaiAdapter extends RecyclerView.Adapter<KhuyenMaiAdapter.ViewHolder> {
    ArrayList<KhuyenMai> khuyenMais;
    Context context;

    public KhuyenMaiAdapter(ArrayList<KhuyenMai> khuyenMais, Context context) {
        this.khuyenMais = khuyenMais;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_rcl_khuyenmai,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
         holder.makm.setText(khuyenMais.get(position).getMakm());

         int stg = Integer.parseInt(khuyenMais.get(position).getSotiengiam());
         String hstg = String.format("%,d",stg);
         holder.sotiengiam.setText(hstg+"đ");

        int tlh = Integer.parseInt(khuyenMais.get(position).getTonglonhon());
        String htlh = String.format("%,d",tlh);
        holder.tonglonhon.setText(htlh+"đ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case  R.id.sua_km:
                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                                builder.setView(R.layout.activity_them_makm);

                                final AlertDialog Dialog = builder.create();
                                Dialog.show();
                                final EditText edt_makm = (EditText) Dialog.findViewById(R.id.edt_add_makm);
                                final EditText edt_sotiengiam = (EditText) Dialog.findViewById(R.id.edt_add_stdg);
                                final EditText edt_tonglonhon  = (EditText) Dialog.findViewById(R.id.edt_add_toithieu);
                                Button btn_dongy = (Button) Dialog.findViewById(R.id.btn_dongy_addkhuyenmai);
                                Button btn_huy = (Button) Dialog.findViewById(R.id.btn_huyaddkhuyemai);
                                TextView txt = (TextView) Dialog.findViewById(R.id.txt_convert_km);

                                txt.setText("Sửa mã khuyến mãi");
                                edt_makm.setText(khuyenMais.get(position).getMakm());
                                edt_sotiengiam.setText(khuyenMais.get(position).getSotiengiam());
                                edt_tonglonhon.setText(khuyenMais.get(position).getTonglonhon());

                                btn_huy.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Dialog.cancel();
                                    }
                                });
                                btn_dongy.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final String makm = edt_makm.getText().toString().toUpperCase();
                                        final String sotiengiam = edt_sotiengiam.getText().toString();
                                        final String tonglonhon = edt_tonglonhon.getText().toString();
                                        if(TextUtils.isEmpty(makm)){
                                            Toast.makeText(context,"Vui lòng điền mã khuyến mãi",Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (TextUtils.isEmpty(sotiengiam)){
                                            Toast.makeText(context,"Vui lòng nhập số tiền được giảm",Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (TextUtils.isEmpty(tonglonhon)){
                                            Toast.makeText(context,"Vui lòng nhập mức tối thiểu của hóa đơn",Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("KhuyenMai");
                                        String id = khuyenMais.get(position).getId();
                                        HashMap<String,Object> khuyenMai = new HashMap<>();
                                        khuyenMai.put("makm",makm.toUpperCase());
                                        khuyenMai.put("sotiengiam",sotiengiam);
                                        khuyenMai.put("tonglonhon",tonglonhon);

                                        myRef.child(id).updateChildren(khuyenMai).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(context,"Sữa mã thành công",Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();
                                                Dialog.cancel();
                                            }
                                        });
                                    }
                                });
                                break;
                            case  R.id.xoa_km:
                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("KhuyenMai");
                                myRef.child(khuyenMais.get(position).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Xóa mã này thành công", Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                    }
                                });
                            break;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.popup_menu_khuyemai);
                popupMenu.show();

            }


        });



    }

    @Override
    public int getItemCount() {
        return khuyenMais.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView makm,sotiengiam,tonglonhon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            makm = (TextView) itemView.findViewById(R.id.km_makm);
            sotiengiam = (TextView) itemView.findViewById(R.id.km_sotiengiam);
            tonglonhon = (TextView) itemView.findViewById(R.id.km_muctoithieu);
        }
    }

}
