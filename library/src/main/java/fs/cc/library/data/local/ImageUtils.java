package fs.cc.library.data.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import fs.cc.library.utils.*;
import fs.cc.library.data.*;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by fostion on 2015/11/26.
 * 粗粒本地获取图片
 */
public class ImageUtils {

    /**
     * 搜索外插sd卡和内置sd卡里面的图片
     *
     * @param context
     */
    public static void getPhoneImages(final Context context, final MyCallback.Callback1<ArrayList<Image>> callback) {
        //使用线程获取图片数据，callback回调
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Image> images = new ArrayList<>();
                boolean isHasSDCard = true;
                //先判断是否有sd卡
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    isHasSDCard = false;
                    L.d("没有外置SD卡，只搜索内置SD卡图片");
                }

                try {
                    ContentResolver contentResolver = context.getContentResolver();
                    //获取内置图片
                    Cursor innerCursor = contentResolver.query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, null, null, null, null);
                    for (innerCursor.moveToFirst(); !innerCursor.isAfterLast(); innerCursor.moveToNext()) {
                        Image image = new Image();
                        image.setId(innerCursor.getInt(innerCursor.getColumnIndex(MediaStore.Images.Media._ID)));
                        image.setName(innerCursor.getString(innerCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
                        image.setPath(innerCursor.getString(innerCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                        images.add(image);
                    }

                    if (isHasSDCard) {
                        //有外插内存卡才使用外插内存卡
                        Cursor outerCursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
                        for (outerCursor.moveToFirst(); !outerCursor.isAfterLast(); outerCursor.moveToNext()) {
                            Image image = new Image();
                            image.setId(outerCursor.getInt(outerCursor.getColumnIndex(MediaStore.Images.Media._ID)));
                            image.setName(outerCursor.getString(outerCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
                            image.setPath(outerCursor.getString(outerCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                            images.add(image);
                        }
                        outerCursor.close();
                    }
                    innerCursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    L.e("获取图片出现异常失败");
                }

                Collections.sort(images);
                for (Image image : images) {
                    L.e(image.toString());
                }
                callback.run(images);
            }
        }).start();
    }
}
