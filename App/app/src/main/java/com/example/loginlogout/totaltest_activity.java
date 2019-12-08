package com.example.loginlogout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginlogout.listening.listening_photo;
import com.example.loginlogout.model.listeningdata;
import com.example.loginlogout.model.readingdata;
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

public class totaltest_activity extends AppCompatActivity implements View.OnClickListener {
    private int i = 0 , socaudung = 0; //stt cau hoi
    int temp = 0;
    Button back, next, stt,stop, submit, unfocus , unfocus1, unfocus2, unfocus3, unfocus4 ;
    private CardView suggest;
    private LinearLayout layout_image,layout_seekbar,layout_reading,totallisten;
    private TableLayout layout_anslisten_image, layout_anslisten, layout_anslisten1, layout_anslisten2;
    private Button[] btn_photo = new Button[4];
    private int[] btn_photo_id = {R.id.a,R.id.b,R.id.c,R.id.d};
    private Button[] btn_listen1 = new Button[3]; // 3 button dap an phan nghe sheet 1
    private int[] btn_listen1_id = {R.id.a1,R.id.b1,R.id.c1};
    private Button[] btn_listen2 = new Button[3]; // 3 button dap an phan nghe sheet 1
    private int[] btn_listen2_id = {R.id.a2,R.id.b2,R.id.c2};
    private Button[] btn_listen3 = new Button[3]; // 3 button dap an phan nghe sheet 1
    private int[] btn_listen3_id = {R.id.a3,R.id.b3,R.id.c3};
    private Button[] btn_reading = new Button[4];
    private int[] btn_reading_id = {R.id.caua,R.id.caub,R.id.cauc,R.id.caud};
    private SeekBar seekBar;
    private Runnable runnable;
    private Handler handler;
    private MediaPlayer mediaPlayer;
    private ArrayList<listeningdata> listeningdata = new ArrayList<>();
    private ArrayList<readingdata> readingdata = new ArrayList<>();
    private TextView stt1, stt2, stt3,stt4,txt_question,texta,textb,textc,textd, txt_suggest;
    private ImageView img;
    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totalytest);
        //init API
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);
        //mapping..................................................................................
        for(int i = 0 ; i < 4 ; i ++)
        {
            btn_photo[i] = findViewById(btn_photo_id[i]);
            btn_reading[i] = findViewById(btn_reading_id[i]);
            btn_photo[i].setOnClickListener(this);
            btn_reading[i].setOnClickListener(this);

        }
        for(int i = 0 ; i < btn_listen1.length ; i ++)
        {
            btn_listen1[i] = findViewById(btn_listen1_id[i]);
            btn_listen2[i] = findViewById(btn_listen2_id[i]);
            btn_listen3[i] = findViewById(btn_listen3_id[i]);

            btn_listen1[i].setOnClickListener(this);
            btn_listen2[i].setOnClickListener(this);
            btn_listen3[i].setOnClickListener(this);

        }
        txt_suggest = findViewById(R.id.txt_suggest);
        suggest = findViewById(R.id.suggest);
        back = findViewById(R.id.btn_back);
        next = findViewById(R.id.btn_next);
        stt = findViewById(R.id.txt_numques);
        stt1 = findViewById(R.id.stt1);
        stt2 = findViewById(R.id.stt2);
        stt3 = findViewById(R.id.stt3);
        stt4 = findViewById(R.id.stt4);
        layout_reading = findViewById(R.id.layout_reading);
        layout_image = findViewById(R.id.layout_image); // layout anh
        layout_anslisten_image = findViewById(R.id.layout_anslisten_image);//layout ans sheet anh
        layout_seekbar = findViewById(R.id.layout_seekbar);//seakbar
        layout_anslisten = findViewById(R.id.layout_anslisten);
        layout_anslisten1 = findViewById(R.id.layout_anslisten1);
        layout_anslisten2 = findViewById(R.id.layout_anslisten2);
        totallisten = findViewById(R.id.totallisten);
        stop = findViewById(R.id.btn_stop);
        seekBar = findViewById(R.id.audio_status);
        submit = findViewById(R.id.btn_submit);
        txt_question = findViewById(R.id.txt_question);
        texta = findViewById(R.id.texta);
        textb = findViewById(R.id.textb);
        textc = findViewById(R.id.textc);
        textd = findViewById(R.id.textd);
        img = findViewById(R.id.img);
        playaudio("http://23.101.29.94:3000/tonghop1");
        //
        submit.setOnClickListener(this);
        handler = new Handler();
        back.setOnClickListener(this);
        back.setEnabled(false);
        next.setOnClickListener(this);
        stop.setOnClickListener(this);
        //
        unfocus = btn_photo[0];
        unfocus1 = btn_listen1[0];
        unfocus2 = btn_listen2[0];
        unfocus3 = btn_listen3[0];
        unfocus4 = btn_reading[0];
        getdata();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_submit:
            {
                temp = 1 ;
                if(submit.getText().toString() == getResources().getString(R.string.submit))
                {
                    submit.setText(getResources().getString(R.string.exit));

                    checktest();
                }
                else
                {
                    onBackPressed();
                }

                break;
            }
            case R.id.btn_back:
            {
                i = i - 1;

                if(i <= 7 || i >= 17)
                {
                    if(i == 7)
                    {
                        i = i -2;
                    }
                    if(i == 17)
                    {
                        totallisten.setVisibility(View.VISIBLE);
                        layout_reading.setVisibility(View.GONE);
                    }
                    if(i <= 6)
                    {
                        loadimage(i);

                        layout_image.setVisibility(View.VISIBLE);
                        layout_anslisten_image.setVisibility(View.VISIBLE);
                        layout_anslisten.setVisibility(View.GONE);
                        layout_anslisten1.setVisibility(View.GONE);
                        layout_anslisten2.setVisibility(View.GONE);
                    }
                    if(i > 17)
                    {
                        loadquestion(i);
                    }


                }
                if(i < 17 && i > 7 )
                {
                    i = i - 2 ;
                }
                stt.setText(i+1+"/38");
                stt1.setText(i-1+".");
                stt2.setText(i+".");
                stt3.setText(i+1+".");
                stt4.setText(i+1+".");

                clearFocus();
                clearFocus1();
                clearFocus2();
                loadAnsPhoto(i);
                loadAnsListening(i);
                loadAnsReading(i);
                if(temp == 1)
                {
                    review(i);
                }
                if(i == 0)
                {
                    checkbutton(next,back);
                }
                else
                {
                    next.setEnabled(true);
                }
                break;
            }
            case R.id.btn_next:
            {
                i = i + 1;

                if(i < 6)
                {
                    loadimage(i);

                }
                if(i >= 6 && i <= 17)
                {
                    i = i + 2;

                    layout_anslisten.setVisibility(View.VISIBLE);
                    layout_anslisten1.setVisibility(View.VISIBLE);
                    layout_anslisten2.setVisibility(View.VISIBLE);
                    layout_image.setVisibility(View.GONE);
                    layout_anslisten_image.setVisibility(View.GONE);
                }
                if(i >= 18 || i < 6 )
                {

                    if(i >= 18)
                    {
                        mediaPlayer.release();
                        playaudio("http://23.101.29.94:3000/tonghop1");
                        layout_reading.setVisibility(View.VISIBLE);
                        totallisten.setVisibility(View.GONE);
                        loadquestion(i);
                    }

                }
                stt.setText(i+1+"/38");
                stt1.setText(i-1+".");
                stt2.setText(i+".");
                stt3.setText(i+1+".");
                stt4.setText(i+1+".");


                clearFocus();
                clearFocus1();
                clearFocus2();
                loadAnsPhoto(i);
                loadAnsListening(i);
                loadAnsReading(i);
                if(temp == 1)
                {
                    review(i);
                }
                if(i==37)
                {
                    checkbutton(back,next);
                }
                else
                {
                    back.setEnabled(true);
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
            case R.id.a:
            {
                setFocusPhoto(unfocus,btn_photo[0]);
                saveAnsPhoto(i,"A");
                break;
            }
            case R.id.b:
            {
                setFocusPhoto(unfocus,btn_photo[1]);
                saveAnsPhoto(i,"B");
                break;
            }
            case R.id.c:
            {
                setFocusPhoto(unfocus,btn_photo[2]);
                saveAnsPhoto(i,"C");
                break;
            }
            case R.id.d:
            {
                setFocusPhoto(unfocus,btn_photo[3]);
                saveAnsPhoto(i,"D");
                break;
            }
            case R.id.a1:
            {
                saveAnsListening(i-2,"A");
                setFocus1(unfocus1,btn_listen1[0]);
                break;
            }
            case R.id.b1:
            {
                saveAnsListening(i-2,"B");
                setFocus1(unfocus1,btn_listen1[1]);
                break;
            }
            case R.id.c1:
            {
                saveAnsListening(i-2,"C");
                setFocus1(unfocus1,btn_listen1[2]);
                break;
            }
            case R.id.a2:
            {
                saveAnsListening(i-1,"A");
                setFocus2(unfocus2,btn_listen2[0]);
                break;
            }
            case R.id.b2:
            {
                saveAnsListening(i-1,"B");
                setFocus2(unfocus2,btn_listen2[1]);
                break;
            }
            case R.id.c2:
            {
                saveAnsListening(i-1,"C");
                setFocus2(unfocus2,btn_listen2[2]);
                break;
            }
            case R.id.a3:
            {
                saveAnsListening(i,"A");
                setFocus3(unfocus3,btn_listen3[0]);
                break;
            }
            case R.id.b3:
            {
                saveAnsListening(i,"B");
                setFocus3(unfocus3,btn_listen3[1]);
                break;
            }
            case R.id.c3:
            {
                saveAnsListening(i,"C");
                setFocus3(unfocus3,btn_listen3[2]);
                break;
            }
            case R.id.caua:
            {
                saveAnsReading(i,1);
                setFocus4(unfocus4,btn_reading[0]);
                break;
            }
            case R.id.caub:
            {
                saveAnsReading(i,2);
                setFocus4(unfocus4,btn_reading[1]);
                break;
            }
            case R.id.cauc:
            {
                saveAnsReading(i,3);
                setFocus4(unfocus4,btn_reading[2]);
                break;
            }
            case R.id.caud:
            {
                saveAnsReading(i,4);
                setFocus4(unfocus4,btn_reading[3]);
                break;
            }
        }
    }
    // su kien tat mo nut back va next................................................................
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
    private void getdata()
    {
        try {
            compositeDisposable.add(API.getlisteningtotal()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            ArrayList<listeningdata> arr = xulyjsonlistening(s);
                            listeningdata.addAll(arr);
                            loadimage(i);

                        }
                    }));
            compositeDisposable.add(API.getreadingtotal()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            ArrayList<readingdata> arr = xulyjsonreading(s);
                            readingdata.addAll(arr);
                        }
                    }));
        }catch (Exception e)
        {

        }
    }
    //xu ly json reading
    public ArrayList<readingdata> xulyjsonreading(String s) throws JSONException {
        ArrayList<readingdata> arrtemp = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(s);
        for(int i = 0 ; i < jsonArray.length() ; i ++)
        {
            readingdata ques = new readingdata();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            ques.question = jsonObject.getString("question");
            ques.anwa = jsonObject.getString("anwa");
            ques.anwb = jsonObject.getString("anwb");
            ques.anwc = jsonObject.getString("anwc");
            ques.anwd = jsonObject.getString("anwd");
            ques.anwser = jsonObject.getInt("anwser");
            ques.fix = jsonObject.getString(("fix"));
            arrtemp.add(ques);
        }
        return arrtemp;
    }
    //xu ly json listening
    public ArrayList<listeningdata> xulyjsonlistening(String s) throws JSONException
    {
        ArrayList<listeningdata> arrtemp = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(s);
        for(int i = 0 ; i < jsonArray.length() ; i ++)
        {
            listeningdata ques = new listeningdata();
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            ques.anwser = jsonObject.getString("answer");
            ques.image = jsonObject.getString("image");
            ques.user_anw = "";
            arrtemp.add(ques);
        }
        return arrtemp;
    }
    //load cau hoi reading vao....................................................................
    public void loadquestion(int i)
    {
        int t = i -18;
        txt_question.setText(readingdata.get(t).getQuestion());
        texta.setText(readingdata.get(t).getAnwa());
        textb.setText(readingdata.get(t).getAnwb());
        textc.setText(readingdata.get(t).getAnwc());
        textd.setText(readingdata.get(t).getAnwd());
        txt_suggest.setText(readingdata.get(t).getFix());
    }
    public void loadimage(int i)
    {
        if(listeningdata.get(i).getImage() != "1")
        {
            Picasso.get().load(listeningdata.get(i).getImage()).fit().into(img);
        }

    }
    private void setFocusPhoto(Button btn_unfocus, Button btn_focus){
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


    }
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
    private void setFocus4(Button btn_unfocus, Button btn_focus){
        if(btn_unfocus == null)
        {
            btn_focus.setBackgroundResource(R.drawable.checkkkk);
        }
        else
        {
            btn_unfocus.setBackgroundResource(R.drawable.backgroud);
            btn_focus.setBackgroundResource(R.drawable.checkkkk);
        }
        this.unfocus4 = btn_focus;
    }
    public void clearFocus()
    {
        for(int i = 0; i < btn_photo.length ; i++)
        {
            btn_photo[i].setBackgroundResource(R.drawable.backgroud);
        }
        unfocus = null;
    }
    public void clearFocus1()
    {
        for(int i = 0; i < btn_listen1.length ; i++)
        {
            btn_listen1[i].setBackgroundResource(R.drawable.backgroud);
            btn_listen2[i].setBackgroundResource(R.drawable.backgroud);
            btn_listen3[i].setBackgroundResource(R.drawable.backgroud);
        }
        unfocus1 = null;
        unfocus2 = null;
        unfocus3 = null;
    }
    public void clearFocus2()
    {
        for(int i = 0; i < btn_reading.length ; i++)
        {
            btn_reading[i].setBackgroundResource(R.drawable.backgroud);
        }
        unfocus4 = null;
    }
    public void saveAnsPhoto(int i,String s)
    {
        listeningdata.get(i).setUser_anw(s);
    }
    public void loadAnsPhoto(int i)
    {
        if(i<17)
        {
            switch (listeningdata.get(i).getUser_anw())
            {
                case "A":
                {
                    setFocusPhoto(unfocus,btn_photo[0]);
                    break;
                }
                case "B":
                {
                    setFocusPhoto(unfocus,btn_photo[1]);
                    break;
                }
                case "C":
                {
                    setFocusPhoto(unfocus,btn_photo[2]);
                    break;
                }
                case "D":
                {
                    setFocusPhoto(unfocus,btn_photo[3]);
                    break;
                }
            }
        }

    }
    public void saveAnsListening(int i, String s)
    {
        listeningdata.get(i).setUser_anw(s);
    }
    public void loadAnsListening(int i)
    {
        if(i<18 && i>7)
        {
            switch (listeningdata.get(i-2).getUser_anw())
            {
                case "A":
                {
                    setFocus1(unfocus1,btn_listen1[0]);
                    break;
                }
                case "B":
                {
                    setFocus1(unfocus1,btn_listen1[1]);
                    break;
                }
                case "C":
                {
                    setFocus1(unfocus1,btn_listen1[2]);
                    break;
                }

            }
            switch (listeningdata.get(i-1).getUser_anw())
            {
                case "A":
                {
                    setFocus2(unfocus2,btn_listen2[0]);
                    break;
                }
                case "B":
                {
                    setFocus2(unfocus2,btn_listen2[1]);
                    break;
                }
                case "C":
                {
                    setFocus2(unfocus2,btn_listen2[2]);
                    break;
                }

            }
            switch (listeningdata.get(i).getUser_anw())
            {
                case "A":
                {
                    setFocus1(unfocus3,btn_listen3[0]);
                    break;
                }
                case "B":
                {
                    setFocus1(unfocus3,btn_listen3[1]);
                    break;
                }
                case "C":
                {
                    setFocus1(unfocus3,btn_listen3[2]);
                    break;
                }

            }
        }

    }
    public void saveAnsReading(int i, int s)
    {
        int t = i - 18;
        readingdata.get(t).setUser_anw(s);

    }
    public void loadAnsReading(int i)
    {
        if(i>17)
        {
            int t = i - 18;
            switch (readingdata.get(t).getUser_anw())
            {
                case 1:
                {
                    setFocus4(unfocus4,btn_reading[0]);
                    break;
                }
                case 2:
                {
                    setFocus4(unfocus4,btn_reading[1]);
                    break;
                }
                case 3:
                {
                    setFocus4(unfocus4,btn_reading[2]);
                    break;
                }
                case 4:
                {
                    setFocus4(unfocus4,btn_reading[3]);
                    break;
                }

            }
        }
    }
    public void checktest()
    {
        socaudung = 0;
        for(listeningdata a : listeningdata)
        {
            if(a.getUser_anw()!="")
            {
                if(a.getUser_anw().equals(a.getAnwser()))
                {
                    socaudung = socaudung + 1;
                }
            }
        }
        for(readingdata b : readingdata)
        {
            if(b.getUser_anw()!= 0)
            {
                if(b.getUser_anw() == b.getAnwser())
                {
                    socaudung = socaudung + 1;
                }
            }
        }
        for(int i = 0 ; i < 4 ; i ++)
        {
            btn_photo[i].setEnabled(false);
            btn_reading[i].setEnabled(false);
        }
        for(int i = 0 ; i < 3 ; i++)
        {
            btn_listen1[i].setEnabled(false);
            btn_listen2[i].setEnabled(false);
            btn_listen3[i].setEnabled(false);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kết quả!!!");
        builder.setMessage("Số câu đúng: "+ socaudung);
        builder.setPositiveButton("Xem lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int ia) {
                review(i);
                saveScore(Integer.toString(socaudung));
            }
        });
        builder.setNegativeButton("Thoát!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveScore(Integer.toString(socaudung));
                totaltest_activity.super.onBackPressed();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void review(int i)
    {
        suggest.setVisibility(View.VISIBLE);
        if(i<6)
        {
            if(!listeningdata.get(i).getUser_anw().equals(listeningdata.get(i).getAnwser()))
            {
                switch (listeningdata.get(i).getAnwser())
                {
                    case "A":
                    {
                        btn_photo[0].setBackgroundResource(R.drawable.right);
                        break;
                    }
                    case "B":
                    {
                        btn_photo[1].setBackgroundResource(R.drawable.right);
                        break;
                    }
                    case "C":
                    {
                        btn_photo[2].setBackgroundResource(R.drawable.right);
                        break;
                    }
                    case "D":
                    {
                        btn_photo[3].setBackgroundResource(R.drawable.right);
                        break;
                    }
                }
            }
        }
        if(i<18 && i>7)
        {
            if(!listeningdata.get(i-2).getAnwser().equals(listeningdata.get(i-2).getUser_anw()))
            {
                switch (listeningdata.get(i-2).getAnwser())
                {
                    case "A":
                    {
                        btn_listen1[0].setBackgroundResource(R.drawable.right);
                        break;
                    }
                    case "B":
                    {
                        btn_listen1[1].setBackgroundResource(R.drawable.right);
                        break;
                    }
                    case "C":
                    {
                        btn_listen1[2].setBackgroundResource(R.drawable.right);
                        break;
                    }
                }
            }
            if(!listeningdata.get(i-1).getAnwser().equals(listeningdata.get(i-1).getUser_anw()))
            {
                switch (listeningdata.get(i-1).getAnwser())
                {
                    case "A":
                    {
                        btn_listen2[0].setBackgroundResource(R.drawable.right);
                        break;
                    }
                    case "B":
                    {
                        btn_listen2[1].setBackgroundResource(R.drawable.right);
                        break;
                    }
                    case "C":
                    {
                        btn_listen2[2].setBackgroundResource(R.drawable.right);
                        break;
                    }
                }
            }
            if(!listeningdata.get(i).getAnwser().equals(listeningdata.get(i).getUser_anw()))
            {
                switch (listeningdata.get(i).getAnwser())
                {
                    case "A":
                    {
                        btn_listen3[0].setBackgroundResource(R.drawable.right);
                        break;
                    }
                    case "B":
                    {
                        btn_listen3[1].setBackgroundResource(R.drawable.right);
                        break;
                    }
                    case "C":
                    {
                        btn_listen3[2].setBackgroundResource(R.drawable.right);
                        break;
                    }
                }
            }
        }
        if(i>17)
        {
            int t = i - 18;
            if(readingdata.get(t).getUser_anw()!=readingdata.get(t).getAnwser())
            {
                switch (readingdata.get(t).getAnwser())
                {
                    case 1:
                    {
                        btn_reading[0].setBackgroundResource(R.drawable.right);
                        break;
                    }
                    case 2:
                    {
                        btn_reading[1].setBackgroundResource(R.drawable.right);
                        break;
                    }
                    case 3:
                    {
                        btn_reading[2].setBackgroundResource(R.drawable.right);
                        break;
                    }
                    case 4:
                    {
                        btn_reading[3].setBackgroundResource(R.drawable.right);
                        break;
                    }
                }
            }

        }
    }
    //luu diem len server............................................................................
    public void saveScore(String i) {
        sessionmanager session;
        session = new sessionmanager(getApplicationContext());
        String id = session.getInuser();
        try {
            compositeDisposable.add(API.saveScore(id, "Tổng hợp 1: ", i)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {

                        }
                    }));
        } catch (Exception e) {

        }
    }
    @Override
    protected void onPause() {
        mediaPlayer.release();
        playaudio("http://23.101.29.94:3000/tonghop1");
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


                totaltest_activity.super.onBackPressed();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //super.onBackPressed();
    }
}
