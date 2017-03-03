package org.zky.tool.magnetsearch.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.zky.tool.magnetsearch.MagnetSearchApp;
import org.zky.tool.magnetsearch.constants.StorageConstants;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * Created by zhangkun on 2017/3/1.
 */

public class QrUtils {

    public static Bitmap encode(String content, int width, int height) throws Exception {
            Map<EncodeHintType, Object> hints = new Hashtable<>();
            // 指定纠错等级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            // 指定编码格式
            hints.put(EncodeHintType.CHARACTER_SET, "GBK");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            int w = bitMatrix.getWidth();
            int h = bitMatrix.getHeight();
            int[] data = new int[w * h];

            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if (bitMatrix.get(x, y))
                        data[y * w + x] = 0xff000000;// 黑色
                    else
                        data[y * w + x] = -1;// -1 相当于0xffffffff 白色
                }
            }
            // 创建一张bitmap图片，采用最高的图片效果ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
// 将上面的二维码颜色数组传入，生成图片颜色
            bitmap.setPixels(data, 0, w, 0, 0, w, h);


        return bitmap;
    }

    public static File qrBitmap2file(Bitmap bitmap,String hash) throws Exception {
        File file = new File(StorageConstants.QR_DIR+ hash + ".png");
        if (!file.exists()){
            file.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
            bos.flush();
            bos.close();
        }
        return file;
    }
}
