package com.lzg.player.item;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lzg.player.R;

/**
 *  * Created by 智光 on 2017/9/12 11:52
 *  
 */
public class MainlistItem extends RecyclerView.ViewHolder {
   public TextView itemText;

    public MainlistItem(View itemView) {
        super(itemView);
        itemText = itemView.findViewById(R.id.item_text);
    }
}
