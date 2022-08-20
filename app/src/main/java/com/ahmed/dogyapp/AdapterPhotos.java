package com.ahmed.dogyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterPhotos extends RecyclerView.Adapter<AdapterPhotos.holder> {

    private ArrayList<Photo> dataMessage;
    private Context context;

    public void swapAdapter(ArrayList<Photo> data){
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
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        if (dataMessage == null) return;
        Photo p = dataMessage.get(position);
        holder.txt.setText(p.getDesc());
        Glide.with(context).load(p.getPhoto()).into(holder.i);

    }

    @Override
    public int getItemCount() {
        if (dataMessage == null){
            return 0;
        }
        return dataMessage.size();
    }

    class holder extends RecyclerView.ViewHolder {
        ImageView i;
        TextView txt;
        public holder(@NonNull View itemView) {
            super(itemView);
            i = itemView.findViewById(R.id.i);
            txt = itemView.findViewById(R.id.desc);

            itemView.setOnClickListener(view -> click.open(dataMessage.get(getAdapterPosition())));

        }
    }

    public interface Click{
        void open(Photo photo);
    }

    private final Click click;

    public AdapterPhotos(Click c){
        click = c;
    }

}
