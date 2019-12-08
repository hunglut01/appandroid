package com.example.loginlogout.listening;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.loginlogout.R;
import com.example.loginlogout.adapter.ReadingAdapter;
import com.example.loginlogout.model.ReadingList;
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

public class listening_photo_list extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    static ArrayList<ReadingList> arr = new ArrayList();
    private ReadingAdapter adapter;
    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_photo_list);
        //init retrofit API........................................................................
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);
        listView = findViewById(R.id.listreading);
        initdata();
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,listening_photo.class);
        intent.putExtra("id",position);
        startActivity(intent);
    }
    public void initdata()
    {
        compositeDisposable.add(API.getlistphototest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ArrayList<ReadingList> temp = inserttoList(s);
                        arr.clear();
                        arr.addAll(temp);
                        adapter = new ReadingAdapter(arr, listening_photo_list.this, R.layout.reading_item);
                        listView.setAdapter(adapter);
                    }
                }));
    }
    public ArrayList<ReadingList> inserttoList(String s) throws JSONException {
        ArrayList<ReadingList> list = new ArrayList<>();
        JSONArray array = new JSONArray(s);
        for (int i = 0; i < array.length(); i++)
        {
            ReadingList gram = new ReadingList();
            JSONObject jsonObject = array.getJSONObject(i);
            gram.name = jsonObject.getString("name");
            gram.number = jsonObject.getString("number");
            list.add(gram);
        }
        return list;
    }
}
