package com.example.loginlogout.listening;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.loginlogout.R;
import com.example.loginlogout.model.listeningdata;
import com.example.loginlogout.retrofit.NODEjs;
import com.example.loginlogout.retrofit.retrofitclient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class listening_photo extends AppCompatActivity implements View.OnClickListener {
    private Button stop, forward, back, number, unfocus1,unfocus2,unfocus3,submit,unfocus;
    private Button[] btn = new Button[4];
    private int[] btn_id = {R.id.a,R.id.b,R.id.c,R.id.d};
    private SeekBar seekBar;
    private Runnable runnable;
    private Handler handler;
    private MediaPlayer mediaPlayer;
    private ImageView img;
    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArrayList<com.example.loginlogout.model.listeningdata> listeningdata = new ArrayList<>();
    int i = 0 , right, temp = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_photo);
        //init retrofit API........................................................................
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);
        //
        for(int i = 0; i < btn_id.length; i++)
        {
            btn[i] = findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }
        img = findViewById(R.id.img);
        stop = findViewById(R.id.btn_stop);
        stop.setOnClickListener(this);
        seekBar = findViewById(R.id.audio_status);
        number = findViewById(R.id.txt_numques);
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(this);
        forward = findViewById(R.id.btn_next);
        forward.setOnClickListener(this);
        submit = findViewById(R.id.btn_submit);
        submit.setOnClickListener(this);
        handler = new Handler();
        back.setEnabled(false);
        Picasso.get().load("http://23.101.29.94:1111/anh1_de1").fit().into(img);
        playaudio("http://23.101.29.94:3000/audio/photo/1");
        insertlist();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_back:
            {
                clearFocus();
                i = i - 1;
                number.setText(i+1+"/10");
                loadImage(i);
                if(i==0)
                {
                    checkbutton(forward,back);
                }
                else {
                    forward.setEnabled(true);
                }
                loadAns(i);
                if(temp == 1)
                {
                    review(i);
                }
                break;
            }
            case R.id.btn_next:
            {
                clearFocus();
                i = i + 1;
                loadImage(i);
                number.setText(i+1+"/10");
                if(i==9)
                {
                    checkbutton(back,forward);
                }
                else{
                    back.setEnabled(true);
                }
                loadAns(i);
                if(temp == 1)
                {
                    review(i);
                }
                break;
            }
            case R.id.btn_submit:
            {
                temp =1;
                stop.setEnabled(false);
                mediaPlayer.release();
                playaudio("http://23.101.29.94:3000/audio/photo/1");
                checkResult();
            }
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
            case R.id.a:
            {
                setFocus(unfocus,btn[0]);
                saveAns(i,"A");
                break;
            }
            case R.id.b:
            {
                setFocus(unfocus,btn[1]);
                saveAns(i,"B");
                break;
            }
            case R.id.c:
            {
                setFocus(unfocus,btn[2]);
                saveAns(i,"C");
                break;
            }
            case R.id.d:
            {
                setFocus(unfocus,btn[3]);
                saveAns(i,"D");
                break;
            }
        }
    }
    public void checkbutton(Button btn_unfocus, Button btn_focus)//button next, back
    {
        btn_unfocus.setEnabled(true);
        btn_focus.setEnabled(false);
    }
    public void loadImage(int i)
    {
        switch (i)
        {
            case 0:
            {
                Picasso.get().load("http://23.101.29.94:1111/anh1_de1").fit().into(img);
                break;
            }
            case 1:
            {
                Picasso.get().load("http://23.101.29.94:1111/anh2_de1").fit().into(img);
                break;
            }
            case 2:
            {
                Picasso.get().load("http://23.101.29.94:1111/anh3_de1").fit().into(img);
                break;
            }
            case 3:
            {
                Picasso.get().load("http://23.101.29.94:1111/anh4_de1").fit().into(img);
                break;
            }
            case 4:
            {
                Picasso.get().load("http://23.101.29.94:1111/anh5_de1").fit().into(img);
                break;
            }
            case 5:
            {
                Picasso.get().load("http://23.101.29.94:1111/anh6_de1").fit().into(img);
                break;
            }
            case 6:
            {
                Picasso.get().load("http://23.101.29.94:1111/anh7_de1").fit().into(img);
                break;
            }
            case 7:
            {
                Picasso.get().load("http://23.101.29.94:1111/anh8_de1").fit().into(img);
                break;
            }
            case 8:
            {
                Picasso.get().load("http://23.101.29.94:1111/anh9_de1").fit().into(img);
                break;
            }
            case 9:
            {
                Picasso.get().load("http://23.101.29.94:1111/anh10_de1").fit().into(img);
                break;
            }
        }
    }
    public void insertlist()
    {
        try
        {
            compositeDisposable.add(API.listeningbyphoto()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            ArrayList<listeningdata> a = initlistquestion(s);
                            listeningdata.addAll(a);
                        }
                    }));
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
    }
    public ArrayList<listeningdata> initlistquestion(String s) throws JSONException {
        ArrayList<listeningdata> arr = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(s);
        for(int i = 0 ; i < jsonArray.length() ; i++)
        {
            listeningdata temp = new listeningdata();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            temp.anwa = jsonObject.getString("optionA");
            temp.anwb = jsonObject.getString("optionB");
            temp.anwc = jsonObject.getString("optionC");
            temp.anwd = jsonObject.getString("optionD");
            temp.anwser = jsonObject.getString("result");
            temp.user_anw = " ";
            arr.add(temp);
        }
        return arr;
    }
    public void saveAns(int i,String s)
    {
        listeningdata.get(i).setUser_anw(s);
    }
    public void loadAns(int i)
    {
        switch (listeningdata.get(i).getUser_anw())
        {
            case "A":
            {
                setFocus(unfocus,btn[0]);
                break;
            }
            case "B":
            {
                setFocus(unfocus,btn[1]);
                break;
            }
            case "C":
            {
                setFocus(unfocus,btn[2]);
                break;
            }
            case "D":
            {
                setFocus(unfocus,btn[3]);
                break;
            }
        }
    }
    public void checkResult()
    {
        right = 0;
        for(listeningdata a : listeningdata)
        {
            if(a.getUser_anw()!="")
            {
                if(a.getUser_anw().equals(a.getAnwser()))
                {
                    right = right + 1;
                }
            }

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kết quả!!!");
        builder.setMessage("Số câu đúng: "+ right);
        builder.setPositiveButton("Xem lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int ia) {
                review(i);
            }
        });
        builder.setNegativeButton("Thoát!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void playaudio(String s)//chay audio
    {
        stop.setText(">");
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(s);

        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mediaPlayer.getDuration());
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
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.seekTo(0);
                stop.setText(">");
            }
        });
    }
    //thay đổi seekbar audio........................................................................
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
    private void setFocus(Button btn_unfocus, Button btn_focus){
        if(btn_unfocus == null)
        {
            btn_focus.setBackgroundResource(R.drawable.checkkkk);
        }
        else
        {
            btn_unfocus.setBackgroundResource(R.drawable.backgroud);
            btn_focus.setBackgroundResource(R.drawable.checkkkk);
        }
        this.unfocus = btn_focus;
    }
    public void clearFocus()
    {
        for(int i = 0; i < btn.length ; i++)
        {
            btn[i].setBackgroundResource(R.drawable.backgroud);

        }
        unfocus = null;
    }
    public void review(int i)
    {
        if(!listeningdata.get(i).getAnwser().equals(listeningdata.get(i).getUser_anw()))
        {
            switch (listeningdata.get(i).getAnwser())
            {
                case "A":
                {
                    btn[0].setBackgroundResource(R.drawable.right);
                    break;
                }
                case "B":
                {
                    btn[1].setBackgroundResource(R.drawable.right);
                    break;
                }
                case "C":
                {
                    btn[2].setBackgroundResource(R.drawable.right);
                    break;
                }
                case "D":
                {
                    btn[3].setBackgroundResource(R.drawable.right);
                    break;
                }
            }
        }
    }
    @Override
    protected void onPause() {
        mediaPlayer.release();
        playaudio("http://10.0.2.2:3000/cau1");
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo!");
        builder.setMessage("Bạn có muốn thoát hay không?");
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int ia) {
                return;
            }
        });
        builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                listening_photo.super.onBackPressed();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //super.onBackPressed();
    }
}
