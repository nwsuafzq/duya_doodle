package cn.edu.chd.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2018/4/5.
 */

public class FileCopy {
   /* public static void main(String[] args) {
        CopySdcardFile("","");
    }*/
   public static void getImageBitmap() throws IOException {
       URL url = new URL("http://bbs.nwafu.me/images/QR1.png");
       HttpURLConnection conn = (HttpURLConnection)url.openConnection();
       conn.setConnectTimeout(5000);
       conn.setRequestMethod("GET");

           InputStream inputStream = conn.getInputStream();

           //bitmap = BitmapFactory.decodeStream(is);
           File f = new File("/storage/emulated/0/yituTemp/share1.png");

           OutputStream os = new FileOutputStream(f);
           int bytesRead = 0;
           byte[] buffer = new byte[8192];

               while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                   os.write(buffer, 0, bytesRead);

               }


               os.close();

                 inputStream.close();
           }

       public static int CopySdcardFile(String fromFile, String toFile) {

        try
        {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex)
        {
            return -1;
        }
    }
}
