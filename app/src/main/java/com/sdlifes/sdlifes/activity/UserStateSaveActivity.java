package com.sdlifes.sdlifes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.me.UserinfoActivity;
import com.sdlifes.sdlifes.fragment.BabyFragment;
import com.sdlifes.sdlifes.fragment.BabyPTFragment;
import com.sdlifes.sdlifes.fragment.BabyReadyFragment;

import java.util.ArrayList;
import java.util.List;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.widget.SnsTabWidget;

public class UserStateSaveActivity extends SimpleTopbarActivity {


    public static Class<?> fragmentArray[] = {
            BabyReadyFragment.class,
            BabyPTFragment.class,
            BabyFragment.class,
    };

    private static int[] MAIN_TAB_TEXT = new int[]{
            R.string.user_state_ready,
            R.string.user_state_pt,
            R.string.user_state_baby
    };

    private static int[] MAIN_TAB_IMAGE = new int[]{
            R.mipmap.addbaby_btn_ready2,
            R.mipmap.addbaby_btn_pt2,
            R.mipmap.addbaby_btn_baby2
    };

    private static int[] MAIN_TAB_IMAGEHL = new int[]{
            R.mipmap.addbaby_btn_ready,
            R.mipmap.addbaby_btn_pt,
            R.mipmap.addbaby_btn_baby
    };

    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    private ViewPager viewPager;

    private SnsTabWidget tabWidget;

    @Override
    protected Object getTopbarTitle() {
        return "选择所处阶段";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_state_save);

        // 初始化fragments
        initFragments();
        // 初始化Tab
        initTabWidget();
        int currentItem = getIntent().getIntExtra("state",0);
        clickFragmentBtn(currentItem);

    }

    private void initView() {
        tabWidget = findViewById(R.id.main_tabwidget);
        // 为布局添加fragment,开启事物
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tran = fm.beginTransaction();

        //R.id.relative为布局
        tran.add(R.id.frame_content, fragmentList.get(0), "pt").show(fragmentList.get(0))
                .add(R.id.frame_content, fragmentList.get(1), "read").hide(fragmentList.get(1))
                .add(R.id.frame_content, fragmentList.get(2), "boby").hide(fragmentList.get(2));
        tran.commit();

    }

    /**
     * 初始化fragments
     */
    protected void initFragments() {
        // 初始化fragments
        for (int i = 0; i < fragmentArray.length; i++) {
            Fragment fragment = null;
            try {
                fragment = (Fragment) fragmentArray[i].newInstance();
//                fragment.setActivity(this);
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
            View view = inflater.inflate(R.layout.view_state_tabitem, null);
            // 为每一个Tab按钮设置图标、文字和内容
            setTextViewStyle(view, i, (i == 0));
            tabWidget.addView(view);
        }
        // 选中第一个
        tabWidget.setCurrentTab(0);
        // 添加监听
        tabWidget.setTabSelectionListener(new MainTabSelectionListener());

    }

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

    private void clickFragmentBtn(int tabIndex) {
        // 得到Fragment事务管理器
        FragmentTransaction fragmentTransaction = this
                .getSupportFragmentManager().beginTransaction();

        for (int i = 0; i < fragmentList.size(); i++) {

            if (i == tabIndex) {
                fragmentTransaction.show(fragmentList.get(i));
                resetTextViewAlpha(tabWidget.getChildAt(i), 1);

            } else {
                fragmentTransaction.hide(fragmentList.get(i));
                resetTextViewAlpha(tabWidget.getChildAt(i), 0);
            }
        }


        fragmentTransaction.commit();
    }

    public void saveStartActivity(){
        //RegisterActivity;MeFragment
        String source = getIntent().getStringExtra("source");
        if ("RegisterActivity".equals(source)){
           startActivity(new Intent(this, MainActivity.class));

        }else {
            startActivity(new Intent(this, UserinfoActivity.class));
//            EventBusMsg messageEvent = new EventBusMsg(Constant.CLOSE_ACTION);
//            EventBus.getDefault().post(messageEvent);
        }
        finish();
    }

    private class MainTabSelectionListener implements SnsTabWidget.ITabSelectionListener {

        @Override
        public void onTabSelectionChanged(int tabIndex) {

            // 重设当前页
            clickFragmentBtn(tabIndex);
        }
    }
}
