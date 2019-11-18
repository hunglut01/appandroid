package com.example.loginlogout.adapter;

import android.content.Context;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;



import com.example.loginlogout.R;

import com.example.loginlogout.model.Word;

import java.util.List;


public class WordAdapter extends RecyclerView.Adapter<WordHolder> {

    private List<Word> wordList;

    private Context context;

    public WordAdapter(List<Word> wordList, Context context) {
        this.wordList = wordList;
        this.context = context;
    }



    @NonNull
    @Override
    public WordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.word, parent, false);

        return new WordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordHolder holder, int position) {

        final Word word = wordList.get(position);
        holder.tvWord.setText(word.word);

        holder.tvWord.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        holder.tvWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder
                        = new AlertDialog.Builder(context);
                builder.setTitle(word.word);
                builder.setMessage(Html.fromHtml(word.html));
                builder.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return wordList.size();
    }
}
