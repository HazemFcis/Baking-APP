package com.example.hazem.baking_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hazem.baking_app.Baking;
import java.util.List;

/**
 * Created by Hazem on 6/20/2018.
 */

public class Adapter_Details extends RecyclerView.Adapter<Adapter_Details.Holder>{
    private Context context;
    private List<Steps>stepsList;
    public Adapter_Details(Context context, List<Steps> stepsList) {
        this.context = context;
        this.stepsList = stepsList;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.details_item, parent, false);

        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.textView.setText(stepsList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return stepsList.size() ;
    }
    public class Holder extends RecyclerView.ViewHolder {
        TextView textView;
        public Holder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.Tv_itemStep);
        }
    }
}
