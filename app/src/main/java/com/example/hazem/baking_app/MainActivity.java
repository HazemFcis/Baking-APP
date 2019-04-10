package com.example.hazem.baking_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  RecyclerView recyclerView;
  Adapter_FristScreen adapter_fristScreen;
  List<Baking>bakingList;
  Baking baking;
    private Parcelable savedRecyclerLayoutState;
    private LinearLayoutManager mGridLayoutManager;
    final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;
    GridLayoutManager gridLayoutManager;
    String  s1="";
    Boolean t=false;
   int n=1;
  private  static final String url="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv_baking);
        t = getResources().getBoolean(R.bool.tabletMood);
        if (t != null && t) n = 3;
        if(savedRecyclerLayoutState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
        StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder=new GsonBuilder();
                Gson gson=gsonBuilder.create();
                Baking[] baking=  gson.fromJson(response,Baking[].class);
                bakingList=new ArrayList<>();
                for (int i=0;i<baking.length;i++){
                    bakingList.add(baking[i]);
                }
                s1 = gson.toJson(bakingList);

                adapter_fristScreen = new Adapter_FristScreen(MainActivity.this, bakingList);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this, n);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(adapter_fristScreen);
                recyclerView.addOnItemTouchListener(
                        new ItClick(MainActivity.this, recyclerView, new ItClick.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent i = new Intent(MainActivity.this, Details.class);
                                //i.putExtra("Index", position);
                                Bundle bundle = new Bundle();
                                bundle.putString("Data", s1);
                                i.putExtras(bundle);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        })
                );

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
       RequestQueue requestQueue= Volley.newRequestQueue(this);
       requestQueue.add(stringRequest);
    }
    ////////////////////////////////////// ُُ

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT,recyclerView.getLayoutManager().onSaveInstanceState());
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
    }

}
