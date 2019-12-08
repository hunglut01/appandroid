package com.example.loginlogout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.example.loginlogout.adapter.HistoryAdapter;
import com.example.loginlogout.grammar.grammar_activity;
import com.example.loginlogout.model.history;
import com.example.loginlogout.reading.reading_activity;
import com.example.loginlogout.reading.reading_test_activity;
import com.example.loginlogout.retrofit.NODEjs;
import com.example.loginlogout.retrofit.retrofitclient;
import com.example.loginlogout.vocabulary.vocabulary_activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Main_menu extends AppCompatActivity {
    private LinearLayout vocabulary,reading,grammar,listening,logout,historytable, test,main;
    private sessionmanager session;
    private ImageView close,icon;
    private TextView name;
    private int i = 0;
    private ArrayList<history> arr = new ArrayList<>();
    private ListView listiv;
    private HistoryAdapter adapter;
    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        session = new sessionmanager(getApplicationContext());
        vocabulary = findViewById(R.id.card_vocabulary);
        reading = findViewById(R.id.card_reading);
        grammar = findViewById(R.id.card_grammar);
        logout = findViewById(R.id.card_logout);
        listening = findViewById(R.id.card_listen);
        historytable = findViewById(R.id.tablehistory);
        close = findViewById(R.id.closeTable);
        main = findViewById(R.id.ll);
        name = findViewById(R.id.name);
        icon = findViewById(R.id.icon);
        listiv = findViewById(R.id.listhistory);
        test = findViewById(R.id.card_totaltest);
        //init API
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);
        //
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //

        //check login
        session.checkLogin();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Main_menu.this,R.anim.closetable);
                Animation animation1 = AnimationUtils.loadAnimation(Main_menu.this,R.anim.opentable);
                main.startAnimation(animation1);
                main.setVisibility(View.VISIBLE);
                historytable.startAnimation(animation);
                historytable.setVisibility(View.GONE);
                i =0;
            }
        });
        vocabulary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Main_menu.this, vocabulary_activity.class);
                startActivity(intent1);
            }
        });
        reading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Main_menu.this, reading_activity.class);
                startActivity(intent1);
            }
        });
        grammar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Main_menu.this, grammar_activity.class);
                startActivity(intent1);
            }
        });
        listening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Main_menu.this, com.example.loginlogout.listening.listening_activity.class);
                startActivity(intent1);
            }
        });
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_menu.this,totaltest_activity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

    }
    public void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("Bạn có muốn đăng xuất không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                session.logoutUser();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu,menu);
        session = new sessionmanager(getApplicationContext());
        String name = session.getUserDetails();
        MenuItem item = menu.findItem(R.id.name); // here itemIndex is int
        item.setTitle(name);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon:{

            }
            case R.id.name:
            {

            }
            case R.id.history:
            {
                if(i == 0)
                {
                    initdata();
                    historytable.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(Main_menu.this,R.anim.opentable);
                    Animation animation1 = AnimationUtils.loadAnimation(Main_menu.this,R.anim.closetable);
                    main.startAnimation(animation1);
                    main.setVisibility(View.GONE);
                    historytable.startAnimation(animation);
                    i = 1;
                }

                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void initdata()
    {
        sessionmanager session;
        session = new sessionmanager(getApplicationContext());
        String id = session.getInuser();
        compositeDisposable.add(API.loadScore(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ArrayList<history> arrhis = getlist(s);
                        arr.clear();
                        arr.addAll(arrhis);
                        adapter = new HistoryAdapter(Main_menu.this, arr);
                        listiv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }));


    }
    public ArrayList<history> getlist(String s) throws JSONException {
        ArrayList<history> temp = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(s);
        for(int i = 0 ; i < jsonArray.length(); i++)
        {
            history history = new history();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            history.name = jsonObject.getString("type");
            history.score = jsonObject.getString("score");
            history.time = jsonObject.getString("date");
            temp.add(history);
        }
        return temp;

    }
}
