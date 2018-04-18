package cn.edu.chd.utils;

import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import javax.net.ssl.SSLException;

public class FaceTest {
    public static byte[] bacd;
    public static String url;
    public static HashMap<String, String> map;
    public static HashMap<String, byte[]> byteMap;

    public static ArrayList faceTest(File file) throws Exception {
        byte[] buff = getBytesFromFile(file);
        url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
        map = new HashMap<>();
        byteMap = new HashMap<>();
        map.put("api_key", "LE8hQe915MT7CC7vUElgXeXirL3LObNR");
        map.put("api_secret", "fNP2VQR6P7IVuZLqIsVNSPwViqZCAlYq");
        map.put("return_landmark", "1");
//        map.put("return_attributes", "gender,age,smiling,headpose,facequality,blur,eyestatus,emotion,ethnicity,beauty,mouthstatus,eyegaze,skinstatus");
        map.put("return_attributes", "gender,age,beauty");
        byteMap.put("image_file", buff);

        ArrayList list = new ArrayList();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    bacd = post(url, map, byteMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start(); //开一个线程执行post方法 等5s

        Thread.sleep(5000);

        String str = new String(bacd);
            Log.i("FaceTest",str);
        JSONObject jsonObject = new JSONObject(str);
//            Log.i("jsonObject",jsonObject.toString());
        if (Objects.equals(jsonObject.toString(), "") || jsonObject.length() == 0) { //图片中没有人脸
            return list;
        }
        JSONArray facesArray = jsonObject.getJSONArray("faces");
        if (Objects.equals(facesArray.toString(), "") || facesArray.length() == 0) { //图片过大
//            Log.i("aadasd", "????" + facesArray.length());
            list.add("0");
            return list;
        }
//        Log.i("facesArray", facesArray.toString());
        JSONObject face = facesArray.getJSONObject(0);
        JSONObject attributes = face.getJSONObject("attributes");

        String gender = attributes.getJSONObject("gender").getString("value");
        String age = attributes.getJSONObject("age").getString("value");
        String beautyFe = attributes.getJSONObject("beauty").getString("female_score");
        String beautyMa = attributes.getJSONObject("beauty").getString("male_score");
            /*String[] res=new String[4];
            res[0]=gender;
            res[1]=age;
            res[2]=beautyFe;
            res[3]=beautyMa;*/

        list.add(gender);
        list.add(age);
        list.add(beautyFe);
        list.add(beautyMa);

        return list;

    }

  /*  public static void main(String[] args) throws Exception{

        File file = new File("/Users/ZhangQiong/Desktop/d3d151baabbd13d9bc.jpg");
        byte[] buff = getBytesFromFile(file);

        String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", "LE8hQe915MT7CC7vUElgXeXirL3LObNR");
        map.put("api_secret", "fNP2VQR6P7IVuZLqIsVNSPwViqZCAlYq");
        map.put("return_landmark", "1");
        map.put("return_attributes", "gender,age,smiling,headpose,facequality,blur,eyestatus,emotion,ethnicity,beauty,mouthstatus,eyegaze,skinstatus");
        byteMap.put("image_file", buff);
        try{
            byte[] bacd = post(url, map, byteMap);
            String str = new String(bacd);
            System.out.println(str);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private final static int CONNECT_TIME_OUT = 30000;
    private final static int READ_OUT_TIME = 50000;
    private static String boundaryString = getBoundary();

    protected static byte[] post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {


        HttpURLConnection conne;
        URL url1 = new URL(url);
        conne = (HttpURLConnection) url1.openConnection();
        conne.setDoOutput(true);
        conne.setUseCaches(false);
        conne.setRequestMethod("POST");
        conne.setConnectTimeout(CONNECT_TIME_OUT);
        conne.setReadTimeout(READ_OUT_TIME);
        conne.setRequestProperty("accept", "*/*");
        conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        conne.setRequestProperty("connection", "Keep-Alive");
        conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
//        Log.i("Test","out "+conne.getOutputStream().toString());
        DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
            obos.writeBytes("--" + boundaryString + "\r\n");
            obos.writeBytes("Content-Disposition: form-data; name=\"" + key
                    + "\"\r\n");
            obos.writeBytes("\r\n");
            obos.writeBytes(value + "\r\n");
        }
        if (fileMap != null && fileMap.size() > 0) {
            Iterator fileIter = fileMap.entrySet().iterator();
            while (fileIter.hasNext()) {
                Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
                obos.writeBytes("--" + boundaryString + "\r\n");
                obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                        + "\"; filename=\"" + encode(" ") + "\"\r\n");
                obos.writeBytes("\r\n");
                obos.write(fileEntry.getValue());
                obos.writeBytes("\r\n");
            }
        }
        obos.writeBytes("--" + boundaryString + "--" + "\r\n");
        obos.writeBytes("\r\n");
        obos.flush();
        obos.close();
        InputStream ins = null;
        int code = conne.getResponseCode();
        try {
            if (code == 200) {
                ins = conne.getInputStream();
            } else {
                ins = conne.getErrorStream();
            }
        } catch (SSLException e) {
            e.printStackTrace();
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len;
        while ((len = ins.read(buff)) != -1) {
            baos.write(buff, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        ins.close();

        baos.close();

        return bytes;
    }

    private static String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }

    private static String encode(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8");
    }

    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
//            Log.i("Test","F为NULL");
            return null;
        }
        try {
//            Log.i("Test","1111");
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1) {
                //Log.i("Test","222");
                out.write(b, 0, n);
            }
            stream.close();
            out.close();
//            Log.i("Test","ccc "+out.toByteArray().length);
//            Log.i("Test","ddd "+out.toByteArray()[0]+" "+out.toByteArray()[1]);
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }
}