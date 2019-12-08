package com.example.loginlogout.grammar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.loginlogout.R;
import com.example.loginlogout.adapter.GrammarAdapter;
import com.example.loginlogout.model.GrammarModel;
import com.example.loginlogout.retrofit.NODEjs;
import com.example.loginlogout.retrofit.retrofitclient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class grammar_activity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ActionBar actionBar;
    private ListView lstGrammar;
    static ArrayList<GrammarModel> arr = new ArrayList();
    private GrammarAdapter adapter;

    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init retrofit API........................................................................
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);
        setContentView(R.layout.activity_grammar_activity);
        actionBar=getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.grammar));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        lstGrammar = findViewById(R.id.lst_grammar);

        initdata();
        lstGrammar.setOnItemClickListener(this);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }
    public void initdata()
    {
        compositeDisposable.add(API.grammar()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                ArrayList<GrammarModel> temp = inserttoList(s);
                arr.clear();
                arr.addAll(temp);
                adapter=new GrammarAdapter(grammar_activity.this,R.layout.grammar_item,arr);
                lstGrammar.setAdapter(adapter);
            }
        }));
    }
    public ArrayList<GrammarModel> inserttoList(String s) throws JSONException {
        ArrayList<GrammarModel> grammar = new ArrayList<>();
        JSONArray array = new JSONArray(s);
        for (int i = 0; i < array.length(); i++)
        {
            GrammarModel gram = new GrammarModel();
            JSONObject jsonObject = array.getJSONObject(i);
            gram.topic = jsonObject.getString("topic");
            gram.name = jsonObject.getString("name");
            gram.html = jsonObject.getString("html");
            gram.image = jsonObject.getString("image");
            grammar.add(gram);
        }
        return grammar;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, grammar_detail.class);
        intent.putExtra("data",position);
        startActivity(intent);
    }
}
