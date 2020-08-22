package www.xcd.com.mylibrary;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.huantansheng.easyphotos.EasyPhotos;

import java.io.File;

import www.xcd.com.mylibrary.activity.PermissionsActivity;
import www.xcd.com.mylibrary.activity.PermissionsChecker;
import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.utils.GlideEngine;

/**
 * Created by Android on 2017/6/26.
 */

public class PhotoActivity extends SimpleTopbarActivity {

    public static final String IMAGE_UNSPECIFIED = "image/*";
    /**
     * 头像Image
     */
    public ImageView imageHead;
    /**
     * 头像修改菜单
     */
    public View viewChoice;
    /**
     * 头像修改dialog
     */
    public Dialog dlgChoice;

    /**
     * 照片地址
     */
    public String photoPath;
    public File photoFile;
    public String photoName;
    public static final int CAMERA_REQUESTCODE = 20001;
    public static final String[] AUTHORIMAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.CAMERA
    };
    public PermissionsChecker mChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChecker = new PermissionsChecker(this);
        if (savedInstanceState != null) {
            photoPath = savedInstanceState.getString("photoPath");
            photoName = savedInstanceState.getString("photoName");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.account_head_choice_cancel) {// 关闭对话框
            closeChoiceDialog();

        } else if (i == R.id.account_head_choice_album) {// 关闭对话框
            closeChoiceDialog();
            EasyPhotos.createAlbum(this, false, GlideEngine.getInstance())
                    .start(101);
        } else if (i == R.id.account_head_choice_camera) {
            closeChoiceDialog();
            PermissionsChecker mChecker = new PermissionsChecker(this);
            if (mChecker.lacksPermissions(AUTHORIMAGE)) {
                // 请求权限
                PermissionsActivity.startActivityForResult(this, CAMERA_REQUESTCODE, AUTHORIMAGE);
            } else {
                // 全部权限都已获取
                EasyPhotos.createCamera(this)
                        .setFileProviderAuthority("com.sdlifes.sdlifes.fileprovider")
                        .start(101);
            }
        }
    }

    public static Uri geturi(Intent intent, Context context) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);

                    index = cur.getInt(index);
                }
                if (index == 0) {
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    /**
     * 菜单View
     *
     * @param
     * @return
     */
    public View getChoiceView() {
        if (viewChoice == null) {
            // 初始化选择菜单
            viewChoice = LayoutInflater.from(PhotoActivity.this).inflate(R.layout.view_head_choice, null);
            viewChoice.findViewById(R.id.account_head_choice_album).setOnClickListener(this);
            viewChoice.findViewById(R.id.account_head_choice_camera).setOnClickListener(this);
            viewChoice.findViewById(R.id.account_head_choice_cancel).setOnClickListener(this);
        }
        return viewChoice;
    }

    /**
     * 修改头像对话框
     *
     * @return
     */
    public Dialog getChoiceDialog() {
        if (dlgChoice == null) {
            dlgChoice = new Dialog(PhotoActivity.this, R.style.DialogStyle);
            dlgChoice.setContentView(getChoiceView());
            return dlgChoice;
        }
        return dlgChoice;
    }


    /**
     * 关闭对话框
     */
    public void closeChoiceDialog() {
        if (dlgChoice != null && dlgChoice.isShowing()) {
            dlgChoice.cancel();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("photoPath", photoPath);
        savedInstanceState.putString("photoName", photoName);
        super.onSaveInstanceState(savedInstanceState); //实现父类方法 放在最后 防止拍照后无法返回当前activity
    }
}
