package com.lzg.player.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lzg.player.R;
import com.lzg.player.adapter.MainListAdapter;

import butterknife.BindView;

/**
 *
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.main_list)
    RecyclerView recyclerView;

    private String[] uriStrings = {"http://asp.cntv.lxdns.com/asp/hls/main/0303000a/3/default/7432e61296394abe8bf17dcc5554ba00/main.m3u8?maxbr=850",
            "https://qavoda-media-m3u8.huanxi.com/vod/02a17925-dcc8-4a45-8be3-0c2653244ece.m3u8?pt=2&dt=3&ra=1",
            "http://asp.cntv.lxdns.com/asp/hls/main/0303000a/3/default/7432e61296394abe8bf17dcc5554ba00/main.m3u8?maxbr=850",
            "https://qavoda-media-m3u8.huanxi.com/vod/02a17925-dcc8-4a45-8be3-0c2653244ece.m3u8?pt=2&dt=3&ra=1"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
    }

    public void setupView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MainListAdapter(this,uriStrings));
        PalyerActivity.launch(mContext,uriStrings);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
