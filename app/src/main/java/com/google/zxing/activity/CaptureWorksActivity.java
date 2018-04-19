package com.google.zxing.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.decoding.RGBLuminanceSource;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.Hashtable;

import cn.edu.chd.utils.Constant;
import cn.edu.chd.yitu.R;
import cn.edu.chd.yitu.TabMyWorks;

import static com.google.zxing.activity.CaptureActivity.RESULT_CODE_QR_SCAN;

public class CaptureWorksActivity extends Activity {

    private Intent intent;

    private Bitmap scanBitmap;

    private String path;
    private TextView textView;
    private Button btn_back;
    Intent intent2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_works);
        textView = (TextView) findViewById(R.id.textView1);
        btn_back = findViewById(R.id.btn_back);


        intent = this.getIntent();
        path = intent.getStringExtra("path");

        Result result = scanningImage(path);

        String res = "";
        if (result != null) {
            res = result.getText();
            textView.setText(res);
        }else {
            textView.setText("图片中没二维码吧..");
        }

        setBack();

    }

    private void setBack() {
        intent2=new Intent(this,TabMyWorks.class);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //因为我们在initData中已经将传输过来的数据放在intent中，所以这里我们直接用intent即可
                setResult(RESULT_OK, intent2);
                finish();
            }
        });
    }
    public Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException | ChecksumException | FormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
