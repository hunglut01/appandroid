package com.example.loginlogout.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginlogout.R;
import com.example.loginlogout.model.Word;
import com.example.loginlogout.retrofit.NODEjs;
import com.example.loginlogout.retrofit.retrofitclient;
import com.example.loginlogout.vocabulary.vietanh_activity;
import com.example.loginlogout.vocabulary.vocabulary_activity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class VAWordAdapterSaved extends RecyclerView.Adapter<WordHolder2>{
    private List<Word> wordList;

    private Context context;

    public VAWordAdapterSaved(List<Word> wordList, Context context) {
        this.wordList = wordList;
        this.context = context;
    }


    @NonNull
    @Override
    public WordHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.wordsaved, parent, false);

        return new WordHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordHolder2 holder, int position) {

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
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NODEjs API;
                CompositeDisposable compositeDisposable = new CompositeDisposable();
                Retrofit retrofit = retrofitclient.getInstance();
                API = retrofit.create(NODEjs.class);
                compositeDisposable.add(API.deleteWordva(word.word)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, vietanh_activity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                        }));
            }
        });
    }
    @Override
    public int getItemCount() {
        return wordList.size();
    }
}
