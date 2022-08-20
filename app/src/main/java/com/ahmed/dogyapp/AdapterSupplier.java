package com.ahmed.dogyapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterSupplier extends RecyclerView.Adapter<AdapterSupplier.holder> {

    private ArrayList<Supplier> dataMessage;
    private Context context;
    private int selectedPos = RecyclerView.NO_POSITION;

    @SuppressLint("NotifyDataSetChanged")
    public void swapAdapter(ArrayList<Supplier> data){
        if (dataMessage == data) return;
        this.dataMessage = data;
        if (data != null){
            this.notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_supllier,
                parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        holder.itemView.setSelected(selectedPos == position);


        Supplier s = dataMessage.get(position);
        if (s == null) return;

        Glide.with(context).load(s.getImage()).into(holder.user);
        String fullname = s.getName() + " " + s.getLastname();

        holder.name.setText(fullname);
        holder.spec.setText(s.getSpecialty());

        switch (s.getSpecialty()){
            case "Training":
                holder.spec_img.setImageDrawable(context.getResources().getDrawable(R.drawable.cat));
                break;
            case "Pet Hotel":
                holder.spec_img.setImageDrawable(context.getResources().getDrawable(R.drawable.pet_hotel));
                break;
            case  "Veterinary":
                holder.spec_img.setImageDrawable(context.getResources().getDrawable(R.drawable.veterinary));
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (dataMessage == null){
            return 0;
        }
        return dataMessage.size();
    }

    class holder extends RecyclerView.ViewHolder{

        ImageView user, spec_img;
        TextView name, spec;
        LinearLayout layout;

        public holder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.photo_item);
            spec_img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name_lastname);
            spec = itemView.findViewById(R.id.specialty);
            layout = itemView.findViewById(R.id.detail);

            itemView.setOnClickListener(view -> {
                notifyItemChanged(selectedPos);
                selectedPos = getLayoutPosition();
                notifyItemChanged(selectedPos);
                click.select(dataMessage.get(getAdapterPosition()));
            });

            layout.setOnClickListener(view -> click.open(dataMessage.get(getAdapterPosition())));

        }
    }


    public interface Click{
        void open(Supplier id);
        void select(Supplier supplier);
    }

    private final Click click;

    public AdapterSupplier(Click c){
        click = c;
    }

}
