package com.lzg.player.modle;

import java.io.Serializable;
import java.util.List;

/**
 *  * Created by 智光 on 2018/10/17 16:59
 *  
 */

public class RemoteMovie implements Serializable {
    public List<RemoteMovie> list;
    public String name;
    public String url;
}
