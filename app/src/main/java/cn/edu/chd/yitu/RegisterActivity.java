package cn.edu.chd.yitu;


import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 注册页面
 */
public class RegisterActivity extends Activity {
    private EditText userName;
    private EditText passWord;
    private Button commit;
    private String name;// 用户名
    private String pwd;// 密码
    private ImageButton fanhuiButton;

    /**
     * url地址(根据你服务器的地址进行设置,主要是设置http://or9574ay.xicp.net:8888这里,or9574ay.xicp.
     * net对应的是你本机的ip地址,比如192.168.1.1,8888是端口号,tomcat默认是8080)
     */
    private String url = "http://nwafu.me/RegisterAndLogin/RegisterServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        userName = (EditText) findViewById(R.id.userName);
        passWord = (EditText) findViewById(R.id.passWord);
        commit = (Button) findViewById(R.id.reg_button);

        commit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userName.getText().toString();
                pwd = passWord.getText().toString();
                if (name == null) {
                    Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (pwd == null) {
                    Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    uploadUserData(url, name, pwd);
                }

            }
        });

        fanhuiButton= (ImageButton) findViewById(R.id.fanhui_button);
        fanhuiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                finish();
            }
        });
    }

    /**
     * 上传用户资料(用户名和密码)
     */
    private void uploadUserData(String url, String name, String pwd) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("username", name);
        params.addQueryStringParameter("password", pwd);
        httpUtils.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {
            /**
             * 上传失败
             */
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                System.out.println(arg1);
                Toast.makeText(getApplicationContext(), arg1, Toast.LENGTH_SHORT).show();
            }

            /**
             * 上传成功
             */
            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Toast.makeText(getApplicationContext(), arg0.result, Toast.LENGTH_SHORT).show();


            }
        });
    }

}
