package com.example.loginlogout.reading;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginlogout.R;
import com.example.loginlogout.model.readingdata;
import com.example.loginlogout.retrofit.NODEjs;
import com.example.loginlogout.retrofit.retrofitclient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class reading_activity extends AppCompatActivity {
    ArrayList<readingdata> listquestionreading1 = new ArrayList<>();
    private ActionBar actionBar;
    private TextView in_ques,stt,time;
    private RadioButton ca,cb,cc,cd;
    private Button start_test, submit;
    private RelativeLayout start_layout, table_layout, question_layout;
    private RadioGroup groupanwser;
    private int i = 0;
    private int socaudung;
    private CountDownTimer countDownTimer;
    private long timeleft = 1200000;

    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_activity);
        //mapping..................................................................................
        start_layout = findViewById(R.id.start_layout);
        table_layout = findViewById(R.id.table_layout);
        question_layout = findViewById(R.id.question_layout);
        in_ques = findViewById(R.id.txt_question);
        ca = findViewById(R.id.a);
        cb = findViewById(R.id.b);
        cc = findViewById(R.id.c);
        cd = findViewById(R.id.d);
        stt = findViewById(R.id.stt);
        start_test = findViewById(R.id.start);
        groupanwser = findViewById(R.id.radioGroup);
        submit = findViewById(R.id.submit_test);
        time = findViewById(R.id.txt_timer);
        //init actionbar...........................................................................
        actionBar = getSupportActionBar();
        actionBar.setTitle("Reading multiple-choice");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        //init retrofit API........................................................................
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);
        //luu data nhap tu API.....................................................................
        insertlist();
        //su kien bat dau..........................................................................
        start_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table_layout.setVisibility(View.VISIBLE);
                question_layout.setVisibility(View.VISIBLE);
                start_layout.setVisibility(View.GONE);
                starttimer();
            }
        });
        //su kien chon dap an......................................................................
        groupanwser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.a:
                    {
                        listquestionreading1.get(i).setUser_anw(1);
                        break;
                    }
                    case R.id.b:
                    {
                        listquestionreading1.get(i).setUser_anw(2);
                        break;
                    }
                    case R.id.c:
                    {
                        listquestionreading1.get(i).setUser_anw(3);
                        break;
                    }
                    case R.id.d:
                    {
                        listquestionreading1.get(i).setUser_anw(4);
                        break;
                    }
                }
            }
        });
        //su kien nop bai..........................................................................
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checktest();

            }
        });
    }
    //get all question va hien cau hoi mac dinh....................................................
    public void insertlist()
    {
        try
        {
            compositeDisposable.add(API.getallreading("1")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            ArrayList<readingdata> arr = initlistquestion(s);
                            listquestionreading1.addAll(arr);
                            in_ques.setText(listquestionreading1.get(0).getQuestion());
                            ca.setText(listquestionreading1.get(0).getAnwa());
                            cb.setText(listquestionreading1.get(0).getAnwb());
                            cc.setText(listquestionreading1.get(0).getAnwc());
                            cd.setText(listquestionreading1.get(0).getAnwd());
                        }
                    }));
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
    }
    //click item toolbar...........................................................................
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
    //khoi tao toolbar.............................................................................
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //show cau hoi len.............................................................................
    public void getquestion(int a)
    {
        in_ques.setText(listquestionreading1.get(a).getQuestion());
        ca.setText(listquestionreading1.get(a).getAnwa());
        cb.setText(listquestionreading1.get(a).getAnwb());
        cc.setText(listquestionreading1.get(a).getAnwc());
        cd.setText(listquestionreading1.get(a).getAnwd());
    }
    //thay doi stt cau hoi.........................................................................
    public void change_number(String a)
    {
        stt.setText(a+". ");
    }
    //su kien chon cau hoi.........................................................................
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.c1: {
                i = 0;
                getquestion(i);
                change_number("1");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c2: {
                i=1;
                getquestion(i);
                change_number("2");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c3: {
                i=2;
                getquestion(i);
                change_number("3");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c4: {
                i=3;
                getquestion(i);
                change_number("4");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c5: {
                i=4;
                getquestion(i);
                change_number("5");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c6: {
                i=5;
                getquestion(i);
                change_number("6");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c7: {
                i=6;
                getquestion(i);
                change_number("7");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c8: {
                i=7;
                getquestion(i);
                change_number("8");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c9: {
                i=8;
                getquestion(i);
                change_number("9");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c10: {
                i=9;
                getquestion(i);
                change_number("10");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c11: {
                i=10;
                getquestion(i);
                change_number("11");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c12: {
                i=11;
                getquestion(i);
                change_number("12");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c13: {
                i=12;
                getquestion(i);
                change_number("13");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c14: {
                i=13;
                getquestion(i);
                change_number("14");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c15: {
                i=14;
                getquestion(i);
                change_number("15");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c16: {
                i=15;
                getquestion(i);
                change_number("16");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c17: {
                i=16;
                getquestion(i);
                change_number("17");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c18: {
                i=17;
                getquestion(i);
                change_number("18");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c19: {
                i=18;
                getquestion(i);
                change_number("19");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c20: {
                i=19;
                getquestion(i);
                change_number("20");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c21: {
                i=20;
                getquestion(i);
                change_number("21");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c22: {
                i=21;
                getquestion(i);
                change_number("22");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c23: {
                i=22;
                getquestion(i);
                change_number("23");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c24: {
                i=23;
                getquestion(i);
                change_number("24");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c25: {
                i=24;
                getquestion(i);
                change_number("25");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c26: {
                i=25;
                getquestion(i);
                change_number("26");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c27: {
                i=26;
                getquestion(i);
                change_number("27");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c28: {
                i=27;
                getquestion(i);
                change_number("28");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c29: {
                i=28;
                getquestion(i);
                change_number("29");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }
            case R.id.c30: {
                i=29;
                getquestion(i);
                change_number("30");
                loadcheckchecked(listquestionreading1.get(i).getUser_anw());
                break;
            }

        }
    }
    //xu ly json nhan tu API.......................................................................
    public ArrayList<readingdata> initlistquestion(String s) throws JSONException {
        ArrayList<readingdata> arr = new ArrayList<>();
        JSONArray array = new JSONArray(s);
        for (int i = 0; i < array.length(); i++) {
            readingdata temp = new readingdata();
            JSONObject jsonObject = array.getJSONObject(i);
            temp.question = jsonObject.getString("question");
            temp.anwa = jsonObject.getString("anwa");
            temp.anwb = jsonObject.getString("anwb");
            temp.anwc = jsonObject.getString("anwc");
            temp.anwd = jsonObject.getString("anwd");
            temp.anwser = jsonObject.getInt("anwser");
            temp.fix = jsonObject.getString("fix");
            arr.add(temp);
        }
        return arr;
    }
    //xu ly goi lai rafio button...................................................................
    public void loadcheckchecked(int a)
    {
        switch (a)
        {
            case 1:
            {
                //Toast.makeText(this, ""+a, Toast.LENGTH_SHORT).show();
                groupanwser.check(R.id.a);
                break;
            }
            case 2:
            {
                //Toast.makeText(this, ""+a, Toast.LENGTH_SHORT).show();
                groupanwser.check(R.id.b);
                break;
            }
            case 3:
            {
                //Toast.makeText(this, ""+a, Toast.LENGTH_SHORT).show();
                groupanwser.check(R.id.c);
                break;
            }
            case 4:
            {
                //Toast.makeText(this, "123"+a, Toast.LENGTH_SHORT).show();
                groupanwser.check(R.id.d);
                break;
            }
            default:
            {
                //Toast.makeText(this, ""+a, Toast.LENGTH_SHORT).show();
                groupanwser.clearCheck();
                break;
            }
        }
    }
    //kiem tra dap an..............................................................................
    public void checktest()
    {
        //khoa dap an
        for(int i = 0; i < groupanwser.getChildCount(); i++){
            (groupanwser.getChildAt(i)).setEnabled(false);
        }
        socaudung = 0;
        for(readingdata q : listquestionreading1)
        {
            try
            {
                if(q.getUser_anw()== q.getAnwser())
                {
                    socaudung = socaudung +1 ;
                }
                else
                {
                    //nothing
                }
            }
            catch (Exception e)
            {
                Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kết quả!!!");
        builder.setMessage("Số câu đúng: "+socaudung);
        builder.setPositiveButton("Xem gợi ý đáp án!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Thoát!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //timer........................................................................................
    public void starttimer()
    {
        countDownTimer = new CountDownTimer(timeleft,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleft = millisUntilFinished;
                updatetime();
            }

            @Override
            public void onFinish() {
                checktest();
            }
        }.start();
    }
    public void updatetime()
    {
        int minutes = (int) (timeleft / 60000);
        int seconds = (int) (timeleft % 60000 / 1000 );
        String timelefttext;
        timelefttext = "" + minutes;
        timelefttext += ":";
        if(seconds < 10 )
        {
            timelefttext += "0" + seconds;
        }
        else
        {
            timelefttext += seconds;
        }
        time.setText(timelefttext);
    }
}
