package com.lzg.player.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzg.player.R;
import com.lzg.player.activity.PalyerActivity;
import com.lzg.player.item.MainlistItem;

/**
 *  * Created by 智光 on 2017/9/12 11:50
 *  
 */
public class MainListAdapter extends RecyclerView.Adapter<MainlistItem> {

    private Context mContext;

    public MainListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public MainlistItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainlistItem(LayoutInflater.from(mContext).inflate(R.layout.main_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MainlistItem holder, int position) {
        holder.itemText.setText("" + position);
        holder.itemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PalyerActivity.launch(mContext);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
