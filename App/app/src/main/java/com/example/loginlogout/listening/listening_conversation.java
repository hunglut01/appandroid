package com.example.loginlogout.listening;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginlogout.R;
import com.example.loginlogout.model.listeningdata;
import com.example.loginlogout.model.readingdata;
import com.example.loginlogout.retrofit.NODEjs;
import com.example.loginlogout.retrofit.retrofitclient;
import com.example.loginlogout.sessionmanager;

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

public class listening_conversation extends AppCompatActivity implements View.OnClickListener {
    private Button stop, forward, back, number, unfocus1,unfocus2,unfocus3,submit;
    private Button[] btn1 = new Button[4];
    private int[] btn_id1 = {R.id.a_1,R.id.b_1,R.id.c_1,R.id.d_1};
    private Button[] btn2 = new Button[4];
    private int[] btn_id2 = {R.id.a_2,R.id.b_2,R.id.c_2,R.id.d_2};
    private Button[] btn3 = new Button[4];
    private int[] btn_id3 = {R.id.a_3,R.id.b_3,R.id.c_3,R.id.d_3};
    private SeekBar seekBar;
    private Runnable runnable;
    private Handler handler;
    private MediaPlayer mediaPlayer;
    private TextView a1,b1,c1,d1,a2,b2,c2,d2,a3,b3,c3,d3;
    private ArrayList<listeningdata> listeningdata = new ArrayList<>();
    int i = 1,temp =0;
    private int right ;
    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);
        //init retrofit API........................................................................
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);
        //mapping..................................................................................
        stop = findViewById(R.id.btn_stop);
        seekBar = findViewById(R.id.audio_status);
        a1 = findViewById(R.id.a1);
        b1 = findViewById(R.id.b1);
        c1 = findViewById(R.id.c1);
        d1 = findViewById(R.id.d1);
        a2 = findViewById(R.id.a2);
        b2 = findViewById(R.id.b2);
        c2 = findViewById(R.id.c2);
        d2 = findViewById(R.id.d2);
        a3 = findViewById(R.id.a3);
        b3 = findViewById(R.id.b3);
        c3 = findViewById(R.id.c3);
        d3 = findViewById(R.id.d3);
        number = findViewById(R.id.txt_numques);
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(this);
        forward = findViewById(R.id.btn_next);
        forward.setOnClickListener(this);
        submit = findViewById(R.id.btn_submit);
        handler = new Handler();
        back.setEnabled(false);
        playaudio("http://23.101.29.94:3000/cau1");
        insertlist();
        for(int i = 0; i < btn1.length ; i++)
        {
            btn1[i] = findViewById(btn_id1[i]);
            btn2[i] = findViewById(btn_id2[i]);
            btn3[i] = findViewById(btn_id3[i]);
            btn1[i].setOnClickListener(this);
            btn2[i].setOnClickListener(this);
            btn3[i].setOnClickListener(this);
        }

        submit.setOnClickListener(this);
        stop.setOnClickListener(this);
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
    //get dữ liệu từ server
    public void insertlist()
    {
        try
        {
            compositeDisposable.add(API.listening()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            ArrayList<listeningdata> a = initlistquestion(s);
                            listeningdata.addAll(a);
                            loadquestion(listeningdata.get(0),listeningdata.get(1),listeningdata.get(2));
                        }
                    }));
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
    }
    //xử lý json dữ liệu
    public ArrayList<listeningdata> initlistquestion(String s) throws JSONException {
        ArrayList<listeningdata> arr = new ArrayList<>();
        JSONArray array = new JSONArray(s);
        for (int i = 0; i < array.length(); i++) {
            listeningdata temp = new listeningdata();
            JSONObject jsonObject = array.getJSONObject(i);
            temp.question = jsonObject.getString("question");
            temp.anwa = jsonObject.getString("optionA");
            temp.anwb = jsonObject.getString("optionB");
            temp.anwc = jsonObject.getString("optionC");
            temp.anwd = jsonObject.getString("optionD");
            temp.anwser = jsonObject.getString("result");
            temp.user_anw = "";
            arr.add(temp);
        }
        return arr;
    }
    //load question...................................................................
    public void loadquestion(listeningdata a, listeningdata b, listeningdata c)
    {
        a1.setText(a.getAnwa());
        b1.setText(a.anwb);
        c1.setText(a.anwc);
        d1.setText(a.anwd);
        //
        a2.setText(b.anwa);
        b2.setText(b.anwb);
        c2.setText(b.anwc);
        d2.setText(b.anwd);
        //
        a3.setText(c.anwa);
        b3.setText(c.anwb);
        c3.setText(c.anwc);
        d3.setText(c.anwd);

    }
    //su kien cua tat ca cac nut
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_submit:
            {

                stop.setEnabled(false);
                mediaPlayer.release();
                playaudio("http://23.101.29.94:3000/cau1");
                temp =1;
                if(submit.getText().toString() == getResources().getString(R.string.submit))
                {
                    checkResult();
                    submit.setText(getResources().getString(R.string.exit));
                }
                else
                {
                    onBackPressed();
                }
                break;
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
            case R.id.btn_back:
            {
                i = i - 1;
                if(i==1)
                {
                    checkbutton(forward,back);
                }
                else {
                    forward.setEnabled(true);
                }
                unfocusALL();
                Loadanswer(i);
                switchaudio(i);
                if(temp ==1 )
                {
                    review(i);
                }
                break;
            }
            case R.id.btn_next:
            {
                i = i + 1;
                switchaudio(i);
                if(i==6)
                {
                    checkbutton(back,forward);
                }
                else{
                    back.setEnabled(true);
                }
                unfocusALL();
                Loadanswer(i);
                if(temp ==1 )
                {
                    review(i);
                }
                break;
            }
            case R.id.a_1:
            {
                initANS(i,"A","","");
                setFocus1(unfocus1,btn1[0]);
                break;
            }
            case R.id.b_1:
            {
                initANS(i,"B","","");
                setFocus1(unfocus1,btn1[1]);
                break;
            }
            case R.id.c_1:
            {
                initANS(i,"C","","");
                setFocus1(unfocus1,btn1[2]);
                break;
            }
            case R.id.d_1:
            {
                initANS(i,"D","","");
                setFocus1(unfocus1,btn1[3]);
                break;
            }
            case R.id.a_2:
            {
                initANS(i,"","A","");
                setFocus2(unfocus2,btn2[0]);
                break;
            }
            case R.id.b_2:
            {
                initANS(i,"","B","");
                setFocus2(unfocus2,btn2[1]);
                break;
            }
            case R.id.c_2:
            {
                initANS(i,"","C","");
                setFocus2(unfocus2,btn2[2]);
                break;
            }
            case R.id.d_2:
            {
                initANS(i,"","D","");
                setFocus2(unfocus2,btn2[3]);
                break;
            }
            case R.id.a_3:
            {
                initANS(i,"","","A");
                setFocus3(unfocus3,btn3[0]);
                break;
            }
            case R.id.b_3:
            {
                initANS(i,"","","B");
                setFocus3(unfocus3,btn3[1]);
                break;
            }
            case R.id.c_3:
            {
                initANS(i,"","","C");
                setFocus3(unfocus3,btn3[2]);
                break;
            }
            case R.id.d_3:
            {
                initANS(i,"","","D");
                setFocus3(unfocus3,btn3[3]);
                break;
            }
        }
    }
    public void switchaudio(int i)//switch trang
    {
        switch (i)
        {
            case 1:
            {
                mediaPlayer.release();
                number.setText("3/18");
                loadquestion(listeningdata.get(0),listeningdata.get(1),listeningdata.get(2));
                playaudio("http://23.101.29.94:3000/cau1");
                break;
            }
            case 2:
            {
                mediaPlayer.release();
                number.setText("6/18");
                loadquestion(listeningdata.get(3),listeningdata.get(4),listeningdata.get(5));
                playaudio("http://23.101.29.94:3000/cau2");
                break;
            }
            case 3:
            {
                mediaPlayer.release();
                number.setText("9/18");
                loadquestion(listeningdata.get(6),listeningdata.get(7),listeningdata.get(8));
                playaudio("http://23.101.29.94:3000/cau3");
                break;
            }
            case 4:
            {
                mediaPlayer.release();
                number.setText("12/18");
                loadquestion(listeningdata.get(9),listeningdata.get(10),listeningdata.get(11));
                playaudio("http://23.101.29.94:3000/cau4");
                break;
            }
            case 5:
            {
                mediaPlayer.release();
                number.setText("15/18");
                loadquestion(listeningdata.get(12),listeningdata.get(13),listeningdata.get(14));
                playaudio("http://23.101.29.94:3000/cau5");
                break;
            }
            case 6:
            {
                mediaPlayer.release();
                number.setText("18/18");
                loadquestion(listeningdata.get(15),listeningdata.get(16),listeningdata.get(17));
                playaudio("http://23.101.29.94:3000/cau6");
                break;
            }
        }
    }
    private void unfocusALL()//bỏ đánh dấu toàn bộ khi sang trang khác
    {
        for(int i = 0; i < btn1.length ; i++)
        {
            btn1[i].setBackgroundResource(R.drawable.backgroud);
            btn2[i].setBackgroundResource(R.drawable.backgroud);
            btn3[i].setBackgroundResource(R.drawable.backgroud);
        }
        unfocus1 = null;
        unfocus2 = null;
        unfocus3 = null;

    }
    //xu ly click dap an cau 1
    private void setFocus1(Button btn_unfocus, Button btn_focus){
        if(btn_unfocus == null)
        {
            btn_focus.setBackgroundResource(R.drawable.checkkkk);
        }
        else
        {
            btn_unfocus.setBackgroundResource(R.drawable.backgroud);
            btn_focus.setBackgroundResource(R.drawable.checkkkk);
        }
        this.unfocus1 = btn_focus;
    }//xu ly click dap an cau 2
    private void setFocus2(Button btn_unfocus, Button btn_focus){
        if(btn_unfocus == null)
        {
            btn_focus.setBackgroundResource(R.drawable.checkkkk);
        }
        else
        {
            btn_unfocus.setBackgroundResource(R.drawable.backgroud);
            btn_focus.setBackgroundResource(R.drawable.checkkkk);
        }
        this.unfocus2 = btn_focus;
    }
    //xu ly click dap an cau 3
    private void setFocus3(Button btn_unfocus, Button btn_focus){
        if(btn_unfocus == null)
        {
            btn_focus.setBackgroundResource(R.drawable.checkkkk);
        }
        else
        {
            btn_unfocus.setBackgroundResource(R.drawable.backgroud);
            btn_focus.setBackgroundResource(R.drawable.checkkkk);
        }
        this.unfocus3 = btn_focus;
    }
    public void checkbutton(Button btn_unfocus, Button btn_focus)//button next, back
    {
        btn_unfocus.setEnabled(true);
        btn_focus.setEnabled(false);
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
    //lưu đáp án vừa đánh...........................................................................
    public void initANS(int i,String s,String s1, String s2)
    {
        switch (i)
        {
            case 1:
            {
                if(s != "")
                {
                    listeningdata.get(0).setUser_anw(s);
                }
                if(s1 != "")
                {
                    listeningdata.get(1).setUser_anw(s1);
                }
                if(s2 != "")
                {
                    listeningdata.get(2).setUser_anw(s2);
                }

                break;
            }
            case 2:
            {
                if(s != "")
                {
                    listeningdata.get(3).setUser_anw(s);
                }
                if(s1 != "")
                {
                    listeningdata.get(4).setUser_anw(s1);
                }
                if(s2 != "")
                {
                    listeningdata.get(5).setUser_anw(s2);
                }
                break;
            }
            case 3:
            {
                if(s != "")
                {
                    listeningdata.get(6).setUser_anw(s);
                }
                if(s1 != "")
                {
                    listeningdata.get(7).setUser_anw(s1);
                }
                if(s2 != "")
                {
                    listeningdata.get(8).setUser_anw(s2);
                }
                break;
            }
            case 4:
            {
                if(s != "")
                {
                    listeningdata.get(9).setUser_anw(s);
                }
                if(s1 != "")
                {
                    listeningdata.get(10).setUser_anw(s1);
                }
                if(s2 != "")
                {
                    listeningdata.get(11).setUser_anw(s2);
                }
                break;
            }
            case 5:
            {
                if(s != "")
                {
                    listeningdata.get(12).setUser_anw(s);
                }
                if(s1 != "")
                {
                    listeningdata.get(13).setUser_anw(s1);
                }
                if(s2 != "")
                {
                    listeningdata.get(14).setUser_anw(s2);
                }
                break;
            }
            case 6:
            {
                if(s != "")
                {
                    listeningdata.get(15).setUser_anw(s);
                }
                if(s1 != "")
                {
                    listeningdata.get(16).setUser_anw(s1);
                }
                if(s2 != "")
                {
                    listeningdata.get(17).setUser_anw(s2);
                }
                break;
            }
        }
    }
    //load lại đáp án đã đánh.......................................................................
    public void Loadanswer(int i)
    {
        switch (i)
        {
            case 1:
            {
                switch (listeningdata.get(0).user_anw)
                {
                    case "A":
                    {
                        setFocus1(unfocus1,btn1[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus1(unfocus1,btn1[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus1(unfocus1,btn1[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus1(unfocus1,btn1[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                switch (listeningdata.get(1).user_anw)
                {
                    case "A":
                    {
                        setFocus2(unfocus2,btn2[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus2(unfocus2,btn2[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus2(unfocus2,btn2[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus2(unfocus2,btn2[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                switch (listeningdata.get(2).user_anw)
                {
                    case "A":
                    {
                        setFocus3(unfocus3,btn3[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus3(unfocus3,btn3[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus3(unfocus3,btn3[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus3(unfocus3,btn3[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
            }
            case 2:
            {
                switch (listeningdata.get(3).user_anw)
                {
                    case "A":
                    {
                        setFocus1(unfocus1,btn1[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus1(unfocus1,btn1[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus1(unfocus1,btn1[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus1(unfocus1,btn1[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                switch (listeningdata.get(4).user_anw)
                {
                    case "A":
                    {
                        setFocus2(unfocus2,btn2[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus2(unfocus2,btn2[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus2(unfocus2,btn2[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus2(unfocus2,btn2[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                switch (listeningdata.get(5).user_anw)
                {
                    case "A":
                    {
                        setFocus3(unfocus3,btn3[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus3(unfocus3,btn3[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus3(unfocus3,btn3[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus3(unfocus3,btn3[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
            }
            case 3:
            {
                switch (listeningdata.get(6).user_anw)
                {
                    case "A":
                    {
                        setFocus1(unfocus1,btn1[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus1(unfocus1,btn1[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus1(unfocus1,btn1[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus1(unfocus1,btn1[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                switch (listeningdata.get(7).user_anw)
                {
                    case "A":
                    {
                        setFocus2(unfocus2,btn2[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus2(unfocus2,btn2[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus2(unfocus2,btn2[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus2(unfocus2,btn2[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                switch (listeningdata.get(8).user_anw)
                {
                    case "A":
                    {
                        setFocus3(unfocus3,btn3[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus3(unfocus3,btn3[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus3(unfocus3,btn3[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus3(unfocus3,btn3[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
            }
            case 4:
            {
                switch (listeningdata.get(9).user_anw)
                {
                    case "A":
                    {
                        setFocus1(unfocus1,btn1[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus1(unfocus1,btn1[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus1(unfocus1,btn1[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus1(unfocus1,btn1[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                switch (listeningdata.get(10).user_anw)
                {
                    case "A":
                    {
                        setFocus2(unfocus2,btn2[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus2(unfocus2,btn2[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus2(unfocus2,btn2[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus2(unfocus2,btn2[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                switch (listeningdata.get(11).user_anw)
                {
                    case "A":
                    {
                        setFocus3(unfocus3,btn3[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus3(unfocus3,btn3[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus3(unfocus3,btn3[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus3(unfocus3,btn3[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
            }
            case 5:
            {
                switch (listeningdata.get(12).user_anw)
                {
                    case "A":
                    {
                        setFocus1(unfocus1,btn1[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus1(unfocus1,btn1[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus1(unfocus1,btn1[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus1(unfocus1,btn1[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                switch (listeningdata.get(13).user_anw)
                {
                    case "A":
                    {
                        setFocus2(unfocus2,btn2[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus2(unfocus2,btn2[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus2(unfocus2,btn2[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus2(unfocus2,btn2[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                switch (listeningdata.get(14).user_anw)
                {
                    case "A":
                    {
                        setFocus3(unfocus3,btn3[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus3(unfocus3,btn3[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus3(unfocus3,btn3[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus3(unfocus3,btn3[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
            }
            case 6:
            {
                switch (listeningdata.get(15).user_anw)
                {
                    case "A":
                    {
                        setFocus1(unfocus1,btn1[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus1(unfocus1,btn1[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus1(unfocus1,btn1[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus1(unfocus1,btn1[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                switch (listeningdata.get(15).user_anw)
                {
                    case "A":
                    {
                        setFocus2(unfocus2,btn2[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus2(unfocus2,btn2[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus2(unfocus2,btn2[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus2(unfocus2,btn2[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                switch (listeningdata.get(17).user_anw)
                {
                    case "A":
                    {
                        setFocus3(unfocus3,btn3[0]);
                        break;
                    }
                    case "B":
                    {
                        setFocus3(unfocus3,btn3[1]);
                        break;
                    }
                    case "C":
                    {
                        setFocus3(unfocus3,btn3[2]);
                        break;
                    }
                    case "D":
                    {
                        setFocus3(unfocus3,btn3[3]);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
            }
        }
    }
    //kiem tra ket qua..............................................................................
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
        for(int i = 0; i < btn1.length ; i++)
        {
            btn1[i] = findViewById(btn_id1[i]);
            //btn[i].setEnabled(false);
        }
        if(btn1[1].isEnabled())
        {
            saveScore(Integer.toString(right));
            for(int i = 0; i < btn1.length ; i++)
            {
                btn1[i].setEnabled(false);
                btn2[i].setEnabled(false);
                btn3[i].setEnabled(false);
            }
        }
        else
        {

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kết quả!!!");
        builder.setMessage("Số câu đúng: "+ right);
        builder.setPositiveButton("Xem lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int bi) {
                review(i);
            }
        });
        builder.setNegativeButton("Thoát!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                onBackPressed();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //
    public void review(int i)
    {
        switch (i) {
            case 1:
            {
                review2(0,1,2);
                break;
            }
            case 2:
            {
                review2(3,4,5);
                break;
            }
            case 3:
            {
                review2(6,7,8);
                break;
            }
            case 4:
            {
                review2(9,10,11);
                break;
            }
            case 5:
            {
                review2(12,13,14);
                break;
            }
            case 6:
            {
                review2(15,16,17);
                break;
            }
        }
    }
    public void review2(int i, int i1, int i2)
    {
        if(!listeningdata.get(i).getUser_anw().equals(listeningdata.get(i).getAnwser()))
        {
            switch (listeningdata.get(i).getAnwser())
            {
                case "A":
                {
                    btn1[0].setBackgroundResource(R.drawable.right);
                    break;
                }
                case "B":
                {
                    btn1[1].setBackgroundResource(R.drawable.right);
                    break;
                }
                case "C":
                {
                    btn1[2].setBackgroundResource(R.drawable.right);
                    break;
                }
                case "D":
                {
                    btn1[3].setBackgroundResource(R.drawable.right);
                    break;
                }
            }
        }
        if(!listeningdata.get(i1).getUser_anw().equals(listeningdata.get(i1).getAnwser()))
        {
            switch (listeningdata.get(i1).getAnwser())
            {
                case "A":
                {
                    btn2[0].setBackgroundResource(R.drawable.right);
                    break;
                }
                case "B":
                {
                    btn2[1].setBackgroundResource(R.drawable.right);
                    break;
                }
                case "C":
                {
                    btn2[2].setBackgroundResource(R.drawable.right);
                    break;
                }
                case "D":
                {
                    btn2[3].setBackgroundResource(R.drawable.right);
                    break;
                }
            }
        }
        if(!listeningdata.get(i2).getUser_anw().equals(listeningdata.get(i2).getAnwser()))
        {
            switch (listeningdata.get(i2).getAnwser())
            {
                case "A":
                {
                    btn3[0].setBackgroundResource(R.drawable.right);
                    break;
                }
                case "B":
                {
                    btn3[1].setBackgroundResource(R.drawable.right);
                    break;
                }
                case "C":
                {
                    btn3[2].setBackgroundResource(R.drawable.right);
                    break;
                }
                case "D":
                {
                    btn2[3].setBackgroundResource(R.drawable.right);
                    break;
                }
            }
        }
    }
    //luu diem len server............................................................................
    public void saveScore(String i)
    {
        sessionmanager session;
        session = new sessionmanager(getApplicationContext());
        String id = session.getInuser();
        try{
            compositeDisposable.add(API.saveScore(id,"Listening Conversation ",i)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {

                        }
                    }));
        }
        catch (Exception e)
        {

        }
    }
    @Override
    protected void onPause() {
        mediaPlayer.release();
        playaudio("http://23.101.29.94:3000/cau1");
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

                listening_conversation.super.onBackPressed();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //super.onBackPressed();
    }
}
