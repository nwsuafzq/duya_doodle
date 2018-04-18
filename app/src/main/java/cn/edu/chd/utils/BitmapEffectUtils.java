package cn.edu.chd.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * @author ZhangQiong
 *
 *图片的效果
 */
public class BitmapEffectUtils
{
    private BitmapEffectUtils(){}

    /**
     * 黑白
     * @return
     */
    public static Bitmap blackAndWhiteEffect(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap base = Bitmap.createBitmap(width, height,bitmap.getConfig());

        Canvas canvas = new Canvas(base);//以base为模板创建canvas对象
        canvas.drawBitmap(bitmap, new Matrix(),new Paint());

        for(int i = 0; i < width; i++)//遍历像素点
        {
            for(int j = 0; j < height; j++)
            {
                int color = bitmap.getPixel(i, j);

                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                int a = Color.alpha(color);

                int avg = (r+g+b)/3;//RGB均值

                if(avg >= 100)//100可以理解为经验值
                {
                    base.setPixel(i, j,Color.argb(a, 255, 255, 255));//设为白色
                }
                else
                {
                    base.setPixel(i, j,Color.argb(a, 0, 0, 0));//设为黑色
                }
            }
        }
        return base;
    }
    /**
     * 底片
     * @return
     */
    public static Bitmap dipianEffect(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //创建可写的bitmap
        Bitmap base = Bitmap.createBitmap(width,height,bitmap.getConfig());
        Paint paint = new Paint();//新建画笔
//      新建画布
        Canvas canvas = new Canvas(base);
        canvas.drawBitmap(bitmap, new Matrix(), paint);
        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {
                int color = bitmap.getPixel(i,j);
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                int a = Color.alpha(color);
                //修改图片每个点的像素的rgb值
                base.setPixel(i,j, Color.argb(a, 255-r, 255-g, 255-b));
            }
        }
        return base;
    }
    /**
     * 灰度
     * @param bitmap
     * @return
     */
    public static Bitmap huiduEffect(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap base = Bitmap.createBitmap(width, height,bitmap.getConfig());

        Canvas canvas = new Canvas(base);//以base为模板创建canvas对象
        canvas.drawBitmap(bitmap, new Matrix(),new Paint());

        for(int i = 0; i < width; i++)//遍历像素点
        {
            for(int j = 0; j < height; j++)
            {
                int color = bitmap.getPixel(i, j);

                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                int a = Color.alpha(color);

                int value = (int) (r*0.3+g*0.59+b*0.11);//经验公式
                int newColor = Color.argb(a, value, value, value);
                base.setPixel(i, j,newColor);
            }
        }
        return base;
    }

    /**
     * 怀旧
     * @param bitmap
     * @return
     */
    public static Bitmap huaijiuEffect(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap base = Bitmap.createBitmap(width, height,bitmap.getConfig());

        Canvas canvas = new Canvas(base);//以base为模板创建canvas对象
        canvas.drawBitmap(bitmap, new Matrix(),new Paint());

        int newR,newG,newB;
        for(int i = 0; i < width; i++)//遍历像素点
        {
            for(int j = 0; j < height; j++)
            {
                int current_color = bitmap.getPixel(i, j);
                int r = Color.red(current_color);
                int g = Color.green(current_color);
                int b = Color.blue(current_color);
                int a = Color.alpha(current_color);

                /*经验公式*/
                newR = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                newG = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                newB = (int) (0.272 * r + 0.534 * g + 0.131 * b);

                int newColor = Color.argb(a, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
                base.setPixel(i, j, newColor);
            }
        }
        return base;
    }

    /**
     * 浮雕
     * @param bitmap
     * @return
     */
    public static Bitmap fudiaoEffect(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap base = Bitmap.createBitmap(width, height,bitmap.getConfig());

        Canvas canvas = new Canvas(base);//以base为模板创建canvas对象
        canvas.drawBitmap(bitmap, new Matrix(),new Paint());

        int pre_color = 0;//记录上一个点的rgb值
        for(int i = 0; i < width; i++)//遍历像素点
        {
            for(int j = 0; j < height; j++)
            {
                int current_color = bitmap.getPixel(i, j);

                int r = Color.red(current_color) - Color.red(pre_color)+128;
                int g = Color.green(current_color) - Color.green(pre_color)+128;
                int b = Color.blue(current_color) - Color.blue(pre_color)+128;
                int a = Color.alpha(current_color);

                int newcolor = (int)(r * 0.3 + g * 0.59 + b * 0.11);//灰度处理

                base.setPixel(i, j, Color.argb(a, newcolor, newcolor, newcolor));
                pre_color = current_color;
            }
        }
        return base;
    }
    /*public static Bitmap getMosaicsBitmap(Bitmap bmp, double precent) {
        long start = System.currentTimeMillis();
        int bmpW = bmp.getWidth();
        int bmpH = bmp.getHeight();
        Bitmap resultBmp = Bitmap.createBitmap(bmpW, bmpH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBmp);
        Paint paint = new Paint();
        double unit;
        if (precent == 0) {
            unit = bmpW;
        } else {
            unit = 1 / precent;
        }
        double resultBmpW = bmpW / unit;
        double resultBmpH = bmpH / unit;
        for (int i = 0; i < resultBmpH; i++) {
            for (int j = 0; j < resultBmpW; j++) {
                int pickPointX = (int) (unit * (j + 0.5));
                int pickPointY = (int) (unit * (i + 0.5));
                int color;
                if (pickPointX >= bmpW || pickPointY >= bmpH) {
                    color = bmp.getPixel(bmpW / 2, bmpH / 2);
                } else {
                    color = bmp.getPixel(pickPointX, pickPointY);
                }
                paint.setColor(color);
                canvas.drawRect((int) (unit * j), (int) (unit * i), (int) (unit * (j + 1)), (int) (unit * (i + 1)), paint);
            }
        }
        canvas.setBitmap(null);
        long end = System.currentTimeMillis();
//        Log.v(TAG, "DrawTime:" + (end - start));
        return resultBmp;
    }


    public static Bitmap getMosaicsBitmaps(Bitmap bmp, double precent) {
        long start = System.currentTimeMillis();
        int bmpW = bmp.getWidth();
        int bmpH = bmp.getHeight();
        int[] pixels = new int[bmpH * bmpW];
        bmp.getPixels(pixels, 0, bmpW, 0, 0, bmpW, bmpH);
        int raw = (int) (bmpW * precent);
        int unit;
        if (raw == 0) {
            unit = bmpW;
        } else {
            unit = bmpW / raw; //原来的unit*unit像素点合成一个，使用原左上角的值
        }
        if (unit >= bmpW || unit >= bmpH) {
            return getMosaicsBitmap(bmp, precent);
        }
        for (int i = 0; i < bmpH; ) {
            for (int j = 0; j < bmpW; ) {
                int leftTopPoint = i * bmpW + j;
                for (int k = 0; k < unit; k++) {
                    for (int m = 0; m < unit; m++) {
                        int point = (i + k) * bmpW + (j + m);
                        if (point < pixels.length) {
                            pixels[point] = pixels[leftTopPoint];
                        }
                    }
                }
                j += unit;
            }
            i += unit;
        }.
        long end = System.currentTimeMillis();
       // Log.v(TAG, "DrawTime:" + (end - start));
        return Bitmap.createBitmap(pixels, bmpW, bmpH, Bitmap.Config.ARGB_8888);
    }
*/

    //高斯模糊
    public static Bitmap mohuEffect(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at="" quasimondo.com="">
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at="" kayenko.com="">
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}


























