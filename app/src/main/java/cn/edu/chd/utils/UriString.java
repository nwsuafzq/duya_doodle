package cn.edu.chd.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/** 通过Uri获取路径
 * Created by ZhangQiong.
 */

public class UriString {
    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI,
                new String[]{MediaStore.Images.ImageColumns.DATA},//
                null, null, null);
        if (cursor == null) result = contentURI.getPath();
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }
}
