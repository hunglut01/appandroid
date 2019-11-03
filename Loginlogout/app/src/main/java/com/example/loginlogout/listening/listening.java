package com.example.loginlogout.listening;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.loginlogout.R;

import java.io.IOException;

public class listening extends AppCompatActivity implements View.OnClickListener {
    private Button stop, forward, back;
    private SeekBar seekBar;
    private Runnable runnable;
    private Handler handler;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);
        //mapping..................................................................................
        stop = findViewById(R.id.btn_stop);
        forward = findViewById(R.id.btn_forward);
        back = findViewById(R.id.btn_back);
        seekBar = findViewById(R.id.audio_status);
        handler = new Handler();
        //decode...................................................................................\

        String url = "https://khoapham.vn/KhoaPhamTraining/laptrinhios/EmCuaNgayHomQua.mp3";
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();



        //su kien click0
        stop.setOnClickListener(this);
        back.setOnClickListener(this);
        forward.setOnClickListener(this);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mediaPlayer.getDuration());
                //mediaPlayer.start();
                changeSeekbar();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    private void changeSeekbar()
    {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        if(mediaPlayer.isPlaying())
        {
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeSeekbar();
                }
            };
            handler.postDelayed(runnable, 1000);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_stop:
            {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    stop.setText(">");
                }
                else
                {
                    mediaPlayer.start();
                    stop.setText("II");
                    changeSeekbar();
                }
                break;
            }
            case R.id.btn_forward:
            {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+2000);
                break;
            }
            case R.id.btn_back:
            {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-2000);
                break;
            }
        }
    }
}
