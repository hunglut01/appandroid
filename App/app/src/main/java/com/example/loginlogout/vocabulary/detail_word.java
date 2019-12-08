package com.example.loginlogout.vocabulary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginlogout.R;

public class detail_word extends AppCompatActivity {
    TextView textView;
    String data;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_word);
        textView = findViewById(R.id.webview);
        //menu...........................................
        actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.search));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        data = getIntent().getExtras().getString("data");
        //Toast.makeText(this, ""+data, Toast.LENGTH_SHORT).show();
        textView.setText(Html.fromHtml(data));
        //webView.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");*/
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

}
