package org.zky.tool.magnetsearch.utils;

import android.graphics.Path;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.zky.tool.magnetsearch.MagnetSearchApp;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * Created by zhangkun on 2017/3/1.
 */

public class QrUtils {

    public static File encode(String content, int width, int height,String hash) throws Exception {
        File file = new File(MagnetSearchApp.getInstanse().getExternalCacheDir()+File.separator+hash+".png");
        if (!file.exists()){
            file.createNewFile();
            Map<EncodeHintType, Object> hints = new Hashtable<>();
            // 指定纠错等级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            // 指定编码格式
            hints.put(EncodeHintType.CHARACTER_SET, "GBK");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,width,height,hints);
            MatrixToImageWriter.writeToStream(bitMatrix,"png",new FileOutputStream(file));
        }
        return file;
    }
}
