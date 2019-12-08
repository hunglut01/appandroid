package com.example.loginlogout.validation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginlogout.Main_menu;
import com.example.loginlogout.R;
import com.example.loginlogout.retrofit.NODEjs;
import com.example.loginlogout.retrofit.retrofitclient;
import com.example.loginlogout.sessionmanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static android.net.NetworkInfo.State.CONNECTED;

public class login_activity extends AppCompatActivity {

    NODEjs API;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    EditText edt_username,edt_password;
    TextView newaccount;
    Button btn_login,btn_forget;
    sessionmanager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //
        //init API..................................................................................
        Retrofit retrofit = retrofitclient.getInstance();
        API = retrofit.create(NODEjs.class);
        //mapping...................................................................................
        session = new sessionmanager(getApplicationContext());
        btn_login = findViewById(R.id.btnlogin);
        edt_username = findViewById(R.id.input_username);
        edt_password = findViewById(R.id.input_password);
        newaccount = findViewById(R.id.NewAcc);
        btn_forget = findViewById(R.id.forgetPass);

        //check internet connection................................................................
        if(!checknetwork())
        {
            showdialog("Không có kết nối mạng!!!");
        }
        else
        {

        }
        //event click login........................................................................
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_user(edt_username.getText().toString(),edt_password.getText().toString());
            }
        });
        //event create new account
        newaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_activity.this,register_activity.class);
                startActivity(intent);
            }
        });
        //event reset pass
        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_activity.this,resetpassword_activity.class);
                startActivity(intent);
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
                            Intent intent = new Intent(login_activity.this, Main_menu.class);
                            edt_password.setText("");
                            session.createLoginSession(getname(s),getid(s));
                            Toast.makeText(login_activity.this, ""+getid(s), Toast.LENGTH_SHORT).show();
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
    public String getname(String s) throws JSONException {
        JSONObject jsonObject = new JSONObject(s);
        String name = jsonObject.getString("username");
        String id = jsonObject.getString("_id");
        return name;
    }
    public String getid(String s) throws JSONException {
        JSONObject jsonObject = new JSONObject(s);
        String id = jsonObject.getString("_id");
        return id;
    }
}
