package com.lzg.palyer.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jiongbull.jlog.JLog;
import com.lzg.palyer.R;
import com.lzg.palyer.exo.EventLogger;
import com.lzg.palyer.exo.PlayerControl;
import com.lzg.palyer.helper.UIHelper;

/**
 * http://asp.cntv.lxdns.com/asp/hls/main/0303000a/3/default/7432e61296394abe8bf17dcc5554ba00/main.m3u8?maxbr=850
 */
public class MainActivity extends AppCompatActivity implements PlaybackControlView.VisibilityListener {

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private EventLogger eventLogger;
    private Handler mainHandler = new Handler();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simpleExoPlayerView = findViewById(R.id.player_view);
        simpleExoPlayerView.setControllerVisibilityListener(this);
        simpleExoPlayerView.requestFocus();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
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
    public void onDestroy() {
        super.onDestroy();
        releaseAdsLoader();
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
        }
        Uri uri = Uri.parse("http://asp.cntv.lxdns.com/asp/hls/main/0303000a/3/default/7432e61296394abe8bf17dcc5554ba00/main.m3u8?maxbr=850");
        MediaSource mediaSource = new HlsMediaSource(uri, dataSourceFactory, mainHandler, eventLogger);
        // 准备播放
        player.prepare(mediaSource);
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

    }

    /**
     * 释放播放器
     */
    private void releasePlayer() {
//        if (player != null) {
//            player.release();
//        }
    }


    private void releaseAdsLoader() {
        if (player != null) {
            player.release();
        }
    }

    /**
     * @param visibility
     */
    @Override
    public void onVisibilityChange(int visibility) {
//        UIHelper.shortToast("onVisibilityChange");
    }
}
