package www.xcd.com.mylibrary;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.huantansheng.easyphotos.EasyPhotos;
import com.yonyou.sns.im.util.common.FileUtils;
import com.yonyou.sns.im.util.common.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import www.xcd.com.mylibrary.activity.AlbumPhotoActivity;
import www.xcd.com.mylibrary.activity.PermissionsActivity;
import www.xcd.com.mylibrary.activity.PermissionsChecker;
import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.utils.GlideEngine;
import www.xcd.com.mylibrary.utils.YYStorageUtil;

import static android.graphics.BitmapFactory.decodeFile;
import static www.xcd.com.mylibrary.utils.handler.ResizerBitmapHandler.calculateInSampleSize;

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
     * 显示图片的view
     */
    private int showViewid;

    public int getShowViewid() {
        return showViewid;
    }

    public void setShowViewid(int showViewid) {
        this.showViewid = showViewid;
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
     * 是否显示更改头像后的dialog,默认不显示
     */
    public boolean getIsShowChoiceDialog() {
        return false;
    }

    /**
     * 关闭对话框
     */
    public void closeChoiceDialog() {
        if (dlgChoice != null && dlgChoice.isShowing()) {
            dlgChoice.cancel();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("TAG_相机", "requestCode=" + requestCode + ";resultCode=" + resultCode);
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case REQUEST_CODE_HEAD_ALBUM:
//                    boolean is_origanl = data.getBooleanExtra(IS_ORIGANL, true);
//                    YYPhotoItem photoItem = null;
//                    if (is_origanl) {
//                        photoItem = (YYPhotoItem) data.getSerializableExtra(AlbumPhotoActivity.BUNDLE_RETURN_PHOTO);
//                        if (photoItem != null) {
//                            startCrop(photoItem.getPhotoPath());
//                        }
//                    } else {
//                        final List<File> list = new ArrayList<>();
//                        List<YYPhotoItem> photoList = (List<YYPhotoItem>) data.getSerializableExtra(AlbumPhotoActivity.BUNDLE_RETURN_PHOTOS);
//                        for (YYPhotoItem photo : photoList) {
//                            // 存储图片到图片目录
//                            list.add(new File(photo.getPhotoPath()));
//                        }
//                        uploadImage(list);
//                    }
//
//                    break;
//                case REQUEST_CODE_HEAD_CAMERA:
//                    //剪切功能
//                    Log.e("TAG_剪切", "photoPath=" + photoPath);
//                    if (photoPath != null) {
//                        startCrop(photoPath);
//                    }
//                    break;
//                //直接返回图片
//                case REQUEST_CODE_HEAD_CROP:
//                    try {
//                        Bundle extras = data.getExtras();
//                        if (extras != null) {
//                            Bitmap cropPhoto = extras.getParcelable("data");
//                            if (cropPhoto != null) {
//                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                // (0 - 100)压缩文件
//                                cropPhoto.compress(Bitmap.CompressFormat.JPEG, 75, stream);
//
//                                File cropFile = new File(YYStorageUtil.getImagePath(PhotoActivity.this), getphotoName() + ".jpg");
//                                final List<File> list = new ArrayList<>();
//                                list.add(cropFile);
//                                FileUtils.compressBmpToFile(cropPhoto, cropFile);
//                                uploadImage(list);
//                            }
////
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
////                case PictureConfig.CHOOSE_REQUEST:
////                    // 图片选择结果回调
////                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
////                    // 例如 LocalMedia 里面返回三种path
////                    // 1.media.getPath(); 为原图path
////                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
////                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
////                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
////                    uploadImageLocalMedia(selectList);
////                    break;
//                default:
//                    break;
//            }
//        } else {
//            // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
//            if (requestCode == PERMISSIONS_GRANTED && resultCode == PERMISSIONS_DENIED) {
//                finish();
//            } else {
//                if (getIsShowChoiceDialog()) {
//                    getChoiceDialog().show();
//                }
//            }
//        }
//    }

    public void uploadImage(final List<File> list) {
        // 调用上传

    }
//    public void uploadImageLocalMedia( List<LocalMedia> list) {
//        // 调用上传
//
//    }

    /**
     * AlbumPhotoActivity.TYPE_SINGLE为单选
     * ""多选
     */

    public void startCrop(String imagePath) {
        Log.e("TAG_裁剪", "imagePath=" + imagePath);
        File cropFile = new File(YYStorageUtil.getImagePath(PhotoActivity.this), getphotoName() + ".jpg");
        final List<File> list = new ArrayList<>();
        list.add(cropFile);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeFile(imagePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap cropPhoto = BitmapFactory.decodeFile(imagePath, options);
        if (cropPhoto != null) {
            FileUtils.compressBmpToFile(cropPhoto, cropFile, true, 75, true, 0);
            uploadImage(list);
        } else {
            ToastUtil.showShort(this, "请选择正确图片");
        }
    }

    //单选还是多选
    public String getTpye() {
        return AlbumPhotoActivity.TYPE_SINGLE;
    }

    public void setphotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getphotoName() {
//        return photoName;
        return UUID.randomUUID().toString();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("photoPath", photoPath);
        savedInstanceState.putString("photoName", photoName);
        super.onSaveInstanceState(savedInstanceState); //实现父类方法 放在最后 防止拍照后无法返回当前activity
    }
}
