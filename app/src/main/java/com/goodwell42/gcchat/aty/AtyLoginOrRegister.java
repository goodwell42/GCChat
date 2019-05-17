package com.goodwell42.gcchat.aty;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.goodwell42.gcchat.R;
import com.goodwell42.gcchat.util.DatebaseHelper;

import java.util.prefs.PreferenceChangeEvent;
import java.util.regex.Pattern;

/**
 * 广告页后跳转，用于登陆注册活动
 */
public class AtyLoginOrRegister extends AppCompatActivity implements View.OnClickListener {

    // 用于储存记住的密码
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // 数据库名称
    private static final String DATABASE_NAME="GCChat.db";

    // 数据库版本号
    private static final int DATABASE_VERSION=1;
    // 表名
    private static final String TABLE_NAME="username";
    private DatebaseHelper databaseHelper;
    private SQLiteDatabase db;

    // 记住密码勾选项
    private CheckBox rememberPass;

    private TabHost tabHost;

    // 登录按键
    private Button btnLogin;
    private EditText etLoginUsername;
    private EditText etLoginPassword;

    // 注册按键
    private Button btnRegister;
    private EditText etRegisterUsername;
    private EditText etRegisterPassword;
    private EditText etInsurePassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_login_or_register);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        initViews();
    }

    /**
     * 初始化页面
     */
    private void initViews() {
        tabHost = (TabHost) findViewById(R.id.tabHost);

        btnLogin = (Button) findViewById(R.id.btn_login);
        etLoginUsername = (EditText) findViewById(R.id.et_login_username);
        etLoginPassword = (EditText) findViewById(R.id.et_login_password);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            // 将账号和密码都设置到文本框里
            String account = pref.getString("et_login_username", "");
            String password = pref.getString("et_login_password", "");
            etLoginUsername.setText(account);
            etLoginPassword.setText(password);
            rememberPass.setChecked(true);
        }

        btnRegister = (Button) findViewById(R.id.btn_register);
        etRegisterUsername = (EditText) findViewById(R.id.et_register_username);
        etRegisterPassword = (EditText) findViewById(R.id.et_register_password);
        etInsurePassword = (EditText) findViewById(R.id.et_insure_password);

        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("Login").setIndicator("Login").setContent(R.id.layout_login));
        tabHost.addTab(tabHost.newTabSpec("Register").setIndicator("Register").setContent(R.id.layout_register));

        for (int i = 0; i < 2; i++) {
            TextView tv = ((TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title));
            tv.setAllCaps(false);
            tv.setTextSize(16);
        }

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                String account = etLoginUsername.getText().toString();
                String password = etLoginPassword.getText().toString();
                // 测试：如果账号是 goodwell 密码是 12103 ,就认为登陆成功
                //if (account.equals("goodwell") && password.equals("12103")) {
                if ((!account.equals("")) && (!password.equals(""))) {
                    isUserinfo(account,password);
                } else {
                    Toast.makeText(this, "account or password is empty",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btn_register: {
                String account = etRegisterUsername.getText().toString();
                String password = etRegisterPassword.getText().toString();
                String insurePassword = etInsurePassword.getText().toString();
                gcRegister(account, password, insurePassword);
                break;
            }
            default:
                break;
        }
    }


    /**
     * 用户账号密码检测，连接SQLite
     * @param name
     * @param pass
     * @return
     */
    public Boolean isUserinfo(String name,String pass) {
        String nameString = name;
        String passString = pass;
        databaseHelper=new DatebaseHelper(AtyLoginOrRegister.this,DATABASE_NAME,null,DATABASE_VERSION);
        db =  databaseHelper.getReadableDatabase();
        try {
            if (CheckNameExist(nameString)) {    // 检查账号是否存在
                Cursor cursor=db.query(TABLE_NAME, new String[]{"name","password"},"name=?",
                        new String[]{nameString},null,null,"password");
                while(cursor.moveToNext()) {    // 检查该账号的密码是否正确
                    String password=cursor.getString(cursor.getColumnIndex("password"));

                    if(passString.equals(password)) {
                        editor = pref.edit();
                        // 检查复选框是否被选中
                        if (rememberPass.isChecked()) {
                            editor.putBoolean("remember_password", true);
                            editor.putString("et_login_username", name);
                            editor.putString("et_login_password", pass);
                        } else {
                            editor.clear();
                        }
                        editor.apply();

                        // 登录成功弹窗
                        new AlertDialog.Builder(AtyLoginOrRegister.this).setTitle("Halo " + name)
                                .setMessage("login successful").setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                AtyMain.actionStart(AtyLoginOrRegister.this, "");
                                //Intent intent = new Intent(this, AtyMain.class);
                                //startActivity(intent);
                                finish();
                            }
                        }).show();
                        break;
                    } else {
                        Toast.makeText(this, "The password is incorrect",Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            } else {
                Toast.makeText(this, "The account does not exist, please register or re-enter",Toast.LENGTH_LONG).show();
            }
        }catch(SQLiteException e){
            CreatTable();
        }
        return false;
    }

    /**
     * 用于用户注册，传入SQLite
     * @param account
     * @param password
     * @param insurePassword
     */
    public void gcRegister(String account, String password, String insurePassword) {
        // 判断格式：a~z、A~Z、0~9
        String pattern = "^[0-9a-zA-Z]+$";
        // 对账号合法性进行判断
        if (CheckNameExist(account)) {
            Toast.makeText(this, "account has been registered.",
                    Toast.LENGTH_SHORT).show();
        } else if (account.equals("")) {
            Toast.makeText(this, "account cannot be empty.",
                    Toast.LENGTH_SHORT).show();
        } else if (!Pattern.matches(pattern, account)) {
            Toast.makeText(this, "accounts are limited to numbers and letters.",
                    Toast.LENGTH_SHORT).show();
        } else if (!password.equals(insurePassword)) {// 对密码进行二次确认
            Toast.makeText(this, "insure passwords are different.",
                    Toast.LENGTH_SHORT).show();
        } else {
            databaseHelper=new DatebaseHelper(AtyLoginOrRegister.this,DATABASE_NAME,null,DATABASE_VERSION);
            db =  databaseHelper.getReadableDatabase();
            db.execSQL("insert into username (name,password) values(?,?)",new String[]{account,password});

            Toast.makeText(AtyLoginOrRegister.this, "registration success！", Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(this, AtyMain.class);
            //startActivity(intent);
            AtyMain.actionStart(AtyLoginOrRegister.this, account);
            finish();
        }
    }

    /**
     * 用于检测用户名是否存在
     * @param value
     * @return
     */
    public boolean CheckNameExist(String value){
        databaseHelper=new DatebaseHelper(AtyLoginOrRegister.this,DATABASE_NAME,null,DATABASE_VERSION);
        db =  databaseHelper.getWritableDatabase();
        String Query = "Select * from username where name =?";
        Cursor cursor = db.rawQuery(Query,new String[] { value });
        if (cursor.getCount()>0){
            cursor.close();
            return  true;
        }
        cursor.close();
        return false;
    }

    private void CreatTable() {
        // TODO Auto-generated method stub
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + " (name varchar(30) primary key,password varchar(30));";
        try{
            db.execSQL(sql);
        }catch(SQLException ex){}
    }

}
