package com.lzg.player.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lzg.player.R;
import com.lzg.player.adapter.MainListAdapter;
import com.lzg.player.modle.MovieRsource;
import com.lzg.player.modle.RemoteMovie;
import com.lzg.player.utils.JsonUtil;
import com.socks.library.KLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

/**
 *
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.main_list)
    RecyclerView recyclerView;
    private MovieRsource movieRsource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMovielist();
        setupView();
    }

    private void initMovielist() {
        OkHttpUtils
                .get()
                .url("https://easy-mock.com/mock/5bc6f05ba6883e28f97cdcd3/gzl003/movielist")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        KLog.e(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        KLog.e(response);
                        RemoteMovie remoteMovie = JsonUtil.fromJson(response, RemoteMovie.class);
                    }
                });
    }

    public void setupView() {
        movieRsource = new MovieRsource();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MainListAdapter(this, movieRsource.getMovislist()));
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.recyclrr_divider));
        recyclerView.addItemDecoration(divider);

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
