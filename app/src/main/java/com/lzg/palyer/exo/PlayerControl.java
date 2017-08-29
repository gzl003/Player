package com.lzg.palyer.exo;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlaybackControlView;

/**
 *  * Created by 智光 on 2017/8/29 17:20
 *  播放器的控制层
 */
public class PlayerControl extends PlaybackControlView implements PlaybackControlView.ControlDispatcher, PlaybackControlView.VisibilityListener {

    public PlayerControl(Context context) {
        this(context, null);
    }

    public PlayerControl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 播放暂停
     *
     * @param player
     * @param playWhenReady
     * @return
     */
    @Override
    public boolean dispatchSetPlayWhenReady(Player player, boolean playWhenReady) {
        player.setPlayWhenReady(playWhenReady);
        return true;
    }

    /**
     * 拖拽 seek
     *
     * @param player
     * @param windowIndex
     * @param positionMs
     * @return
     */
    @Override
    public boolean dispatchSeekTo(Player player, int windowIndex, long positionMs) {
        player.seekTo(windowIndex, positionMs);
        return true;
    }

    /**
     * 重复播放
     *
     * @param player
     * @param repeatMode
     * @return
     */
    @Override
    public boolean dispatchSetRepeatMode(Player player, int repeatMode) {
        player.setRepeatMode(repeatMode);
        return true;
    }

    /**
     * 显示隐藏的回调
     *
     * @param visibility
     */
    @Override
    public void onVisibilityChange(int visibility) {

    }
}
