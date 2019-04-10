package com.example.hazem.baking_app;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * The configuration screen for the {@link NewAppWidget NewAppWidget} AppWidget.
 */
public class NewAppWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.example.hazem.baking_app.NewAppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    RecyclerView recyclerView;
    Adapter_FristScreen adapter_fristScreen;
    List<Baking> bakingList;
    Baking baking;
    String  s1="";
    Boolean t=false;
    int n=1;
    private  static final String url="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";


    public NewAppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.new_app_widget_configure);
        recyclerView = (RecyclerView) findViewById(R.id.rv_baking_widget);
        t = getResources().getBoolean(R.bool.tabletMood);
        if (t != null && t) n = 4;
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

                adapter_fristScreen = new Adapter_FristScreen(NewAppWidgetConfigureActivity.this, bakingList);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(NewAppWidgetConfigureActivity.this, n);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(adapter_fristScreen);
                recyclerView.addOnItemTouchListener(
                        new ItClick(NewAppWidgetConfigureActivity.this, recyclerView, new ItClick.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                              //TextView  textView1=(TextView)findViewById(R.id.appwidget_text);
                                String s="Ingredients: \n";
                                for (int i=0;i<bakingList.get(position).getIngredients().size();i++){
                                    s += bakingList.get(position).getIngredients().get(i).getIngredient() + " : ";
                                    s += bakingList.get(position).getIngredients().get(i).getQuantity() + " ";
                                    s +=bakingList.get(position).getIngredients().get(i).getMeasure() + "\n";
                                }
                                saveTitlePref(NewAppWidgetConfigureActivity.this, mAppWidgetId, s);
                                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(NewAppWidgetConfigureActivity.this);
                                NewAppWidget.updateAppWidget(NewAppWidgetConfigureActivity.this, appWidgetManager, mAppWidgetId);

                                // Make sure we pass back the original appWidgetId
                                Intent resultValue = new Intent();
                                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                                setResult(RESULT_OK, resultValue);
                                finish();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        })
                ); }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }


    }
}

