package com.example.hazem.baking_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {
      RecyclerView recyclerView;
      int ind;
      List<Baking>bakingList;
      Baking baking1;
      Adapter_Details adapter_details;
      TextView textView1;
      Vplayer vplayer;
      Gson g;
    public DetailsFragment() {
    }

    void set (Vplayer vplayer1){
        this.vplayer=vplayer1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View x= inflater.inflate(R.layout.fragment_details, container, false);
        Bundle bundle = getArguments();

        String venName = bundle.getString("Data");
         recyclerView =(RecyclerView)x.findViewById(R.id.RV_vedios);

        ind=bundle.getInt("Indix");
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("shared",Context.MODE_PRIVATE);
       String s=sharedPreferences.getString("neededData","");
        Gson gson = new Gson();
        gson.toJson(venName);// prints "abcd"
        Baking[] baking=  gson.fromJson(venName,Baking[].class);
        bakingList=new ArrayList<>();
        for (int i=0;i<baking.length;i++){
            bakingList.add(baking[i]);
        }
        baking1=new Baking();
        baking1=bakingList.get(ind);

        textView1=(TextView)x.findViewById(R.id.TV_ingred);
        textView1.setText("Ingredients: \n");
        for (int i=0;i<baking1.getIngredients().size();i++){
            textView1.append(baking1.getIngredients().get(i).getIngredient()+ " : ");
            textView1.append(baking1.getIngredients().get(i).getQuantity()+ "  ");
            textView1.append(baking1.getIngredients().get(i).getMeasure()+ "\n");
        }
        adapter_details = new Adapter_Details(getActivity(), baking1.getSteps());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter_details);
        recyclerView.addOnItemTouchListener(
                new ItClick(getActivity(), recyclerView, new ItClick.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String VideoURL = baking1.getSteps().get(position).getVideoURL();
                        String Description = baking1.getSteps().get(position).getDescription();
                        String thumbnailURL = baking1.getSteps().get(position).getThumbnailURL();
                        g=new Gson();
                        String stepsBean = g.toJson(baking1.getSteps());
                        int stepsBeanSize = bakingList.size();
                        vplayer.set_B(VideoURL, Description, thumbnailURL, stepsBean, position, stepsBeanSize);
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

        return x;
    }


}
