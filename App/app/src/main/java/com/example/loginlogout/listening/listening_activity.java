package com.example.loginlogout.listening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.loginlogout.R;
import com.squareup.picasso.Picasso;

public class listening_activity extends AppCompatActivity implements View.OnClickListener {
    private ActionBar actionBar;
    public ImageView photo, imgconversation;
    LinearLayout conversation,photograph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_activity);
        actionBar = getSupportActionBar();
        actionBar.setTitle("AppStudyEnglish");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        //
        photograph = findViewById(R.id.photgraph);
        conversation = findViewById(R.id.conversation);
        photograph.setOnClickListener(this);
        conversation.setOnClickListener(this);
        photo = findViewById(R.id.img_photo);
        imgconversation = findViewById(R.id.img_Conversation);
        Picasso.get().load("http://23.101.29.94:1111/images/image_image.jpg").into(photo);
        Picasso.get().load("http://23.101.29.94:1111/images/conversation_image.jpg").into(imgconversation);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photgraph:
            {
                Intent intent = new Intent(this, listening_photo_list.class);
                startActivity(intent);
                break;
            }
            case R.id.conversation:
            {
                Intent intent = new Intent(this, listening_conversation.class);
                startActivity(intent);
                break;
            }
        }
    }
}
