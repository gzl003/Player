# Player

![image](https://github.com/gzl003/Palyer/blob/master/img/device-2017-08-30-164747.png)



* Exoplayer的简单使用
* PlayerControl 的控制层自定义
* 单个资源多个资源的播放方式

 * 播放器状态改变
 
 playWhenReady 是否开始播放 true 播放 false暂停     playbackState 播放器的状态
   
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
           
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