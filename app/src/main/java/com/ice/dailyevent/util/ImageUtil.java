package com.ice.dailyevent.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;

import com.ice.dailyevent.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by asd on 4/16/2017.
 */

public class ImageUtil {
    public static boolean setImageByPath(ImageView  imageView, String path){
        Bitmap bitmap = null;
        try{
           File file = new File(path);
            if(file.exists() && file.isFile()) {
/*                 BitmapFactory.Options options = new BitmapFactory.Options();
                // 先设置为TRUE不加载到内存中，但可以得到宽和高
                options.inJustDecodeBounds = true;
                Bitmap bm = BitmapFactory.decodeFile(path, options); // 此时返回bm为空
                options.inJustDecodeBounds = false;
                // 计算缩放比
                int be = (int) (options.outHeight / (float) imageView.getHeight());
                if (be <= 0)
                    be = 1;
                options.inSampleSize = be;*/
                bitmap = BitmapFactory.decodeFile(path);

                imageView.setImageBitmap(bitmap);

        }
        }catch (Exception e){
            return false;
        }finally {
            if(bitmap==null){
                imageView.setImageResource(R.drawable.jay_bg);
            }
        }

        return true;
    }


    public static final String ImagePath =Environment.getExternalStorageDirectory().getPath()+"/DailyEvent/";

    public static final String ImageName = "user_image.jpg";

    public static void saveFile(Bitmap bm, String fileName, Activity activity) throws IOException {

        File dirFile = new File(ImagePath);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ImagePath + fileName);

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        if(bos == null ||Bitmap.CompressFormat.JPEG==null){
            return;
        }

        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }
}
