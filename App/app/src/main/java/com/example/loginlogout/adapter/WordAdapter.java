package com.example.loginlogout.adapter;

import android.content.Context;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;



import com.example.loginlogout.R;

import com.example.loginlogout.model.Word;
import com.example.loginlogout.retrofit.NODEjs;
import com.example.loginlogout.retrofit.retrofitclient;
import com.example.loginlogout.sessionmanager;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class WordAdapter extends RecyclerView.Adapter<WordHolder>{

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
                NODEjs API;
                CompositeDisposable compositeDisposable = new CompositeDisposable();
                Retrofit retrofit = retrofitclient.getInstance();
                API = retrofit.create(NODEjs.class);
                sessionmanager session;
                session = new sessionmanager(context.getApplicationContext());
                String id = session.getInuser();
                try
                {
                    compositeDisposable.add(API.saveWord(id,word.word,word.html)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                }
                            }));
                }catch(Exception E)
                {

                }

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
