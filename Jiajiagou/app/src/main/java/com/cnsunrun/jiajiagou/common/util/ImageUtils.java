package com.cnsunrun.jiajiagou.common.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 图片操作工具类
 * <p>
 * author:yyc
 * date: 2017-09-02 14:25
 */
public class ImageUtils {
    /**
     * 截图
     *
     * @param activity
     * @return
     */
    public static Bitmap takeScreenShot(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;

        // 获取屏幕宽和高
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widths = dm.widthPixels;
        int heights = dm.heightPixels;
//        int actionBarHeight = getActionBar().getHeight(); //actionBar的高度,目前没有

        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);

        // 截图时去掉状态栏和actionBar
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeights, widths,
                heights - statusBarHeights);
        view.destroyDrawingCache();
        return bmp;
    }

    /**
     * 放大缩小图片
     *
     * @param filePath  图片的绝对路径
     * @param reqWidth  要求的宽度 640
     * @param reqHeight 要求的高度 480
     * @return true 压缩成功
     */
    public static boolean zoomBitmap(String filePath, int reqWidth, int reqHeight) {
        // 计算sampleSize,只是加载宽高信息
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 调用方法后，option已经有图片宽高信息
        BitmapFactory.decodeFile(filePath, options);
        // 计算最相近缩放比例
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        Bitmap out = BitmapFactory.decodeFile(filePath, options);
        return saveBitmap2file(out, filePath);
    }

    /**
     * 放大缩小图片(放到应用缓存文件夹,防止把用户在app外部拍的大图给覆盖了)
     *
     * @param filePath  图片的绝对路径
     * @param reqWidth  要求的宽度 640
     * @param reqHeight 要求的高度 480
     */
    public static String zoomBitmap2File(String filePath, int reqWidth, int reqHeight) {
        // 计算sampleSize,只是加载宽高信息
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 调用方法后，option已经有图片宽高信息
        BitmapFactory.decodeFile(filePath, options);
        // 计算最相近缩放比例
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        File file = new File(ConstantUtils.ExternalCacheDir + "/img" + System.currentTimeMillis() / 1000 +
                ".jpg");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {

        }finally {
            bitmap.recycle();
        }

        return file.getAbsolutePath();
    }

    /**
     * 放大缩小图片(放到应用缓存文件夹,防止把用户在app外部拍的大图给覆盖了)
     *
     * @param filePath 图片的绝对路径
     */
    public static String zoomBitmap2File(String filePath) {

        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        File file = new File(ConstantUtils.ExternalCacheDir + "/img" + System.currentTimeMillis() / 1000 +
                ".jpg");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
            bos.flush();
            bos.close();

            //旋转图片
//            WLXImageUtils.rotaingImageView(readPictureDegree(imagePath),imagePath);
        } catch (IOException e) {

        } finally {
            bitmap.recycle();
        }


        return file.getAbsolutePath();
    }

    /**
     * 获取缩略图
     *
     * @param filePath
     * @param scalSize >0
     * @return
     */
    @Nullable
    public static Bitmap getThumbnail(String filePath, int scalSize) {
        if (scalSize <= 0) return null;

        // 计算sampleSize,只是加载宽高信息
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 调用方法后，option已经有图片宽高信息
        BitmapFactory.decodeFile(filePath, options);

        // 计算最相近缩放比例
        options.inSampleSize = options.outWidth / scalSize;
        options.inJustDecodeBounds = false;


        return BitmapFactory.decodeFile(filePath, options);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 计算图片的缩放值
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int
            reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 压缩图片到指定位置(默认png格式)
     *
     * @param bitmap       需要压缩的图片
     * @param compressPath 生成文件路径(例如: /storage/imageCache/1.png)
     * @param quality      图片质量，0~100
     * @return if true,保存成功
     */
    public static boolean compressBitmap(Bitmap bitmap, String compressPath, int quality) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(compressPath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);// (0-100)压缩文件
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            bitmap.recycle();
            try {
                stream.flush();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }


    /**
     * Get bitmap from specified image path
     *
     * @param imgPath
     * @return
     */
    public static Bitmap getBitmap(String imgPath) {
        // Get bitmap through image path
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // Do not compress
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(imgPath, newOpts);
    }

    /**
     * 生成bitmap
     *
     * @param file
     * @return
     */
    public static Bitmap getBitmap(File file) {
        // Get bitmap through image path
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // Do not compress
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), newOpts);
    }

    /**
     * 保存文件到制定路径
     *
     * @param bmp
     * @param filePath
     * @return
     */
    public static boolean saveBitmap2file(Bitmap bmp, String filePath) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 70;
        OutputStream stream;

        boolean isCompress = false;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            stream = new FileOutputStream(filePath);
            isCompress = bmp.compress(format, quality, stream);

            stream.flush();
            stream.close();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            bmp.recycle();
        }
        return isCompress;
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            if (!StringUtils.isEmpty(path)) {
                ExifInterface exifInterface = new ExifInterface(path);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /* 旋转图片
    * @param angle
    * @param bitmap
    * @return Bitmap
    */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap
                .getHeight(), matrix, true);

        return resizedBitmap;
    }

    /* 旋转图片
    * @param angle
    * @param bitmap
    * @return true 成功
    */
    public static boolean rotaingImageView(int angle, String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap
                .getHeight(), matrix, true);
        bitmap.recycle();
        return saveBitmap2file(resizedBitmap, path);
    }
}
