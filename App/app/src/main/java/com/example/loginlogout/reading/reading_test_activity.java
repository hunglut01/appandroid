package com.example.loginlogout.reading;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginlogout.R;
import com.example.loginlogout.model.readingdata;
import com.example.loginlogout.retrofit.NODEjs;
import com.example.loginlogout.retrofit.retrofitclient;
import com.example.loginlogout.sessionmanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class reading_test_activity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<readingdata> listquestionreading1 = new ArrayList<>();
    private ActionBar actionBar;
    private TextView in_ques,time,suggest;
    private Button start_test, submit, next, back,number,unfocus;
    private int i = 0, temp = 0;
    private int socaudung,id;
    private String data,socauhoi;
    private CountDownTimer countDownTimer;
    private long timeleft = 1200000;
    private Button[] btn = new Button[4];
    private int[] btn_id = {R.id.a_1,R.id.b_1,R.id.c_1,R.id.d_1};
    private TextView a1,b1,c1,d1;
    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_reading);
        id = getIntent().getExtras().getInt("id");
        data = reading_activity.arr.get(id).getName();
        socauhoi = reading_activity.arr.get(id).getNumber();
        //mapping.................................................................................
        in_ques = findViewById(R.id.txt_question);
        for(int i = 0; i < btn.length ; i++)
        {
            btn[i] = findViewById(btn_id[i]);
            btn[i] = findViewById(btn_id[i]);
            btn[i] = findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
            btn[i].setOnClickListener(this);
            btn[i].setOnClickListener(this);
        }
        a1 = findViewById(R.id.a);
        b1 = findViewById(R.id.b);
        c1 = findViewById(R.id.c);
        d1 = findViewById(R.id.d);
        suggest = findViewById(R.id.txt_suggest);
        suggest.setVisibility(View.GONE);
        next = findViewById(R.id.btn_next);
        back = findViewById(R.id.btn_back);
        number = findViewById(R.id.txt_numques);
        submit = findViewById(R.id.btn_submit);
        next.setOnClickListener(this);
        back.setOnClickListener(this);
        back.setEnabled(false);
        submit.setOnClickListener(this);
        //init retrofit API........................................................................
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);
        //luu data nhap tu API.....................................................................
        insertlist();
        number.setText("1/"+socauhoi);


    }
    //get all question va hien cau hoi mac dinh....................................................
    public void insertlist()
    {
        try
        {
            compositeDisposable.add(API.getallreading(data)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            ArrayList<readingdata> arr = initlistquestion(s);
                            listquestionreading1.addAll(arr);
                            in_ques.setText(listquestionreading1.get(0).getQuestion());
                            a1.setText(listquestionreading1.get(0).getAnwa());
                            b1.setText(listquestionreading1.get(0).getAnwb());
                            c1.setText(listquestionreading1.get(0).getAnwc());
                            d1.setText(listquestionreading1.get(0).getAnwd());
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
        a1.setText(listquestionreading1.get(a).getAnwa());
        b1.setText(listquestionreading1.get(a).getAnwb());
        c1.setText(listquestionreading1.get(a).getAnwc());
        d1.setText(listquestionreading1.get(a).getAnwd());
        suggest.setText(listquestionreading1.get(a).getFix());
    }
    //su kien chon cau hoi.........................................................................
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_back:
            {
                clearFocus();
                i = i - 1;
                number.setText(i+1+"/" +socauhoi);
                if(i==0)
                {
                    checkbutton(next,back);
                }
                else {
                    next.setEnabled(true);
                }
                getquestion(i);
                loadAns(i);
                if(temp==1)
                {
                    review(i);
                }

                break;
            }
            case R.id.btn_next:
            {
                clearFocus();
                i = i + 1;
                number.setText(i+1+"/"+socauhoi);
                if(i==Integer.parseInt(socauhoi)-1)
                {
                    checkbutton(back,next);
                }
                else{
                    back.setEnabled(true);
                }
                getquestion(i);
                if(temp==1)
                {
                    review(i);
                }

                loadAns(i);
                break;
            }
            case R.id.btn_submit:
            {
                temp = 1;
                if(submit.getText().toString() == getResources().getString(R.string.submit))
                {
                    checktest();
                    submit.setText(getResources().getString(R.string.exit));
                }
                else
                {
                    onBackPressed();
                }



                break;
            }
            case R.id.a_1:
            {
                saveAns(1,i);
                setFocus(unfocus,btn[0]);
                break;
            }
            case R.id.b_1:
            {
                saveAns(2,i);
                setFocus(unfocus,btn[1]);
                break;
            }
            case R.id.c_1:
            {
                saveAns(3,i);
                setFocus(unfocus,btn[2]);
                break;
            }
            case R.id.d_1:
            {
                saveAns(4,i);
                setFocus(unfocus,btn[3]);
                break;
            }
        }
    }
    public void checkbutton(Button btn_unfocus, Button btn_focus)//button next, back
    {
        btn_unfocus.setEnabled(true);
        btn_focus.setEnabled(false);
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
    public void saveAns(int i, int i2)
    {
        switch (i) {
            case 1:
            {
                listquestionreading1.get(i2).setUser_anw(1);
                break;
            }
            case 2:
            {
                listquestionreading1.get(i2).setUser_anw(2);
                break;
            }
            case 3:
            {
                listquestionreading1.get(i2).setUser_anw(3);
                break;
            }
            case 4:
            {
                listquestionreading1.get(i2).setUser_anw(4);
                break;
            }
        }
    }
    public void loadAns(int i)
    {
        switch (listquestionreading1.get(i).getUser_anw())
        {
            case 1:
            {
                setFocus(unfocus,btn[0]);
                break;
            }
            case 2:
            {
                setFocus(unfocus,btn[1]);
                break;
            }
            case 3:
            {
                setFocus(unfocus,btn[2]);
                break;
            }
            case 4:
            {
                setFocus(unfocus,btn[3]);
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
    //kiem tra dap an..............................................................................
    public void checktest()
    {



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
        for(int i = 0; i < btn.length ; i++)
        {
            btn[i] = findViewById(btn_id[i]);
            //btn[i].setEnabled(false);
        }
        if(btn[1].isEnabled())
        {
            saveScore(Integer.toString(socaudung));
            for(int i = 0; i < btn.length ; i++)
            {
                btn[i].setEnabled(false);
            }
        }
        else
        {

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kết quả!!!");
        builder.setMessage("Số câu đúng: "+socaudung);
        builder.setPositiveButton("Xem gợi ý đáp án!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int ia) {
                suggest.setVisibility(View.VISIBLE);

                review(i);
                suggest.setText(listquestionreading1.get(i).getFix());
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
    //cham dap an...................................................................................
    public void review(int i)
    {
        if(listquestionreading1.get(i).getAnwser() != listquestionreading1.get(i).getUser_anw())
        {
            switch (listquestionreading1.get(i).getAnwser())
            {
                case 1:
                {
                    btn[0].setBackgroundResource(R.drawable.right);
                    break;
                }
                case 2:
                {
                    btn[1].setBackgroundResource(R.drawable.right);
                    break;
                }
                case 3:
                {
                    btn[2].setBackgroundResource(R.drawable.right);
                    break;
                }
                case 4:
                {
                    btn[3].setBackgroundResource(R.drawable.right);
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
            compositeDisposable.add(API.saveScore(id,"Reading "+data,i)
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
                //saveScore(Integer.toString(socaudung));
                reading_test_activity.super.onBackPressed();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //super.onBackPressed();
    }
    //timer........................................................................................
    public void starttimer(int i)
    {
        if(i == 1){
            countDownTimer.cancel();
        }
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
