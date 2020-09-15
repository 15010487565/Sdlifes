package com.sdlifes.sdlifes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.application.BaseApplication;
import com.sdlifes.sdlifes.dialog.AgreementDialogFragment;
import com.sdlifes.sdlifes.fragment.HomeFragment;
import com.sdlifes.sdlifes.fragment.MeFragment;
import com.sdlifes.sdlifes.fragment.PostFragment;

import java.util.ArrayList;
import java.util.List;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.base.fragment.BaseFragment;
import www.xcd.com.mylibrary.utils.ShareHelper;
import www.xcd.com.mylibrary.utils.ToastUtil;
import www.xcd.com.mylibrary.widget.SnsTabWidget;


/**
 * 主页面
 */
public class MainActivity extends SimpleTopbarActivity implements AgreementDialogFragment.CloseDialogFragment {

    /**
     *
     * fragment classes
     */
    public static Class<?> fragmentArray[] = {

            HomeFragment.class,
            PostFragment.class,
//            VideoNewFragment.class,
            MeFragment.class,
    };
    /**
     * tabs text
     */
    private static int[] MAIN_TAB_TEXT = new int[]{
            R.string.home,
//            R.string.video,
            R.string.post,
            R.string.me
    };
    /**
     * tabs image normal
     */
    private static int[] MAIN_TAB_IMAGE = new int[]{
            R.mipmap.home,
            R.mipmap.video,
            R.mipmap.me
    };
    /**
     * tabs image selected
     */
    private static int[] MAIN_TAB_IMAGEHL = new int[]{
            R.mipmap.home_hl,
            R.mipmap.video_hl,
            R.mipmap.me_hl
    };

    @Override
    protected Object getTopbarTitle() {
        return R.string.app_name;
    }

    @Override
    public boolean isTopbarVisibility() {
        return false;
    }

    /**
     * fragment列表
     */
    private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();

    private ViewPager viewPager;

    private SnsTabWidget tabWidget;

    /**
     * 第一次返回按钮时间
     */
    private long firstTime;
    private int currentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化fragments
        initFragments();
        // 初始化Tab
        initTabWidget();
        //实例化红点
//        resetRedPoint(0, 0);
//        resetRedPoint(1, 0);
//        resetRedPoint(2, 0);
        clickFragmentBtn(currentItem);



    }

    public void initAgreementDialog() {
        AgreementDialogFragment agreement = new AgreementDialogFragment();
        agreement.setCloseDialogFragment(this);

        agreement.show(getSupportFragmentManager(), "lose");
    }

    private void initView() {
        tabWidget = findViewById(R.id.main_tabwidget);
        // 为布局添加fragment,开启事物
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tran = fm.beginTransaction();

        //R.id.relative为布局
        tran.add(R.id.frame_content, fragmentList.get(0), "home").show(fragmentList.get(0))
                .add(R.id.frame_content, fragmentList.get(1), "contact").hide(fragmentList.get(1))
                .add(R.id.frame_content, fragmentList.get(2), "find").hide(fragmentList.get(2));
        tran.commit();

    }

    /**
     * 初始化fragments
     */
    protected void initFragments() {
        // 初始化fragments
        for (int i = 0; i < fragmentArray.length; i++) {
            BaseFragment fragment = null;
            try {
                fragment = (BaseFragment) fragmentArray[i].newInstance();
                fragment.setActivity(this);
            } catch (Exception e) {
            }
            fragmentList.add(fragment);
        }
        initView();
    }

    /**
     * 初始化Tab
     */
    protected void initTabWidget() {
        // LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < fragmentArray.length; i++) {
            // 实例化tabitem
            View view = inflater.inflate(R.layout.view_main_tabitem, null);
            // 为每一个Tab按钮设置图标、文字和内容
            setTextViewStyle(view, i, (i == 0));
            tabWidget.addView(view);
        }
        // 选中第一个
        tabWidget.setCurrentTab(0);
        // 添加监听
        tabWidget.setTabSelectionListener(new MainTabSelectionListener());
        findViewById(R.id.post).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.post:
                String userId = ShareHelper.getUserId();
                if (TextUtils.isEmpty(userId)){
                    startActivity(new Intent(this,LoginActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(this,PostActivity.class));

                }

                break;
        }
    }

    /**
     * 重设TextView的样式
     *
     * @param index
     * @param isCur
     */
    protected void setTextViewStyle(View view, int index, boolean isCur) {
        // TextView
        TextView textView = (TextView) view.findViewById(R.id.main_tabitem_text);
        // 设置文字
        textView.setText(MAIN_TAB_TEXT[index]);

        // TextView
        TextView textViewHl = (TextView) view.findViewById(R.id.main_tabitem_texthl);
        // 设置文字
        textViewHl.setText(MAIN_TAB_TEXT[index]);

        // ImageView
        ImageView imageView = (ImageView) view.findViewById(R.id.main_tabitem_icon);
        // 非高亮图标
        imageView.setImageResource(MAIN_TAB_IMAGE[index]);

        // ImageView
        ImageView imageViewHl = (ImageView) view.findViewById(R.id.main_tabitem_iconhl);
        // 高亮图标
        imageViewHl.setImageResource(MAIN_TAB_IMAGEHL[index]);

        resetTextViewStyle(view, index, isCur);
    }

    /**
     * 重设TextView的样式
     *
     * @param index
     * @param isCur
     */
    protected void resetTextViewStyle(View view, int index, boolean isCur) {
        // 选中页签
        if (isCur) {
            resetTextViewAlpha(view, 1);
        } else {// 未选中页签
            resetTextViewAlpha(view, 0);
        }
    }

    /**
     * 重设TextView的Alpha值
     *
     * @param view
     * @param f
     */
    private void resetTextViewAlpha(View view, float f) {
        if (view == null) {
            return;
        }
        // ViewHl  通过设置透明度实现切换
        View itemViewHl = (View) view.findViewById(R.id.main_tabitem_viewhl);
        itemViewHl.setAlpha(f);
        //通过布局隐藏实现切换
        View itemViewH = (View) view.findViewById(R.id.main_tabitem_view);
        if (f == 1) {
            itemViewH.setVisibility(View.GONE);
        }
        if (f == 0) {
            itemViewH.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 重设页面的Alpha
     *
     * @param index
     * @param f
     */
    private void resetFragmentAlpha(int index, float f) {
        if (index < 0 || index >= fragmentList.size()) {
            return;
        }
        View view = fragmentList.get(index).getView();
        if (view != null) {
            view.setAlpha(f);
        }
    }

    private void clickFragmentBtn(int tabIndex) {
        // 得到Fragment事务管理器
        FragmentTransaction fragmentTransaction = this
                .getSupportFragmentManager().beginTransaction();

        for (int i = 0; i < fragmentList.size(); i++) {
//            if (tabIndex == 1){
//                startActivity(new Intent(this,PostActivity.class));
//            }else {
                if (i == tabIndex) {
                    fragmentTransaction.show(fragmentList.get(i));
                    resetTextViewAlpha(tabWidget.getChildAt(i), 1);

                } else {
                    fragmentTransaction.hide(fragmentList.get(i));
                    resetTextViewAlpha(tabWidget.getChildAt(i), 0);
                }
//            }
        }
        Log.e("TAG_切换", "tabIndex=" + tabIndex);
        if (tabIndex != 2) {
            setTransparent(this, ContextCompat.getColor(this, R.color.white));
//            topViewGradient.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        } else {
            setTransparent(this, ContextCompat.getColor(this, R.color.black_26));
//            topViewGradient.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_gradient));
        }

        fragmentTransaction.commit();
    }

    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstTime < 3000) {
            BaseApplication.getInstance().exitApp();
        } else {
            firstTime = System.currentTimeMillis();
            // 再按一次返回桌面

            ToastUtil.showToast(getString(R.string.main_press_again_back));
        }
    }

    /**
     * tab change监听
     *
     * @author litfb
     * @version 1.0
     * @date 2014年9月23日
     */
    private class MainTabSelectionListener implements SnsTabWidget.ITabSelectionListener {

        @Override
        public void onTabSelectionChanged(int tabIndex) {

            // 重设当前页
            clickFragmentBtn(tabIndex);
        }
    }

    /**
     * 重设红点
     *
     * @param index
     * @param number
     */
//    private void resetRedPoint(int index, int number) {
//        View view = tabWidget.getChildAt(index);
//        // red number
//        BadgeView textRedpoint = (BadgeView) view.findViewById(R.id.main_tabitem_redpoint);
//        if (number > 0) {
//            if (String.valueOf(number).length() > 2) {
//                textRedpoint.setText("...");
//            } else {
//                textRedpoint.setText(String.valueOf(number));
//            }
//            //隐藏红点
//            textRedpoint.setVisibility(View.VISIBLE);
////			textRedpoint.setVisibility(View.GONE);
//        } else {
//            textRedpoint.setText("");
//            textRedpoint.setVisibility(View.GONE);
//        }
//    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.showToast(getString(R.string.toast_exit));
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;


        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void close() {
        ShareHelper.setAgreementFlag();
    }

    // 保存MyTouchListener接口的列表
//    private ArrayList<MainActivityTouchListener> activityTouchListeners = new ArrayList<MainActivityTouchListener>();
//
//    public interface MainActivityTouchListener {
//        public void onTouchEvent(MotionEvent event);
//    }
//
//    /**
//     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
//     *
//     * @param listener
//     */
//    public void registerTouchListener(MainActivityTouchListener listener) {
//        activityTouchListeners.add(listener);
//    }
//
//    /**
//     * 提供给Fragment通过getActivity()方法来注销自己的触摸事件的方法
//     *
//     * @param listener
//     */
//    public void unRegisterTouchListener(MainActivityTouchListener listener) {
//        activityTouchListeners.remove(listener);
//    }
//
//    /**
//     * 分发触摸事件给所有注册了MainActivityTouchListener的接口
//     */
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        for (MainActivityTouchListener listener : activityTouchListeners) {
//            listener.onTouchEvent(ev);
//        }
//        return super.dispatchTouchEvent(ev);
//    }
}
