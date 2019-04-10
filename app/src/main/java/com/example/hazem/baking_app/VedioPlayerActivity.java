package com.example.hazem.baking_app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VedioPlayerActivity extends AppCompatActivity {

     SimpleExoPlayer exoPlayer;
    boolean land;
     Timeline.Window window;
    int pos, stepsBeanSize;
     DataSource.Factory mediaDataSourceFactory;
     SimpleExoPlayerView simpleExoPlayerView;
     DefaultTrackSelector trackSelector;
     Gson g;
     BandwidthMeter bandwidthMeter;
     String vidURL, stepsBean;
     boolean shouldAutoPlay;
     int currentWindow;
     long playbackPosition;
    List<Steps>stepsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_player);
        Intent sentIntent = getIntent();
        Bundle sentBundle = sentIntent.getExtras();
        land = getResources().getBoolean(R.bool.landscapeMood);
        if (savedInstanceState != null) {

            stepsBean = savedInstanceState.getString("stepsBean");
            vidURL = savedInstanceState.getString("videoURL");
            playbackPosition = savedInstanceState.getLong("playbackPosition");
            stepsBeanSize = savedInstanceState.getInt("stepsBeanSize");
            pos = savedInstanceState.getInt("position");
            shouldAutoPlay = savedInstanceState.getBoolean("shouldAutoPlay");
            currentWindow = savedInstanceState.getInt("currentWindow");
        } else {
            stepsBean = sentBundle.getString("stepsBean");
            pos = sentBundle.getInt("position");
            stepsBeanSize = sentBundle.getInt("stepsBeanSize");
            vidURL = sentBundle.getString("videoURL");
        }
        g = new Gson();
        g.toJson(stepsBean);
        Steps[] steps=  g.fromJson(stepsBean,Steps[].class);
        stepsList=new ArrayList<>();
        for (int i=0;i<steps.length;i++){
            stepsList.add(steps[i]);
        }
        if (!land) {

            VedioPlayerFragment vedioPlayerFragment = new VedioPlayerFragment();
            sentBundle.putBoolean("shouldAutoPlay", shouldAutoPlay);
            sentBundle.putString("Description", stepsList.get(pos).getDescription());
            sentBundle.putString("thumbnailURL", stepsList.get(pos).getThumbnailURL());
            sentBundle.putLong("playbackPosition", playbackPosition);
            sentBundle.putInt("currentWindow", currentWindow);
            sentBundle.putString("videoURL", stepsList.get(pos).getVideoURL());
            vedioPlayerFragment.setArguments(sentBundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_Vidio, vedioPlayerFragment, "").commit();
        }
    }
    private void releasePlayer() {
        if (land) {
            if (exoPlayer != null) {
                playbackPosition = exoPlayer.getCurrentPosition();
                currentWindow = exoPlayer.getCurrentWindowIndex();
                shouldAutoPlay = exoPlayer.getPlayWhenReady();
                exoPlayer.release();
                exoPlayer = null;
                trackSelector = null;
            }
        }
    }
    private void initializePlayer() {

        if (land) {
            simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoplayeri);

            shouldAutoPlay = true;
            bandwidthMeter = new DefaultBandwidthMeter();
            mediaDataSourceFactory = new DefaultDataSourceFactory(VedioPlayerActivity.this, Util.getUserAgent(VedioPlayerActivity.this, "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
            window = new Timeline.Window();

            simpleExoPlayerView.requestFocus();

            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);

            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            exoPlayer = ExoPlayerFactory.newSimpleInstance(VedioPlayerActivity.this, trackSelector);

            simpleExoPlayerView.setPlayer(exoPlayer);

            exoPlayer.setPlayWhenReady(shouldAutoPlay);
            exoPlayer.seekTo(currentWindow, playbackPosition);

            DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(vidURL),
                    mediaDataSourceFactory, extractorsFactory, null, null);

            exoPlayer.prepare(mediaSource, true, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || exoPlayer == null)) {
            initializePlayer();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", pos);
        outState.putString("thumbnailURL", stepsList.get(pos).getThumbnailURL());
        outState.putLong("playbackPosition", playbackPosition);
        outState.putInt("currentWindow", currentWindow);
        outState.putString("Description", stepsList.get(pos).getDescription());
        outState.putBoolean("shouldAutoPlay", shouldAutoPlay);
        outState.putString("stepsBean", stepsBean);
        outState.putInt("stepsBeanSize", stepsBeanSize);
        outState.putString("videoURL", stepsList.get(pos).getVideoURL());


    }
}
