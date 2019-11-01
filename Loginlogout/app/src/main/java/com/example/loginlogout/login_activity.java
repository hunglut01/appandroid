package com.example.loginlogout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.loginlogout.retrofit.NODEjs;
import com.example.loginlogout.retrofit.retrofitclient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static android.net.NetworkInfo.State.CONNECTED;

public class login_activity extends AppCompatActivity {

    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    EditText edt_username,edt_password, edt_verifypass;
    Button btn_login,btn_reg,btn_newuser,btn_to_login;
    TextView title;
    sessionmanager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        //init API..................................................................................
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);
        //mapping...................................................................................
        session = new sessionmanager(getApplicationContext());
        btn_login = findViewById(R.id.btnlogin);
        btn_newuser = findViewById(R.id.btn_addnewuser);
        btn_reg = findViewById(R.id.btnreg);
        btn_to_login = findViewById(R.id.btn_back_to_login);
        edt_username = findViewById(R.id.input_username);
        edt_password = findViewById(R.id.input_password);
        edt_verifypass = findViewById(R.id.verify_pass);
        title = findViewById(R.id.singin_title);
        //check internet connection................................................................
        if(!checknetwork())
        {
            showdialog("Không có kết nối mạng!!!");
        }
        //event click login........................................................................
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_user(edt_username.getText().toString(),edt_password.getText().toString());
            }
        });
        //event click back to login................................................................
        btn_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
                title.setText(getResources().getString(R.string.sign_in));
                btn_login.setVisibility(View.VISIBLE);
                btn_newuser.setVisibility(View.VISIBLE);
                edt_verifypass.setVisibility(View.GONE);
                btn_reg.setVisibility(View.GONE);
                btn_to_login.setVisibility(View.GONE);
            }
        });
        //event click register......................................................................
        btn_newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
                title.setText(getResources().getString(R.string.sign_up));
                btn_login.setVisibility(View.GONE);
                btn_newuser.setVisibility(View.GONE);
                edt_verifypass.setVisibility(View.VISIBLE);
                btn_reg.setVisibility(View.VISIBLE);
                btn_to_login.setVisibility(View.VISIBLE);
                btn_reg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(edt_password.getText().toString().equals(edt_verifypass.getText().toString()))
                        {
                            registerUser(edt_username.getText().toString(),edt_password.getText().toString());
                        }
                        else
                        {
                            showdialog("Mật khẩu xác nhận sai!!! Vui lòng nhập lại!!");
                        }

                    }
                });

            }
        });
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
    //dang nhap.....................................................................................
    public void login_user(final String user, String pass)
    {
        compositeDisposable.add(API.loginUser(user,pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(s.contains("id"))
                        {
                            Toast.makeText(login_activity.this,"Login success",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(login_activity.this,Main_menu.class);
                            edt_password.setText("");
                            session.createLoginSession(user);
                            //intent.putExtra("name",edt_username.getText().toString());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else
                        {
                            showdialog(s);
                        }
                    }
                }));
    }
    //dang ki.......................................................................................
    public void registerUser(final String user, final String pass)
    {
        compositeDisposable.add(API.registerUser(user,pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        showdialog(s);
                    }
                }));
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
    public boolean checknetwork()
    {
        ConnectivityManager check = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] info = check.getAllNetworkInfo();
        for (int i = 0; i<info.length; i++){
            if (info[i].getState() == CONNECTED){
                return true;
            }
        }
        return false;
    }
    public void clear()
    {
        edt_password.setText("");
        edt_username.setText("");
        edt_verifypass.setText("");
    }
}
