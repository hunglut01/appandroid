package com.example.loginlogout.validation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class register_activity extends AppCompatActivity implements View.OnClickListener {
    private TextView back;
    private Button btn_reg;
    private EditText username, email, password,verifypass;
    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //init API..................................................................................
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);
        //mapping..................................................................................
        back = findViewById(R.id.backtolog);
        btn_reg = findViewById(R.id.btnregister);
        username = findViewById(R.id.reg_username);
        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_password);
        verifypass = findViewById(R.id.verify_password);
        //init event
        back.setOnClickListener(this);
        btn_reg.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backtolog:
            {
                onBackPressed();
                break;
            }
            case R.id.btnregister:
            {
                if(username.getText().toString().length()==0||password.getText().toString().length()==0||email.getText().toString().length()==0||verifypass.getText().toString().length()==0)
                {
                    showdialog("Vui lòng điền đầy đủ các mục!");
                }
                else
                {
                    registerUser(username.getText().toString(),email.getText().toString(),password.getText().toString());
                }

            }
        }
    }
    //dang ki.......................................................................................
    public void registerUser(final String user, final String email, final String pass)
    {
        if(verifypass.getText().toString().equals(password.getText().toString()))
        {
            compositeDisposable.add(API.registerUser(user,email,pass)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {

                            if(s.contains("Đăng kí thành công!!!"))
                            {
                                onBackPressed();
                                Toast.makeText(register_activity.this, ""+s, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                showdialog(s);
                            }

                        }
                    }));
        }
        else
        {
            showdialog("Mật khẩu không khớp!");
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
