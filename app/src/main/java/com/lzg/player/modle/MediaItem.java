package com.lzg.player.modle;

import android.net.Uri;

import java.io.Serializable;

/**
 *  * Created by 智光 on 2018/10/17 11:09
 *  
 */

public class MediaItem implements Serializable {

    public MediaItem(Uri murl, String name) {
        this.murl = murl;
        this.name = name;
    }

    public Uri murl;
    public String name;
}
