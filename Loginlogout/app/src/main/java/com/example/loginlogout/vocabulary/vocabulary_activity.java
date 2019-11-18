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
import com.example.loginlogout.model.Word;
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

public class vocabulary_activity extends AppCompatActivity {
    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextView text;
    private RecyclerView lvList;
    private WordAdapter wordAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Word> wordList = new ArrayList<>();
    private ActionBar actionBar;
    private SearchView searchView;
    private LinearLayout anhviet, vietanh, batquytac;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabulary_activity);
        //mapping...................................................................................
        anhviet = findViewById(R.id.anhviet_layout);
        vietanh = findViewById(R.id.vietanh_layout);
        batquytac = findViewById(R.id.batquytac_layout);
        text = findViewById(R.id.textView);
        //menu...........................................
        actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.search));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        //init API
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);

        //
        batquytac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vocabulary_activity.this,Irregular_verb_activity.class);
                startActivity(intent);
            }
        });
        anhviet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("Anh-Việt");
                i = 0;
            }
        });
        vietanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("Việt-Anh");
                i = 1;
            }
        });
        lvList = findViewById(R.id.lvList);
        wordAdapter = new WordAdapter(wordList, this);
        linearLayoutManager = new LinearLayoutManager(this);
        lvList.setAdapter(wordAdapter);
        lvList.setLayoutManager(linearLayoutManager);

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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                wordList.clear();
                if(s.isEmpty())
                {
                    wordList.clear();
                    wordAdapter = new WordAdapter(wordList,vocabulary_activity.this);
                    linearLayoutManager = new LinearLayoutManager(vocabulary_activity.this);
                    lvList.setAdapter(wordAdapter);
                    wordAdapter.notifyDataSetChanged();

                }
                else
                {
                    if(i == 0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new initAV().execute(s);
                            }
                        });
                        //initdata(s);
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new initVA().execute(s);
                            }
                        });
                        //initdataVA(s);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                wordList.clear();
                if(s.isEmpty())
                {
                    wordList.clear();
                    wordAdapter = new WordAdapter(wordList,vocabulary_activity.this);
                    linearLayoutManager = new LinearLayoutManager(vocabulary_activity.this);
                    lvList.setAdapter(wordAdapter);
                    wordAdapter.notifyDataSetChanged();

                }
                else
                {
                    if(i == 0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new initAV().execute(s);
                            }
                        });
                        //initdata(s);
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new initVA().execute(s);
                            }
                        });
                        //initdataVA(s);
                    }
                }
                return false;
            }
        });
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
                                lvList.setAdapter(wordAdapter);
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
                                Toast.makeText(vocabulary_activity.this, ""+s, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                ArrayList<Word> arr = getWordList(s);
                                wordList.addAll(arr);
                                wordAdapter = new WordAdapter(wordList,vocabulary_activity.this);
                                linearLayoutManager = new LinearLayoutManager(vocabulary_activity.this);
                                lvList.setAdapter(wordAdapter);
                                wordAdapter.notifyDataSetChanged();
                            }


                        }
                    }));
            return null;
        }
    }

}
