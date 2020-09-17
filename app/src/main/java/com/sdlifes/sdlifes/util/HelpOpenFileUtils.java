package com.sdlifes.sdlifes.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.sdlifes.sdlifes.util.permissions.PermissionsActivity;
import com.sdlifes.sdlifes.util.permissions.PermissionsChecker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import www.xcd.com.mylibrary.utils.ToastUtil;

/**
 * Data:  2019/6/18
 * Auther: xcd
 * Description:
 */
public class HelpOpenFileUtils {

    /**
     * 读写权限
     */
    protected static final int WRITE_PERMISSION = 20003;
    protected static final String[] WRITEPERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static String mDownloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator;

    private static final String TAG = "TAG_HelpOpenFileUtils";

    private static void openFile(FragmentActivity context, File downloadFile) {
        String fileName = downloadFile.getName();
        openFile(context,downloadFile,fileName);
    }
    public static void openFile(FragmentActivity context, File downloadFile, String fileName) {
        Log.e("TAG_文件下载", "downloadFile=" + downloadFile.getPath());
        String name = fileName .toLowerCase();
        Log.e("TAG_文件下载", "name=" + name);
       if (name.endsWith(".jpg") || name.endsWith(".png")
                || name.endsWith(".gif")){
           //发送广播刷新相册。
           Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
           Uri uri = Uri.fromFile(downloadFile);
           intent.setData(uri);
           context.sendBroadcast(intent);
           ToastUtil.showToast("保存成功！");
        }
    }

    public static void nativeDownloadFile(final FragmentActivity context, String url, final File downloadFile) {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Log.e(TAG, "下载url=" + url);
        Request request = new Request.Builder().url(url).get().build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG_文件下载","onFailure");
               ToastUtil.showToast("下载文件失败！！！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code >= 500) {
                    Log.e("TAG_文件下载","code="+code);
                    ToastUtil.showToast("下载文件失败！！！");
                } else if (code >= 200 && code < 300) {
                    InputStream is = response.body().byteStream();
                    byte[] buf = new byte[8 * 1024];
                    int len = 0;
                    long sum = 0;
                    OutputStream fos = null;
                    try {
                        Log.e("TAG_文件下载","downloadFile="+downloadFile);
                        fos = new FileOutputStream(downloadFile);

                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                        }
                        fos.flush();
                        openFile(context, downloadFile);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("TAG_文件下载","IOException=");
                        ToastUtil.showToast("下载文件失败！！！");
                    } finally {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    }
                }

            }
        });
    }

    //文件下载
    public static void downloadFile(FragmentActivity context, String url) {
        if (url.indexOf(".")!=-1){
            int dian = url.lastIndexOf(".");
            String type = "";
            if (url.indexOf("/?")!=-1){
                int i = url.lastIndexOf("/?");
                type=url.substring(dian,i);
            } else if (url.indexOf("?")!=-1){
                int i = url.lastIndexOf("?");
                type=url.substring(dian,i);
            }else {
                type=url.substring(dian+1);
            }
            Log.e(TAG, "type=" + type);
            downloadFile(context,url,type);
        }
    }
    public static void downloadFile(FragmentActivity context, String url, String type) {

            PermissionsChecker mChecker = new PermissionsChecker(context);
            if (mChecker.lacksPermissions(WRITEPERMISSION)) {
                // 请求权限
                PermissionsActivity.startActivityForResult((Activity) context, WRITE_PERMISSION, WRITEPERMISSION);
            } else {
                // 全部权限都已获取
                Log.e(TAG, "url=" + url);
                if (TextUtils.isEmpty(url)) {
                    Log.e(TAG, "downloadFile: url下载地址为空！！！");
                    return;
                }
//                try {
//                    int indexOf = url.indexOf("file_name=");
//                    if (indexOf != -1) {
//                        url = url.substring(0, indexOf + 10) + URLEncoder.encode(url.substring(indexOf + 10), "UTF-8");
//                        Log.e(TAG, "url=" + url);
//                    }
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }

                File downloadFile = createDefaultTimeFile(type);
                if (null == downloadFile) {
                    Log.e(TAG, "downloadFile: 文件创建失败！！！");
                    return;
                }
                nativeDownloadFile(context,url, downloadFile);
            }
    }
    private static File createDefaultTimeFile(String type) {
        SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = dateformat1.format(System.currentTimeMillis());
        File downloadFile = new File(mDownloadDir, dateStr + "." + type);
        return createFile(downloadFile);
    }
    public static File createFile(File file) {
        Log.e(TAG, "file=" + file.toString());
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                Log.e(TAG, "createFile: 目录创建失败！！！");
                return null;
            }
        }
        try {
            file = new File(file.toString());
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;

    }
}
