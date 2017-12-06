package com.lzg.player.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jiongbull.jlog.JLog;
import com.lzg.player.R;
import com.lzg.player.exo.EventLogger;
import com.lzg.player.exo.ExoPlayerView;
import com.lzg.player.exo.PlayerControl;
import com.lzg.player.helper.UIHelper;

/**
 * 播放页
 */
public class PalyerActivity extends BaseActivity {

    public static final String PLAY_URL = "play_url";
    public static final String PLAY_URLS = "play_url_list";

    private ExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private EventLogger eventLogger;
    private Handler mainHandler = new Handler();

    private Context mContext;
    private Uri[] uris;
    private String playurl;
    private String[] playurls;

    public static void launch(Context mContext, String url) {
        Intent intent = new Intent(mContext, PalyerActivity.class);
        intent.putExtra(PLAY_URL, url);
        mContext.startActivity(intent);
//        StartUtils.startActivity(mContext, PalyerActivity.class);
    }

    public static void launch(Context mContext, String[] url) {
        Intent intent = new Intent(mContext, PalyerActivity.class);
        intent.putExtra(PLAY_URLS, url);
        mContext.startActivity(intent);
//        StartUtils.startActivity(mContext, PalyerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palyer);
        simpleExoPlayerView = findViewById(R.id.player_view);
        simpleExoPlayerView.requestFocus();
        playurl = getIntent().getStringExtra(PLAY_URL);
        playurls = getIntent().getStringArrayExtra(PLAY_URLS);
        initializePlayer();
//        startActivity(new Intent(new Intent(this, LoginActivity.class)));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null) {
            //开始播放
            player.setPlayWhenReady(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            //暂停播放
            player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    /**
     * 初始化播放器
     */
    private void initializePlayer() {
        mContext = this;
        if (player == null) {
            // 1 create a default TrackSelector
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            // 打印日志
            eventLogger = new EventLogger(trackSelector);
            // 2. 创建播放器
            player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
            // 绑定播放器到simpleExoPlayerView
            simpleExoPlayerView.setPlayer(player);
            //设置播放器的控制层
            simpleExoPlayerView.setControlDispatcher(new PlayerControl(this));
            // 默认带宽测量
            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
            dataSourceFactory = new DefaultDataSourceFactory(mContext,
                    Util.getUserAgent(mContext, "ExoPlayerDemo"), defaultBandwidthMeter);
//            simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);//设置视频的填充类型
        }
        if (playurls != null && playurls.length > 0) {
            uris = new Uri[playurls.length];
            for (int i = 0; i < playurls.length; i++) {
                uris[i] = Uri.parse(playurls[i]);
            }
        } else {
            uris = new Uri[1];
            uris[0] = Uri.parse(playurl);
        }

        //播放地址的的集合

        //处理资源的集合
        MediaSource[] mediaSources = new MediaSource[uris.length];
        for (int i = 0; i < uris.length; i++) {
            mediaSources[i] = new HlsMediaSource(uris[i], dataSourceFactory, mainHandler, eventLogger);
        }
        //串联Media资源（合并所有的播放资源）
        MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0]
                : new ConcatenatingMediaSource(mediaSources);
        // 准备播放资源
        player.prepare(mediaSource, false, true);
        // 自动播放
        player.setPlayWhenReady(true);
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
                JLog.d("timeline" + timeline + ">>>manifest>>>" + manifest);
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                JLog.d("onTracksChanged");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                JLog.d("onLoadingChanged");
            }

            /**
             * 播放器状态改变
             * @param playWhenReady 是否开始播放 true 播放 false暂停
             * @param playbackState 播放器的状态
             */
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                JLog.d("onPlayerStateChanged 播放器的状态》》》" + playbackState);
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        UIHelper.shortToast("播放缓冲中......");
                        break;
                    case Player.STATE_READY:
                        String string = playWhenReady ? "开始" : "暂停";
                        UIHelper.shortToast("播放" + string);
                        break;
                    case Player.STATE_ENDED:
                        UIHelper.shortToast("播放结束");
                        break;
                    case Player.STATE_IDLE:
                        UIHelper.shortToast("播放器空闲中");
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                JLog.d("onRepeatModeChanged" + repeatMode);
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                JLog.d("onPlayerError");
            }

            @Override
            public void onPositionDiscontinuity() {
                JLog.d("onPositionDiscontinuity");
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                JLog.d("onPlaybackParametersChanged");
            }
        });
        player.addListener(eventLogger);
        player.setAudioDebugListener(eventLogger);
        player.setVideoDebugListener(eventLogger);
        player.setMetadataOutput(eventLogger);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(simpleExoPlayerView!= null && player != null){
            simpleExoPlayerView.onConfigurationChanged(newConfig);


        }

        super.onConfigurationChanged(newConfig);
    }

    /**
     * 释放播放器
     */
    private void releasePlayer() {
        if (player != null) {
            player.release();
        }
    }
}
