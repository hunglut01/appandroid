package com.example.loginlogout.vocabulary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.example.loginlogout.R;
import com.example.loginlogout.adapter.ListViewIrregularAdapter;
import com.example.loginlogout.model.irregular;
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

public class Irregular_verb_activity extends AppCompatActivity {
    private ActionBar actionBar;
    private SearchView searchView;
    private ArrayList<irregular> arrIv = new ArrayList<>();
    ListView listiv;
    private ListViewIrregularAdapter adapter;
    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irregular_verb_activity);
        //init menu bar
        actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.irregular_verbs));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        listiv = findViewById(R.id.listView_iv);
        //init API
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);
        //getdata from server
        initdata();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iv_activity, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        initSearchIv();
        return super.onCreateOptionsMenu(menu);
    }
    //tim kiem
    public void initSearchIv(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<irregular> arr = new ArrayList<>();
                for(irregular iv : arrIv){
                    if(iv.getInfinitive().contains(s.toLowerCase())) arr.add(iv);
                }
                listiv.setAdapter(new ListViewIrregularAdapter(Irregular_verb_activity.this,arr));
                return true;
            }
        });
    }
    //json to arrrylist
    public ArrayList<irregular> getIrreList(String s) throws JSONException {
        ArrayList<irregular> words = new ArrayList<>();
        JSONArray array = new JSONArray(s);
        for (int i = 0; i < array.length(); i++) {
            irregular irre = new irregular();
            JSONObject jsonObject = array.getJSONObject(i);
            irre.infinitive = jsonObject.getString("Infinitive");
            irre.simple = jsonObject.getString("Simple");
            irre.participle = jsonObject.getString("Participle");
            irre.mean = jsonObject.getString("Meaning");
            words.add(irre);
        }
        return words;
    }
    public void initdata()
    {
        compositeDisposable.add(API.Irregular()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ArrayList<irregular> arr = getIrreList(s);
                        arrIv.addAll(arr);
                        adapter = new ListViewIrregularAdapter(Irregular_verb_activity.this, arrIv);
                        listiv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }));


    }
}
