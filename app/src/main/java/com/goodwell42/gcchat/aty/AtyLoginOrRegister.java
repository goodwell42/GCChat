package com.goodwell42.gcchat.aty;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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

import java.util.prefs.PreferenceChangeEvent;
import java.util.regex.Pattern;

public class AtyLoginOrRegister extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private CheckBox rememberPass;

    private TabHost tabHost;

    private Button btnLogin;
    private EditText etLoginUsername;
    private EditText etLoginPassword;

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
                // 如果账号是 goodwell 密码是 12103 ,就认为登陆成功
                if (account.equals("goodwell") && password.equals("12103")) {
                    editor = pref.edit();
                    // 检查复选框是否被选中
                    if (rememberPass.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("et_login_username", account);
                        editor.putString("et_login_password", password);
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    //Intent intent = new Intent(this, AtyMain.class);
                    //startActivity(intent);
                    AtyMain.actionStart(AtyLoginOrRegister.this, account);
                    finish();
                } else {
                    Toast.makeText(this, "account or password is invalid",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btn_register: {
                String account = etRegisterUsername.getText().toString();
                String password = etRegisterPassword.getText().toString();
                String insurePassword = etInsurePassword.getText().toString();
                // 判断格式：a~z、A~Z、0~9
                String pattern = "^[0-9a-zA-Z]+$";
                // 对账号合法性进行判断
                if (account.equals("goodwell")) {
                    Toast.makeText(this, "account has been registered.",
                            Toast.LENGTH_SHORT).show();
                } else if (account.equals("")) {
                    Toast.makeText(this, "account cannot be empty.",
                            Toast.LENGTH_SHORT).show();
                } else if (!Pattern.matches(pattern, account)) {
                    Toast.makeText(this, "accounts are limited to numbers and letters.",
                            Toast.LENGTH_SHORT).show();
                } else if (!password.equals(insurePassword)) {
                    Toast.makeText(this, "insure passwords are different.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, AtyMain.class);
                    startActivity(intent);
                    finish();
                }
                // 对密码进行二次确认
                break;
            }
            default:
                break;
        }
    }
}
