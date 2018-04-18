package cn.edu.chd.yitu;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import java.util.ArrayList;

import cn.edu.chd.utils.BitmapUtils;
import cn.edu.chd.utils.FaceTest;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class FaceCheckActivity extends Activity {
    private Intent intent;
    private Bundle bundle;
    private Button button;
    private Button button_share;
    private TextView textView;
    private ImageView imageView;
    public static File file1;
    public static ArrayList faceResult = new ArrayList();

    String res="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facecheck);
        /*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());*/
        initData();
        setBackData();
    }
    private void initData(){
        textView=(TextView)findViewById(R.id.textView);
        button=(Button)findViewById(R.id.button_back);
        button_share=findViewById(R.id.button_share);

        imageView=findViewById(R.id.imageView);

        intent=this.getIntent();
        bundle=intent.getExtras();
        String file=bundle.getString("fileUri1");

        file1=new File(file);
        Log.i("fdsfds",file.toString());

        try {
            faceResult = FaceTest.faceTest(file1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Toast.makeText(this, "性别:" + faceResult.get(0) + "\n年龄:" + faceResult.get(1) + "\n颜值分:\n女性角度 " + faceResult.get(2) + "\n男性角度 " + faceResult.get(3), Toast.LENGTH_LONG).show();
    }

    private void setBackData(){

        if (faceResult.size()==0){
            //Toast.makeText(getActivity(),"恐怕这个图片太大了，换一张试试吧",Toast.LENGTH_LONG).show();
            res="恐怕这个图片太大了，换一张试试吧;或者检查一下网络";
        }else if (faceResult.get(0).equals("0")){
            //Toast.makeText(getActivity(),"貌似这里没有人类的脸啊??",Toast.LENGTH_LONG).show();
            res="貌似这里没有人类的脸啊??";
        }else {
            res="性别:" + faceResult.get(0) + "\n年龄:" + faceResult.get(1) + "\n颜值分:\n女性角度 " + faceResult.get(2) + "\n男性角度 " + faceResult.get(3);
//            textView.setText("性别:" + faceResult.get(0) + "\n年龄:" + faceResult.get(1) + "\n颜值分:\n女性角度 " + faceResult.get(2) + "\n男性角度 " + faceResult.get(3));

        }
        textView.setText(res);
        Uri uri = Uri.fromFile(file1);
        ContentResolver cr = this.getContentResolver();
        //            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

//        Resources resources = this.getResources();
//        DisplayMetrics dm = resources.getDisplayMetrics();
        //float density1 = dm.density;
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;

//        Log.i("width", width+"");
//        Log.i("height",height+"");
        Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFile(file1.getAbsolutePath(),500,800);
        imageView.setImageBitmap(bitmap);

        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();

                // title标题，微信、QQ和QQ空间等平台使用
                oks.setTitle("ZQ小度涂鸦APP");
                // titleUrl QQ和QQ空间跳转链接
                oks.setTitleUrl("http://www.nwafu.me");
                // text是分享文本，所有平台都需要这个字段
                oks.setText(res);
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                oks.setImagePath(file1.getAbsolutePath());//确保SDcard下面存在此张图片
//                Log.i("ADSAD",file1.getAbsolutePath());
                // url在微信、微博，Facebook等平台中使用
                oks.setUrl("http://www.nwafu.me");
                // comment是我对这条分享的评论，仅在人人网使用
                oks.setComment("我是测试评论文本仅在人人网使用");
                // 启动分享GUI
                oks.show(FaceCheckActivity.this);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //因为我们在initData中已经将传输过来的数据放在intent中，所以这里我们直接用intent即可
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

}
