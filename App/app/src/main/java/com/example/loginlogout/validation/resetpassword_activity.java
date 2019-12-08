package com.example.loginlogout.validation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginlogout.R;
import com.example.loginlogout.retrofit.NODEjs;
import com.example.loginlogout.retrofit.retrofitclient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class resetpassword_activity extends AppCompatActivity implements View.OnClickListener {
    private EditText verifyEmail,code, pass, newpass;
    Button btn_next;
    private RelativeLayout layout1, layout2,layout3,layout4;
    private TextView title,back,textview;
    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        //init API..................................................................................
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);
        btn_next = findViewById(R.id.btnContinue);
        verifyEmail = findViewById(R.id.input_email);
        code = findViewById(R.id.verify_code);
        pass = findViewById(R.id.new_password);
        newpass = findViewById(R.id.verify_newpassword);
        layout1 = findViewById(R.id.relativeLayout1);
        layout2 = findViewById(R.id.relativeLayout2);
        layout3 = findViewById(R.id.relativeLayout3);
        layout4 = findViewById(R.id.relativeLayout4);
        title = findViewById(R.id.text1);
        back = findViewById(R.id.backtolog1);
        textview = findViewById(R.id.warning);
        back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnContinue:
            {
                if(title.getText()=="Thay đổi mật khẩu")
                {
                    resetPass(code.getText().toString(),pass.getText().toString(),newpass.getText().toString());
                }
                else
                {
                    if(verifyEmail.getText().toString().length() == 0)
                    {
                        textview.setVisibility(View.VISIBLE);
                        textview.setText("Bạn chưa nhập email");
                        //textview.setPaintFlags(textview.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                    }
                    else
                    {
                        requestChangepass(verifyEmail.getText().toString());
                    }


                }

                break;
            }
            case R.id.backtolog1:
            {
                if(title.getText()=="Thay đổi mật khẩu")
                {
                    title.setText("Vui lòng nhập email");
                    layout1.setVisibility(View.VISIBLE);
                    layout2.setVisibility(View.GONE);
                    layout3.setVisibility(View.GONE);
                    layout4.setVisibility(View.GONE);
                }
                else
                {
                    onBackPressed();
                }
                break;
            }
        }
    }
    public void requestChangepass(final String email)
    {
        try {
            compositeDisposable.add(API.reqresetpass(email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            if(s.contains("Gửi thành công!"))
                            {
                                title.setText("Thay đổi mật khẩu");
                                layout1.setVisibility(View.GONE);
                                layout2.setVisibility(View.VISIBLE);
                                layout3.setVisibility(View.VISIBLE);
                                layout4.setVisibility(View.VISIBLE);
                            }
                            else{
                                textview.setVisibility(View.VISIBLE);
                                textview.setText(s);
                            }
                        }
                    }));
        }
        catch (Exception e)
        {

        }

    }
    public void resetPass(final String code,final String pass,final String verify)
    {
        try {
            compositeDisposable.add(API.resetpass(code,pass,verify)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            Toast.makeText(resetpassword_activity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }));
        }
        catch (Exception e)
        {

        }
    }
    //dialog........................................................................................
    private void showdialog(String s)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage(s);
        builder.setCancelable(false);
        builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
