package com.example.loginlogout.vocabulary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.loginlogout.R;
import com.example.loginlogout.adapter.VAWordAdapter;
import com.example.loginlogout.adapter.VAWordAdapterSaved;
import com.example.loginlogout.adapter.WordAdapterSaved;
import com.example.loginlogout.model.Word;
import com.example.loginlogout.retrofit.NODEjs;
import com.example.loginlogout.retrofit.retrofitclient;
import com.example.loginlogout.sessionmanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class vietanh_activity extends AppCompatActivity {
    private ActionBar actionBar;
    private SearchView searchView;
    private ArrayList<Word> wordList = new ArrayList<>();
    private ArrayList<Word> wordListSaved = new ArrayList<>();
    private VAWordAdapter wordAdapter;
    private VAWordAdapterSaved wordAdapterSaved;
    private LinearLayoutManager linearLayoutManager,linearLayoutManager1 = new LinearLayoutManager(this);
    private RecyclerView lvList, suggest;
    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vietanh);
        //menu...........................................
        actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.searchva));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        //
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);
        //
        suggest = findViewById(R.id.suggest_word1);
        lvList = findViewById(R.id.lvlist1);
        load();
        wordAdapterSaved = new VAWordAdapterSaved(wordListSaved,this);
        lvList.setAdapter(wordAdapterSaved);
        wordAdapter = new VAWordAdapter(wordList, this);
        linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager = new LinearLayoutManager(this);
        suggest.setAdapter(wordAdapter);
        suggest.setLayoutManager(linearLayoutManager);
        lvList.setLayoutManager(linearLayoutManager1);
        ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_iv_activity, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        initSearchIv();
        return super.onCreateOptionsMenu(menu);
    }
    //get data viet anh
    public class initVA extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            compositeDisposable.add(API.Searchva(strings[0])
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            if(s.contains("Không tìm thấy từ này!"))
                            {
                                Toast.makeText(vietanh_activity.this, ""+s, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                ArrayList<Word> arr = getWordList(s);
                                wordList.addAll(arr);
                                wordAdapter = new VAWordAdapter(wordList,vietanh_activity.this);
                                linearLayoutManager = new LinearLayoutManager(vietanh_activity.this);
                                suggest.setAdapter(wordAdapter);
                                wordAdapter.notifyDataSetChanged();
                            }
                        }
                    }));
            return null;
        }
    }
    public ArrayList<Word> getWordList(String s) throws JSONException {
        ArrayList<Word> words = new ArrayList<>();
        JSONArray array = new JSONArray(s);
        for (int i = 0; i < array.length(); i++) {
            Word word = new Word();
            JSONObject jsonObject = array.getJSONObject(i);
            word.word = jsonObject.getString("word");
            word.html = jsonObject.getString("html");
            words.add(word);
        }
        return words;
    }
    public void load()
    {
        sessionmanager session;
        session = new sessionmanager(getApplicationContext());
        String id = session.getInuser();
        wordListSaved.clear();
        compositeDisposable.add(API.loadWordva(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ArrayList<Word> arr = getWordList(s);
                        wordListSaved.addAll(arr);
                        wordAdapterSaved = new VAWordAdapterSaved(wordListSaved,vietanh_activity.this);
                        lvList.setAdapter(wordAdapterSaved);
                        linearLayoutManager1 = new LinearLayoutManager(vietanh_activity.this);
                        lvList.setLayoutManager(linearLayoutManager1);
                        wordAdapterSaved.notifyDataSetChanged();
                    }
                }));
    }
    public void initSearchIv() {
        load();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                load();
                wordList.clear();
                if (s.equals("")) {
                    suggest.setVisibility(View.GONE);
                    wordList.clear();
                    wordAdapter = new VAWordAdapter(wordList, vietanh_activity.this);
                    linearLayoutManager = new LinearLayoutManager(vietanh_activity.this);
                    suggest.setAdapter(wordAdapter);
                    wordAdapter.notifyDataSetChanged();


                } else {
                    suggest.setVisibility(View.VISIBLE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new initVA().execute(s);
                        }
                    });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                load();
                wordList.clear();
                if (s.equals("")) {
                    wordList.clear();
                    wordAdapter = new VAWordAdapter(wordList, vietanh_activity.this);
                    linearLayoutManager = new LinearLayoutManager(vietanh_activity.this);
                    suggest.setAdapter(wordAdapter);
                    wordAdapter.notifyDataSetChanged();
                    suggest.setVisibility(View.GONE);

                } else {
                    suggest.setVisibility(View.VISIBLE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new initVA().execute(s);
                        }
                    });
                }
                return false;
            }
        });
    }
}

