package org.zky.tool.magnetsearch.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.zky.tool.magnetsearch.R;

import java.io.File;

/**
 * 二维码制作对话框
 * Created by zhangkun on 2017/3/2.
 */

public class QrDialogManager implements View.OnClickListener {

    private Context mContext;

    private Dialog mDialog;

    private ImageView ivQr;

    private TextView tvTitle;

    private Button btnOpenFile;

    private Button btnShare;

    private Bitmap mQrBitmap;

    private String title;

    private File file;

    public QrDialogManager(Context context) {
        mContext = context;
    }

    public void show() {
        mDialog = new Dialog(mContext, R.style.Theme_QrDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_qr_code, null);
        mDialog.setContentView(view);

        ivQr = (ImageView) view.findViewById(R.id.iv_qr_code);
        btnOpenFile = (Button) view.findViewById(R.id.btn_open);
        btnShare = (Button) view.findViewById(R.id.btn_share);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btnOpenFile.setOnClickListener(this);
        btnShare.setOnClickListener(this);

        mDialog.show();
    }

    public void openFile() {
        //TODO 7.0权限修改，不能直接读取私有目录文件 待解决
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.N){
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri =
                    Uri.fromFile(file);

            intent.setDataAndType(uri, "image/*");
            mContext.startActivity(intent);
        }

    }
    public void shareText(String text){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); //纯文本
        intent.putExtra(Intent.EXTRA_SUBJECT, GetRes.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(Intent.createChooser(intent, GetRes.getString(R.string.share_to)));
    }

    public void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");//添加图片

        Uri uri = Uri.fromFile(file);
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        intent.putExtra(Intent.EXTRA_SUBJECT, GetRes.getString(R.string.app_name));
        if (title != null) {
            intent.putExtra(Intent.EXTRA_TEXT, title);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(Intent.createChooser(intent, GetRes.getString(R.string.share_to)));
    }

    public void createQR(String content, String title, String hash) {
        try {
            mQrBitmap = QrUtils.encode(content, 300, 300);
            ivQr.setImageBitmap(mQrBitmap);

            tvTitle.setText(title);
            this.title = title;

            file = QrUtils.qrBitmap2file(mQrBitmap, hash);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open:
                if (file != null)
                    openFile();
                break;
            case R.id.btn_share:
                if (file != null)
                    share();
                break;
        }
    }
}
