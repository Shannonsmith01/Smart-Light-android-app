package com.example.smartlightingcontrol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EntityAdapter extends RecyclerView.Adapter<EntityAdapter.EntityViewHolder> {
    ArrayList<Entity> entities;

    public EntityAdapter(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    @NonNull
    @Override
    public EntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entity, parent, false);
        return new EntityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntityViewHolder holder, int position) {
        Entity entity = entities.get(position);
        holder.property1.setText(entity.getProperty1());
        holder.property2.setText(entity.getProperty2());
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public static class EntityViewHolder extends RecyclerView.ViewHolder {
        TextView property1, property2;

        public EntityViewHolder(@NonNull View itemView) {
            super(itemView);
            property1 = itemView.findViewById(R.id.property1);
            property2 = itemView.findViewById(R.id.property2);
        }
    }
}