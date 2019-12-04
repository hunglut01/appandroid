package com.example.loginlogout.vocabulary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginlogout.R;
import com.example.loginlogout.adapter.WordAdapter;
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

public class vocabulary_activity extends AppCompatActivity {
    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextView text;
    private RecyclerView lvList, suggest;
    private WordAdapter wordAdapter;
    private WordAdapterSaved wordAdapterSaved;
    private LinearLayoutManager linearLayoutManager,linearLayoutManager1 = new LinearLayoutManager(this);
    private ArrayList<Word> wordList = new ArrayList<>();
    private ArrayList<Word> wordListSaved = new ArrayList<>();
    private ActionBar actionBar;
    private SearchView searchView;
    private LinearLayout vietanh, batquytac;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        //mapping...................................................................................
        vietanh = findViewById(R.id.vietanh_layout);
        batquytac = findViewById(R.id.batquytac_layout);



        //menu...........................................
        actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.search));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        //init API
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);
        vietanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vocabulary_activity.this,vietanh_activity.class );
                startActivity(intent);
            }
        });
        //
        batquytac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vocabulary_activity.this,Irregular_verb_activity.class);
                startActivity(intent);
            }
        });

        suggest = findViewById(R.id.suggest_word);
        lvList = findViewById(R.id.lvList);
        load();
        wordAdapterSaved = new WordAdapterSaved(wordListSaved,this);
        lvList.setAdapter(wordAdapterSaved);
        wordAdapter = new WordAdapter(wordList, this);
        linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager = new LinearLayoutManager(this);
        suggest.setAdapter(wordAdapter);
        suggest.setLayoutManager(linearLayoutManager);
        lvList.setLayoutManager(linearLayoutManager1);

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
        getMenuInflater().inflate(R.menu.menu_word_activity, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        initSearchIv();
        return super.onCreateOptionsMenu(menu);
    }

    public void initSearchIv() {
        load();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                load();
                wordList.clear();
                if(s.equals(""))
                {
                    suggest.setVisibility(View.GONE);
                    wordList.clear();
                    wordAdapter = new WordAdapter(wordList,vocabulary_activity.this);
                    linearLayoutManager = new LinearLayoutManager(vocabulary_activity.this);
                    suggest.setAdapter(wordAdapter);
                    wordAdapter.notifyDataSetChanged();
                }
                else
                {
                    suggest.setVisibility(View.VISIBLE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new initAV().execute(s);
                        }
                    });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                wordList.clear();
                if(s.equals(""))
                {
                    wordList.clear();
                    wordAdapter = new WordAdapter(wordList,vocabulary_activity.this);
                    linearLayoutManager = new LinearLayoutManager(vocabulary_activity.this);
                    suggest.setAdapter(wordAdapter);
                    wordAdapter.notifyDataSetChanged();
                    suggest.setVisibility(View.GONE);

                }
                else
                {
                    suggest.setVisibility(View.VISIBLE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new initAV().execute(s);
                        }
                    });
                }
                return false;
            }
        });
    }

    public void load()
    {
        sessionmanager session;
        session = new sessionmanager(getApplicationContext());
        String id = session.getInuser();
        wordListSaved.clear();
        try {
            compositeDisposable.add(API.loadWord(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            ArrayList<Word> arr = getWordList(s);
                            wordListSaved.addAll(arr);
                            wordAdapterSaved = new WordAdapterSaved(wordListSaved,vocabulary_activity.this);
                            lvList.setAdapter(wordAdapterSaved);
                            linearLayoutManager1 = new LinearLayoutManager(vocabulary_activity.this);
                            lvList.setLayoutManager(linearLayoutManager1);
                            wordAdapterSaved.notifyDataSetChanged();
                        }
                    }));
        }
        catch (Exception e)
        {

        }

    }
    //get data anh viet
    public class initAV extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            compositeDisposable.add(API.Searchav(strings[0])
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            if(s.contains("Không tìm thấy từ này!"))
                            {
                                Toast.makeText(vocabulary_activity.this, ""+s, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                ArrayList<Word> arr = getWordList(s);
                                wordList.addAll(arr);
                                wordAdapter = new WordAdapter(wordList,vocabulary_activity.this);
                                linearLayoutManager = new LinearLayoutManager(vocabulary_activity.this);
                                suggest.setAdapter(wordAdapter);
                                wordAdapter.notifyDataSetChanged();
                            }


                        }
                    }));
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


}
