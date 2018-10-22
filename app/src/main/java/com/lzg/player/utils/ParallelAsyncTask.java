package com.lzg.player.utils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.util.concurrent.RejectedExecutionException;

/**
 * 异步任务
 *
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract class ParallelAsyncTask<Params, Progress, Result> extends
        AsyncTask<Params, Progress, Result> {
    @SafeVarargs
    @SuppressLint("NewApi")
    public final void executeOnPoolExecutor(Params... params)
            throws RejectedExecutionException {
        try {
            super.executeOnExecutor(THREAD_POOL_EXECUTOR, params);
        } catch (RejectedExecutionException localRejectedExecutionException) {
            localRejectedExecutionException.printStackTrace();
        }
    }
}
