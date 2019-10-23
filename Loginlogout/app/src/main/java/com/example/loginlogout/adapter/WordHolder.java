package com.example.loginlogout.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginlogout.R;

class WordHolder extends RecyclerView.ViewHolder {

    public final TextView tvWord;

    public WordHolder(@NonNull View itemView) {
        super(itemView);

        tvWord = itemView.findViewById(R.id.tvText);
    }
}
