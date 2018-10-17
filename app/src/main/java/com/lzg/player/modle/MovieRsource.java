package com.lzg.player.modle;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 *  * Created by 智光 on 2018/10/17 11:07
 *  
 */
public class MovieRsource {

    private static List<MediaItem> movislist;

    public MovieRsource() {
        movislist = new ArrayList<>();
        movislist.add(new MediaItem(Uri.parse("http://asp.cntv.lxdns.com/asp/hls/main/0303000a/3/default/7432e61296394abe8bf17dcc5554ba00/main.m3u8?maxbr=850"), "呵呵 历史天气预报"));
        movislist.add(new MediaItem(Uri.parse("https://agmeijucdnvideo.ixibeiren.com/AGMTV/HFnej55k21/index.m3u8"), "侏罗纪世界"));
        movislist.add(new MediaItem(Uri.parse("https://agmjjzer.ixibeiren.com/20181014/eORYxbtM/index.m3u8"), "巨齿鲨"));
        movislist.add(new MediaItem(Uri.parse("https://agmeijucdnvideo.ixibeiren.com/20180602/TOP567/index.m3u8"), "极限特工3：终极回归 xXx: Return of Xander Cage"));
        movislist.add(new MediaItem(Uri.parse("https://agmeijucdnvideo.ixibeiren.com/20180602/TOP559/index.m3u8"), "速度与激情8 The Fate of the Furious"));
    }

    public List<MediaItem> getMovislist() {
        return movislist;
    }
}
