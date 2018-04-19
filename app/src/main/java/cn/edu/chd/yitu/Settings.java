package cn.edu.chd.yitu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.app.Activity;

import android.app.Dialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.mob.MobSDK;

import butterknife.BindView;
import butterknife.OnClick;
import cn.edu.chd.service.ShakeService;
import cn.edu.chd.values.ApplicationValues;
import cn.edu.chd.view.YiSettingButton;
import cn.edu.chd.view.YiSettingButton.OnCheckChangedListener;
import cn.edu.chd.view.YiTitleBar;
import cn.edu.chd.view.YiTitleBar.LeftButtonClickListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * @author ZhangQiong
 *         <p>
 *         设置界面
 */
public class Settings extends Activity implements OnCheckChangedListener, OnSeekBarChangeListener, OnClickListener, OnGestureListener, PlatformActionListener, Callback {
    /**
     * 标题栏
     */
    private YiTitleBar title_bar = null;
    /**
     * 画图时屏幕是否常亮的开关
     */
    private YiSettingButton ysb_screen_switch = null;
    /**
     * "摇一摇"启动应用的开关
     */
    private YiSettingButton ysb_shake_start = null;
    /**
     * 显示画布宽度的TextView
     */
    private TextView tv_canvas_width = null;
    /**
     * 显示画布高度的TextView
     */
    private TextView tv_canvas_height = null;
    /**
     * 设置画布高度的SeekBar
     */
    private SeekBar seekbar_canvas_size_height = null;
    /**
     * 设置画布宽度的SeekBar
     */
    private SeekBar seekbar_canvas_size_width = null;
    /**
     * 关于
     */
    private Button but_about = null;
    /**
     * 用户指引
     */
    private Button but_user_guide = null;
    /**
     * 分享
     */
//    private Button but_share = null;
    private Button but_share1 = null;
    /**
     * 检测用户手势
     */
    private GestureDetector mGestureDetector;

    private Button but_qrscan = null;

//    @BindView(R.id.but_qrscan)
//    Button qrscan;
    /**
     * 触发滑动的阀值
     */
    private static final int VALUE = 100;
    private static final String TAG = "Settings";

    private int defaultWidth;
    private int defaultHeight;

    private Dialog dialog;

//    private static final String SHARE_TEXT = "易涂是一款基于android的2d趣味涂鸦软件，支持各种图元创建、填色、变换、浏览等操作.";


    private View view;

    private TextView qita;

    private long timeclick=0;
    private static int clickCounts=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        defaultWidth = dm.widthPixels;
        defaultHeight = dm.heightPixels;

        mGestureDetector = new GestureDetector(this, this);//注册手势事件
        initTitleBar();//初始化标题栏
        initComponent();//初始化控件
        readConfiguration();//读取配置,必须在初始化控件后执行


        MobSDK.init(this);
        qita=findViewById(R.id.qita);
        qita.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStyle();
                System.out.println("11");
            }
        });

    }


    /**
     * 读取配置信息
     */
    public void readConfiguration() {
        Log.i(TAG, "WI:" + defaultWidth + ",HEI" + defaultHeight);

        SharedPreferences sp = this.getSharedPreferences(ApplicationValues.Settings.SETTING_PREF, MODE_PRIVATE);

        String canvas_width = sp.getString(ApplicationValues.Settings.CANVAS_WIDTH, defaultWidth + "");
        String canvas_height = sp.getString(ApplicationValues.Settings.CANVAS_HEIGHT, defaultHeight + "");
        boolean is_screen_on = sp.getBoolean(ApplicationValues.Settings.SCREEN_STATE, false);
        boolean is_shake_on = sp.getBoolean(ApplicationValues.Settings.SHAKE_MODEL, false);
        //将画布宽高显示到ui
        seekbar_canvas_size_width.setProgress(Integer.parseInt(canvas_width) - defaultWidth);
        seekbar_canvas_size_height.setProgress(Integer.parseInt(canvas_height) - defaultHeight);
        tv_canvas_width.setText(canvas_width);
        tv_canvas_height.setText(canvas_height);

        //将开关状态显示到UI
        ysb_screen_switch.setChecked(is_screen_on);
        ysb_shake_start.setChecked(is_shake_on);
    }

    /**
     * 在activity销毁之前，保存好配置
     */
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sp = this.getSharedPreferences(ApplicationValues.Settings.SETTING_PREF, MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(ApplicationValues.Settings.CANVAS_WIDTH, tv_canvas_width.getText().toString());
        editor.putString(ApplicationValues.Settings.CANVAS_HEIGHT, tv_canvas_height.getText().toString());
        editor.putBoolean(ApplicationValues.Settings.SCREEN_STATE, ysb_screen_switch.isChecked());
        editor.putBoolean(ApplicationValues.Settings.SHAKE_MODEL, ysb_shake_start.isChecked());
        editor.commit();
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        title_bar = (YiTitleBar) findViewById(R.id.ytb_settings);
        title_bar.setTitleName("设置");
        title_bar.setLeftButtonBGResource(R.drawable.setting_title_bar_selector);
        title_bar.setOnLeftButtonClickListener(new LeftButtonClickListener() {
            @Override
            public void leftButtonClick() {
                Settings.this.finish();
                overridePendingTransition(R.anim.slide_remain, R.anim.out_left);
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initComponent() {
        view = getLayoutInflater().inflate(R.layout.popupwindow_share, null);

        ysb_screen_switch = (YiSettingButton) findViewById(R.id.ysb_screen_always_on);
        ysb_shake_start = (YiSettingButton) findViewById(R.id.ysb_shake_start);

        tv_canvas_height = (TextView) findViewById(R.id.tv_canvas_size_height);
        tv_canvas_width = (TextView) findViewById(R.id.tv_canvas_size_width);

        seekbar_canvas_size_height = (SeekBar) findViewById(R.id.seekbar_canvas_size_height);
        seekbar_canvas_size_width = (SeekBar) findViewById(R.id.seekbar_canvas_size_width);

        ysb_screen_switch.setOnCheckChangedListener(this);
        ysb_shake_start.setOnCheckChangedListener(this);

        ysb_screen_switch.setContentTitle(R.string.str_screen_on);
        ysb_shake_start.setContentTitle(R.string.str_shake_start);

        seekbar_canvas_size_height.setOnSeekBarChangeListener(this);
        seekbar_canvas_size_width.setOnSeekBarChangeListener(this);

        //设置最大值
        seekbar_canvas_size_width.setMax(2048 - defaultWidth);
        seekbar_canvas_size_height.setMax(2048 - defaultHeight);

        but_about = (Button) findViewById(R.id.but_about);
        but_user_guide = (Button) findViewById(R.id.but_user_guide);

        but_share1 = (Button) findViewById(R.id.but_share1);

        but_qrscan = findViewById(R.id.but_qrscan);


        but_about.setOnClickListener(this);


        but_share1.setOnClickListener(this);

        but_user_guide.setOnClickListener(this);
        but_qrscan.setOnClickListener(this);

    }

    /*
     * 【重要】重新定义touch事件分发优先级
     * */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        this.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onCheckChanged(View view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.ysb_screen_always_on:
                //onstop方法中保存配置即可,此处不需要操作
                break;
            case R.id.ysb_shake_start:
                if (isChecked) {
                    Intent intent = new Intent(this, ShakeService.class);
                    this.startService(intent);
                } else {
                    Intent intent = new Intent(this, ShakeService.class);
                    this.stopService(intent);
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekbar_canvas_size_width:
                progress += defaultWidth;
                tv_canvas_width.setText(progress + "");
                break;
            case R.id.seekbar_canvas_size_height:
                progress += defaultHeight;
                tv_canvas_height.setText(progress + "");
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_about:
                Intent intent = new Intent(this, CopyRight.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_left, R.anim.slide_remain);
                break;
            case R.id.but_user_guide:
                Intent intent1 = new Intent(this, UserGuide.class);
                this.startActivity(intent1);
                overridePendingTransition(R.anim.in_left, R.anim.slide_remain);
                break;

            case R.id.but_share1: //推荐给朋友
                recommend();
                break;
            case R.id.but_qrscan:
                Intent qrscan_intent = new Intent(this, QRScaner.class);
                startActivity(qrscan_intent);
                overridePendingTransition(R.anim.in_left, R.anim.slide_remain);
                break;

        }
    }


    private void recommend() {

        /*Intent actionsend = new Intent(Intent.ACTION_SEND);
        actionsend.setType("image*//*");
        actionsend.putExtra(Intent.EXTRA_SUBJECT,"推荐到");
        actionsend.putExtra(Intent.EXTRA_TEXT,"震惊!ZQ涂鸦是一款基于android的2d趣味涂鸦软件，支持各种图元创建、填色、变换、浏览等操作.快来下载吧http://www.nwafu.me from ZQ涂鸦APP");
        //actionsend.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("/storage/emulated/0/yitu/20180321_091659.jpg")));
        actionsend.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(actionsend,getTitle()));*/
        InputStream input = getResources().openRawResource(R.raw.welcome);
        BufferedReader read = new BufferedReader(new InputStreamReader(input));
        String line = "";
        try {
            while ((line = read.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("ZQ小度涂鸦APP");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://www.nwafu.me");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("这是一款有趣的画图app!");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/storage/emulated/0/yituTemp/share1.png");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://www.nwafu.me");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本仅在人人网使用");
        // 启动分享GUI
        oks.show(this);
    }

    /*private void initShareWindow() {
        dialog = new AlertDialog.Builder(this).create();
        int width = getWindowManager().getDefaultDisplay().getWidth() * 4 / 5;
        dialog.show();
        LayoutParams lp = new LayoutParams(width, LayoutParams.MATCH_PARENT);
        dialog.getWindow().setContentView(view, lp);
        dialog.dismiss();
    }*/

    /*private void share_to_sina() {
        *//*SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setText(SHARE_TEXT);
        sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        sinaWeibo.setPlatformActionListener(this);
        sinaWeibo.share(sp);*//*
        *//*Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image*//**//*");
        intent.putExtra(Intent.EXTRA_SUBJECT,"share");
        intent.putExtra(Intent.EXTRA_TEXT,"震惊!ZQ涂鸦是一款基于android的2d趣味涂鸦软件，支持各种图元创建、填色、变换、浏览等操作.快来下载吧http://www.nwafu.me");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent,getTitle()));
*//*

    }

    private void share_to_renren() {
        *//*cn.sharesdk.renren.Renren.ShareParams sp = new cn.sharesdk.renren.Renren.ShareParams();
        sp.setText(SHARE_TEXT);
        sp.setTitleUrl("http://www.cnsoftbei.com/");
        sp.setTitle("软件分享");
        sp.setComment("赞");
        renren = ShareSDK.getPlatform(Renren.NAME);
        renren.setPlatformActionListener(this);
        renren.share(sp);*//*
    }

    private void share_to_qzone() {
        *//*cn.sharesdk.tencent.qzone.QZone.ShareParams sp = new cn.sharesdk.tencent.qzone.QZone.ShareParams();
        sp.setText(SHARE_TEXT);
        sp.setTitleUrl("http://www.cnsoftbei.com/");
        sp.setTitle("软件分享");
        sp.setSite("易涂");
        sp.setSiteUrl("http://www.cnsoftbei.com/");
        qqZone = ShareSDK.getPlatform(QZone.NAME);
        qqZone.setPlatformActionListener(this);
        qqZone.share(sp);*//*
    }*/
/*
    private void share_to_email() {
        cn.sharesdk.system.email.Email.ShareParams sp = new cn.sharesdk.system.email.Email.ShareParams();
        sp.setText(SHARE_TEXT);
        sp.setAddress("http://www.baidu.com/");
        sp.setTitle("厉害的涂鸦APP");
        email = ShareSDK.getPlatform(Email.NAME);
        email.setPlatformActionListener(this);
        email.share(sp);
    }

    private void share_to_message() {
        cn.sharesdk.system.text.ShortMessage.ShareParams sp = new cn.sharesdk.system.text.ShortMessage.ShareParams();
        sp.setText(SHARE_TEXT);
        message = ShareSDK.getPlatform(ShortMessage.NAME);
        message.setPlatformActionListener(this);
        message.share(sp);
    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_remain, R.anim.out_left);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        Log.i(TAG, "e1x = " + e1.getX() + ",e2x=" + e2.getX());
        Log.i(TAG, "vx=" + velocityX + ",vy=" + velocityY);
        if (Math.abs(velocityX) > Math.abs(velocityY) && (e1.getX() - e2.getX() > VALUE)) {
            this.finish();
            overridePendingTransition(R.anim.slide_remain, R.anim.out_left);
            return true;
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    //-----------------share sdk 部分----------------------------------
    /*@Override
    public void onCancel(Platform arg0, int arg1) {
        Message msg = Message.obtain();
        msg.what = 0;
        UIHandler.sendMessage(msg, this);
    }


    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        Message msg = Message.obtain();
        msg.what = 1;
        UIHandler.sendMessage(msg, this);
    }


    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        Message msg = Message.obtain();
        msg.what = -1;
        UIHandler.sendMessage(msg, this);
    }*/


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                dialog.dismiss();
                break;
            case 0:
                dialog.dismiss();
                break;
            case -1:
                dialog.dismiss();
                break;
        }
        return true;
    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }


    private void clickStyle()
    {
        if (timeclick == 0)
        {
            timeclick = System.currentTimeMillis();
            clickCounts = 0;
        } else
        {
            if (System.currentTimeMillis() - timeclick < 500)    //计算两次单击的时间差
            {
                clickCounts++;
                if (clickCounts == 2)
                {
                    timeclick = System.currentTimeMillis();
                    //CustomToast.makeTextSucess(getApplicationContext(), "", "在按2次就可以进入设置页面了");
                } else if (clickCounts == 3)
                {
                    timeclick = System.currentTimeMillis();
                    //CustomToast.makeTextSucess(getApplicationContext(), "", "在按1次就可以进入设置页面了");
                } else if (clickCounts == 4)
                {
//                    System.out.println("2222");
                    Intent intent = new Intent(this, WebcaidanActivity.class);
                    startActivity(intent);
                    timeclick = System.currentTimeMillis();
                    clickCounts = 0;
                }
            } else
            {
                timeclick = System.currentTimeMillis();
                clickCounts = 0;
            }
        }
    }
}




















