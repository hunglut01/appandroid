package com.example.loginlogout.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginlogout.R;

class WordHolder2 extends RecyclerView.ViewHolder {

    public final TextView tvWord;
    public final ImageView delete;

    public WordHolder2(@NonNull View itemView) {
        super(itemView);
        delete = itemView.findViewById(R.id.icdelete);
        tvWord = itemView.findViewById(R.id.tvText1);
    }
}
