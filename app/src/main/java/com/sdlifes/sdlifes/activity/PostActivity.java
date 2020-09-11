package com.sdlifes.sdlifes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.huantansheng.easyphotos.EasyPhotos;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.adapter.NoScrollGridView;
import com.sdlifes.sdlifes.adapter.PostGridAdapter;
import com.sdlifes.sdlifes.dialog.PostDialogFragment;
import com.sdlifes.sdlifes.func.PostRightTopBtnFunc;
import com.sdlifes.sdlifes.network.UrlAddr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.PhotoActivity;
import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.utils.ShareHelper;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class PostActivity extends PhotoActivity {

    private EditText et_post;
    PostGridAdapter adapter;
    private TextView tv_add_topicArr;
    private static Class<?> rightFuncArray[] = {PostRightTopBtnFunc.class};

    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected Object getTopbarTitle() {
        return "编辑发布";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        et_post = findViewById(R.id.et_post);
        NoScrollGridView noScrollGridView = findViewById(R.id.gridView);
        adapter = new PostGridAdapter(noScrollGridView, this);
        noScrollGridView.setAdapter(adapter);
        noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        tv_add_topicArr = findViewById(R.id.tv_add_topicArr);
        tv_add_topicArr.setOnClickListener(this);
    }


    public void selectImage() {
        getChoiceDialog().show();
    }
    private String topicid = null;
    private String title;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {

            case 101: {
                //返回图片地址集合：如果你只需要获取图片的地址，可以用这个
                ArrayList<String> resultPaths = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS);
                //返回图片地址集合时如果你需要知道用户选择图片时是否选择了原图选项，用如下方法获取
                boolean selectedOriginal = data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);
                if (resultPaths != null && resultPaths.size() > 0) {
                    String path = resultPaths.get(0);
                    List<String> list = adapter.getData();
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(path);
                    adapter.setData(list);
                }
            }
            break;
            case 11:
                title = data.getStringExtra("title");
                tv_add_topicArr.setText("#\t"+title);
                topicid = data.getStringExtra("topicid");
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        String etPost = et_post.getText().toString().trim();
        List<String> list = adapter.getData();
        if (!TextUtils.isEmpty(etPost) || (list != null && list.size() > 0)) {
            PostDialogFragment dialogFr = new PostDialogFragment();
            dialogFr.show(getSupportFragmentManager(), "PostActivity");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_add_topicArr:
                Intent intent = new Intent(this, PostMoreActivity.class);

                startActivityForResult(intent, 11);

                break;
        }
    }

    public void editor() {
        String etPost = et_post.getText().toString().trim();
        if (TextUtils.isEmpty(etPost)) {
            ToastUtil.showToast("话题内容不能为空！");
            return;
        }
        List<String> list = adapter.getData();
        if (list == null || list.size() == 0) {
            ToastUtil.showToast("请上传图片！");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                buffer.append(",");
            }
            String s = list.get(i);
            buffer.append(s);
        }

        if (TextUtils.isEmpty(topicid)) {
            ToastUtil.showToast("请选择话题！");
            return;
        }

        String userId = ShareHelper.getUserId();
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", userId);
        params.put("topicid", topicid);
        params.put("context", etPost);
        params.put("pic", buffer.toString());
        OkHttpHelper.postAsyncHttp(this, 1000, params, UrlAddr.TOPIC_SAVE, this);
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        switch (requestCode) {
            case 1000:
                ToastUtil.showToast("发布成功！");
                finish();
                break;

        }
    }
}
