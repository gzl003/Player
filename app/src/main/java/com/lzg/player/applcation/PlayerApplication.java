package com.lzg.player.applcation;

import android.app.Application;

import com.google.android.exoplayer2.util.Util;
import com.jiongbull.jlog.JLog;
import com.lzg.player.BuildConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class PlayerApplication extends Application {
    private static PlayerApplication instance;
    protected String userAgent;

    public static PlayerApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        userAgent = Util.getUserAgent(this, "ExoPlayerDemo");

        JLog.init(this).setDebug(BuildConfig.DEBUG);
        JLog.getSettings().setDebug(true);

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("TAG"))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

}