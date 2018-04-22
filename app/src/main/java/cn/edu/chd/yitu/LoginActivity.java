package cn.edu.chd.yitu;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText userName;
    private EditText passWord;
    private Button loginButton;
    private Button registerButton;
    private String name;// 用户名
    private String pwd;// 密码

    private String url = "http://nwafu.me/RegisterAndLogin/LoginServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        userName = (EditText) findViewById(R.id.userName_loginActivity);
        passWord = (EditText) findViewById(R.id.passWord_loginActivity_et);
        loginButton = (Button) findViewById(R.id.login_button);
        registerButton = (Button) findViewById(R.id.reg_button_login);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:// 点击登录按钮
                name = userName.getText().toString();
                pwd = passWord.getText().toString();
                uploadUserData(url, name, pwd);

                break;
            case R.id.reg_button_login:// 点击注册按钮
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                //finish();
                break;

            default:
                break;
        }
    }

    /**
     * 上传用户资料(用户名和密码)
     */
    private void uploadUserData(String url, String name, String pwd) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("username", name);
        params.addQueryStringParameter("password", pwd);
        httpUtils.send(HttpRequest.HttpMethod.GET, url, params, new RequestCallBack<String>() {
            /**
             * 上传失败
             */
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(getApplicationContext(), arg1, Toast.LENGTH_SHORT).show();
            }

            /**
             * 上传成功
             */
            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Toast.makeText(getApplicationContext(), arg0.result, Toast.LENGTH_SHORT).show();
                if("登录成功".equals(arg0.result)){
                    Intent intent = new Intent(LoginActivity.this, UserGuide.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}