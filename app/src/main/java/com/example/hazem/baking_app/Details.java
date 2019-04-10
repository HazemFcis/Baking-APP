package com.example.hazem.baking_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.content.res.Configuration;
import android.widget.Toast;


public class Details extends AppCompatActivity implements Vplayer {
    boolean x=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        DetailsFragment mMainFragment = new DetailsFragment();
        mMainFragment.setArguments(bundle);
        mMainFragment.set(Details.this);
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_Dails, mMainFragment, "").commit();
        if ( findViewById(R.id.layout_Vidio)!=null) {
            x = true;
        }
    }
    @Override
    public void set_B(String videoURL, String Description, String thumbnailURL, String stepsBean, int position, int stepsBeanSize) {

        if (!x) {
            Intent i = new Intent(this, VedioPlayerActivity.class);
            i.putExtra("Description", Description);
            i.putExtra("stepsBeanSize", stepsBeanSize);
            i.putExtra("stepsBean", stepsBean);
            i.putExtra("videoURL", videoURL);
            i.putExtra("position", position);
            i.putExtra("thumbnailURL", thumbnailURL);
            startActivity(i);
             } else {
            VedioPlayerFragment vedioPlayerFragment = new VedioPlayerFragment();
            Bundle extras = new Bundle();
            extras.putString("Description", Description);
            extras.putString("stepsBean", stepsBean);
            extras.putString("thumbnailURL", thumbnailURL);
            extras.putString("videoURL", videoURL);
            extras.putInt("stepsBeanSize", stepsBeanSize);
            extras.putInt("position", position);
            vedioPlayerFragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_Vidio, vedioPlayerFragment, "").commit();
          }
    }
}
