package com.sdlifes.sdlifes.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.adapter.DragAdapter;
import com.sdlifes.sdlifes.adapter.OtherAdapter;
import com.sdlifes.sdlifes.model.ChannelModel;
import com.sdlifes.sdlifes.network.Constant;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.EventBusMsg;
import com.sdlifes.sdlifes.view.DragGridView;
import com.sdlifes.sdlifes.view.MyGridView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.NoTitleActivity;
import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.http.HttpInterface;
import www.xcd.com.mylibrary.utils.ShareHelper;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class ChannelActivity extends NoTitleActivity implements
        AdapterView.OnItemClickListener, HttpInterface
        , View.OnClickListener {

    private MyGridView mOtherGv;
    private DragGridView mUserGv;
    //    private List<String> mUserList = new ArrayList<>();
//    private List<String> mOtherList = new ArrayList<>();
    private OtherAdapter mOtherAdapter;
    private DragAdapter mUserAdapter;
    private TextView tv_channel_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        initView();
        initData();
    }

    private void initData() {
        String userId = ShareHelper.getUserId();
        OkHttpHelper.getRestfulHttp(
                this, 1000,
                UrlAddr.CHANNEL + (TextUtils.isEmpty(userId) ? "0" : userId), this);
    }

    public void initView() {
        mUserGv = (DragGridView) findViewById(R.id.userGridView);
        mOtherGv = (MyGridView) findViewById(R.id.otherGridView);
        mUserAdapter = new DragAdapter(this);
        mOtherAdapter = new OtherAdapter(this, false);
        mUserGv.setAdapter(mUserAdapter);
        mOtherGv.setAdapter(mOtherAdapter);
        mUserGv.setOnItemClickListener(this);
        mOtherGv.setOnItemClickListener(this);
        findViewById(R.id.iv_Finish).setOnClickListener(this);
        tv_channel_editor = findViewById(R.id.tv_channel_editor);
        tv_channel_editor.setOnClickListener(this);

    }

    /**
     * 获取点击的Item的对应View，
     * 因为点击的Item已经有了自己归属的父容器MyGridView，所有我们要是有一个ImageView来代替Item移动
     *
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     *
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     * 用于存放我们移动的View
     */
    private ViewGroup getMoveViewGroup() {
        //window中最顶层的view
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final ChannelModel moveChannel,
                          final GridView clickGridView, final boolean isUser) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // 判断点击的是DragGrid还是OtherGridView
                if (isUser) {
                    mOtherAdapter.setVisible(true);
                    mOtherAdapter.notifyDataSetChanged();
                    mUserAdapter.remove();
                } else {
                    mUserAdapter.setVisible(true);
                    mUserAdapter.notifyDataSetChanged();
                    mOtherAdapter.remove();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        switch (parent.getId()) {
            case R.id.userGridView: {
                //position为 0，1 的不可以进行任何操作
                if (position != 0 && position != 1&& mUserAdapter.isEditor()) { //改为-1前2个就可以移出的
                    final ImageView moveImageView = getView(view);
                    if (moveImageView != null) {
                        TextView newTextView = (TextView) view.findViewById(R.id.text_item);//
                        final int[] startLocation = new int[2];
                        newTextView.getLocationInWindow(startLocation);
                        final ChannelModel channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
//                        Toast.makeText(this, channel+"dsa", Toast.LENGTH_SHORT).show();
                        mOtherAdapter.setVisible(false);
                        //添加到最后一个
                        mOtherAdapter.addItem(channel);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    //获取终点的坐标
                                    mOtherGv.getChildAt(mOtherGv.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                    MoveAnim(moveImageView, startLocation, endLocation, channel, mUserGv, true);
                                    mUserAdapter.setRemove(position);
                                } catch (Exception localException) {
                                }
                            }
                        }, 50L);
                    }
                }else {
                    final ChannelModel channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                    int categoryid = channel.getCategoryid();
                    EventBusMsg messageEvent = new EventBusMsg(Constant.REFRESH_HOME);
                    messageEvent.setId(categoryid);
                    EventBus.getDefault().post(messageEvent);
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
            break;
            case R.id.otherGridView:
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final ChannelModel channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
                    mUserAdapter.setVisible(false);
//                    Toast.makeText(this, channel+"dsa", Toast.LENGTH_SHORT).show();
                   //判断已有集合中是否含有频道，避免重复添加
                    List<ChannelModel> channnelLst = mUserAdapter.getChannnelLst();
                    Log.e("TAG_添加频道",""+(channnelLst.contains(channel)));
                    if (!channnelLst.contains(channel)){
                        mUserAdapter.addItem(channel);
                    }
                    //添加到最后一个
                    updataChannleEditor();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                mUserGv.getChildAt(mUserGv.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation, channel, mOtherGv, false);
                                mOtherAdapter.setRemove(position);

                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        switch (requestCode) {
            case 1000:
                try {
                    JSONObject jsonObject = new JSONObject(returnData);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray mine = data.getJSONArray("mine");
                    //我的频道
                    List<ChannelModel> mineList = JSON.parseArray(mine.toString(), ChannelModel.class);
                    mUserAdapter.setData(mineList);
                    //推荐频道
                    JSONArray reco = data.getJSONArray("reco");
                    List<ChannelModel> recoList = JSON.parseArray(reco.toString(), ChannelModel.class);
                    mOtherAdapter.setData(recoList);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 1002:
//                EventBusMsg messageEvent = new EventBusMsg(Constant.REFRESH_HOME);
//                EventBus.getDefault().post(messageEvent);
                break;
        }
    }

    @Override
    public void onErrorResult(int requestCode, String errorExcep) {
        switch (requestCode) {
            case 1002:
                initData();
                ToastUtil.showToast(errorExcep);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_Finish:
                finish();
                break;
            case R.id.tv_channel_editor:
                boolean editor = mUserAdapter.isEditor();
                if (!editor) {
                    tv_channel_editor.setText("完成");
                    mUserAdapter.setEditor(true);
                } else {
                    tv_channel_editor.setText("编辑");
                    mUserAdapter.setEditor(false);
                    updataChannleEditor();
                }
                break;

        }
    }

    private void updataChannleEditor() {
        List<ChannelModel> channnelLst = mUserAdapter.getChannnelLst();
        com.alibaba.fastjson.JSONArray array = com.alibaba.fastjson.JSONArray.parseArray(JSON.toJSONString(channnelLst));
        Log.e("TAG_频道", "array=" + array.toString());
        String userId = ShareHelper.getUserId();
        String id = getIntent().getStringExtra("id");
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", userId);
        params.put("channelArr", array.toString());
        //增加浏览记录
        OkHttpHelper.postAsyncHttp(this, 1002,
                params, UrlAddr.CHANNEL_EDIT, this);
    }
}
