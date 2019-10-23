package com.example.loginlogout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.loginlogout.vocabulary.vocabulary_activity;

public class Main_menu extends AppCompatActivity {
    TextView inname;
    LinearLayout vocabulary,reading,grammar,listening,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        inname = findViewById(R.id.edt_name);
        vocabulary = findViewById(R.id.card_vocabulary);
        reading = findViewById(R.id.card_reading);
        Intent intent = getIntent();
        String name =intent.getStringExtra("name");
        inname.setText(name);
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

    }


}
