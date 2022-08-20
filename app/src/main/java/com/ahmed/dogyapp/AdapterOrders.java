package com.ahmed.dogyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterOrders extends RecyclerView.Adapter<AdapterOrders.holder> {

    ArrayList<String> list;
    private Context context;

    public AdapterOrders(Context context, Click click){
        this.click = click;
        this.context = context;
    }

    public void swapAdapter(ArrayList<String> data){
        if (list == data) return;
        this.list = data;
        if (data != null){
            this.notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public AdapterOrders.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrders.holder holder, int position) {
            String oderid = list.get(position);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String uid = preferences.getString("uid", null);

        FirebaseDatabase.getInstance().getReference().child("PROFILES/" + uid + "/ORDERS/" + oderid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ODER oder = snapshot.getValue(ODER.class);
                if (oder== null)
                    return;

                holder.textView.setText(oder.getDate());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        if (list == null){
            return 0;
        }
        return list.size();
    }

    class holder extends RecyclerView.ViewHolder{

        TextView textView;

        public holder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.dat);

            itemView.setOnClickListener(view -> {
                    click.open(list.get(getAbsoluteAdapterPosition()));
            });

        }
    }

    public interface Click{
        void open(String photo);
    }

    private final Click click;


}
