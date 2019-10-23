package com.example.loginlogout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class grammar_activity extends AppCompatActivity {
    private TextView mytext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_activity);
        mytext = findViewById(R.id.grammar_txt);
        mytext.setText(Html.fromHtml(""));
    }
}
