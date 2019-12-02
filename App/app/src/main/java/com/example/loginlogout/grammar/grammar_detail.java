package com.example.loginlogout.grammar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.example.loginlogout.R;

public class grammar_detail extends AppCompatActivity {
    private ActionBar actionBar;
    private WebView mytext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_detail);
        mytext = findViewById(R.id.webview);
        actionBar=getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.grammar));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        int id = getIntent().getExtras().getInt("data");
        String data = grammar_activity.arr.get(id).getHtml();

        mytext.getSettings().setJavaScriptEnabled(true);
        mytext.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");
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

}
