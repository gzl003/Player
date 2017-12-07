package com.lzg.player.imple;

import android.view.MotionEvent;

/**
 *  * Created by 智光 on 2017/12/7 10:32
 *  
 */
public interface PlayerMovieListener {
    /**
     * 播放器移动开始
     *
     * @param ev
     */
    void onPlayerDown(MotionEvent ev);

    /**
     * 播放器移动
     */
    void onPlayerMovie(MotionEvent ev,float move);

    /**
     * 播放器移动手势结束
     */
    void onPayerUp(MotionEvent ev);
}
