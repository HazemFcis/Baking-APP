package com.example.hazem.baking_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Hazem on 6/17/2018.
 */

public class Adapter_FristScreen extends RecyclerView.Adapter<Adapter_FristScreen.Holder> {
    private Context context;
    private List<Baking> bakingList;
    public Adapter_FristScreen(Context context, List<Baking> bakingList) {
        this.context = context;
        this.bakingList = bakingList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.baking_item,parent,false);
        return  new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

    Baking baking=bakingList.get(position);
    holder.name.setText(baking.getName());
    holder.servings.setText(String.valueOf(baking.getServings()) + " servings");

    }

    @Override
    public int getItemCount() {
        return bakingList.size();
    }

    public class Holder extends  RecyclerView.ViewHolder {
        public TextView name, servings;
        public ImageView image;
        public Holder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.TV_sandName);
            servings=(TextView)itemView.findViewById(R.id.TV_serv);
            image=(ImageView)itemView.findViewById(R.id.IV_sand);
        }
    }
}
