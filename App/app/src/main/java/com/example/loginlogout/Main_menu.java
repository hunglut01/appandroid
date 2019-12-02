package com.example.loginlogout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.loginlogout.grammar.grammar_activity;
import com.example.loginlogout.reading.reading_activity;
import com.example.loginlogout.vocabulary.vocabulary_activity;

public class Main_menu extends AppCompatActivity {
    LinearLayout vocabulary,reading,grammar,listening,logout;
    sessionmanager session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        session = new sessionmanager(getApplicationContext());
        String name = session.getUserDetails();
        vocabulary = findViewById(R.id.card_vocabulary);
        reading = findViewById(R.id.card_reading);
        grammar = findViewById(R.id.card_grammar);
        logout = findViewById(R.id.card_logout);
        listening = findViewById(R.id.card_listen);
        //
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //check login
        session.checkLogin();


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
}
