package com.example.hazem.baking_app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

public class VedioPlayerFragment extends Fragment {


     SimpleExoPlayer exoPlayer;                                            //Done
     Timeline.Window window;
     SimpleExoPlayerView simpleExoPlayerView;                              //Done
     BandwidthMeter bandwidthMeter;
    ImageView ImageView;
     DataSource.Factory mediaDataSourceFactory;
     DefaultTrackSelector trackSelector;
    String vidURL, Desc, thURL;
     boolean shouldAutoPlay;
     int currentWindow;
    TextView DescriptionT;
     long playbackPosition;
    public VedioPlayerFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View x= inflater.inflate(R.layout.fragment_vedio_player, container, false);
        Bundle sentBundle = getArguments();
        ImageView = (ImageView) x.findViewById(R.id.IV_VEdio);
        DescriptionT = (TextView) x.findViewById(R.id.TV_DES);
        simpleExoPlayerView = (SimpleExoPlayerView) x.findViewById(R.id.player);
        Desc = sentBundle.getString("Description");
        shouldAutoPlay = sentBundle.getBoolean("shouldAutoPlay");
        thURL = sentBundle.getString("thumbnailURL");
        currentWindow = sentBundle.getInt("currentWindow");
        vidURL = sentBundle.getString("videoURL");
        playbackPosition = sentBundle.getLong("playbackPosition");
        if (savedInstanceState != null) {
            shouldAutoPlay = savedInstanceState.getBoolean("shouldAutoPlay");
            playbackPosition = savedInstanceState.getLong("playbackPosition");
            currentWindow = savedInstanceState.getInt("currentWindow");
        }

        if (!thURL.isEmpty())
            Picasso.with(getActivity()).load(thURL).into(ImageView);
        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        DescriptionT.append("\n\n" + Desc);
        mediaDataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();
        return x;
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            playbackPosition = exoPlayer.getCurrentPosition();
            currentWindow = exoPlayer.getCurrentWindowIndex();
            shouldAutoPlay = exoPlayer.getPlayWhenReady();
            exoPlayer.release();
            exoPlayer = null;
            trackSelector = null;
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
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || exoPlayer == null)) {
            initializePlayer();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
    private void initializePlayer() {

        simpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

        simpleExoPlayerView.setPlayer(exoPlayer);

        exoPlayer.setPlayWhenReady(shouldAutoPlay);
        exoPlayer.seekTo(currentWindow, playbackPosition);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(vidURL),
                mediaDataSourceFactory, extractorsFactory, null, null);

        exoPlayer.prepare(mediaSource, true, false);
    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("playbackPosition", playbackPosition);
        outState.putInt("currentWindow", currentWindow);
        outState.putBoolean("shouldAutoPlay", shouldAutoPlay);
    }
}
