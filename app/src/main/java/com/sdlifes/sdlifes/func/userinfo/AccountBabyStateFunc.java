package com.sdlifes.sdlifes.func.userinfo;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.UserStateActivity;

import www.xcd.com.mylibrary.func.BaseFunc;
import www.xcd.com.mylibrary.utils.ShareHelper;


public class AccountBabyStateFunc extends BaseFunc {

    private TextView textview;

    public AccountBabyStateFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.account_baby_state;
    }

    @Override
    public int getFuncIcon() {
        return 0;
    }

    @Override
    public int getFuncName() {
        return R.string.account_baby_state;
    }

    @Override
    public void onclick() {
        Intent intent = new Intent(getActivity(), UserStateActivity.class);
        intent.putExtra("source","MeFragment");
        getActivity().startActivity(intent);
        //用户状态 0未设置 1备孕中 2怀孕中 3宝宝出生
//                    int state = data.getState();
//                    if (state == 0){
//
//                    }
    }

    @Override
    public View initFuncView(boolean isSeparator, Object... params) {
        View funcView = super.initFuncView(isSeparator, params);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.topMargin = (int) getActivity().getResources().getDimension(R.dimen.me_vertical_margin);
        funcView.setLayoutParams(params2);
        funcView.findViewById(R.id.func_separator).setVisibility(View.GONE);
        return funcView;
    }

    @Override
    protected void initCustomView(LinearLayout customView) {
        super.initCustomView(customView);
        //创建保存布局参数的对象
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        , LinearLayout.LayoutParams.MATCH_PARENT);
        textview = new TextView(getActivity());
        textview.setLayoutParams(params);//设置布局参数
        int state = ShareHelper.getState();
        resetRightState(state);
        textview.setTextSize(12);
        textview.setGravity(Gravity.RIGHT);
        customView.addView(textview);
    }
    public void resetRightState(int state){
        //用户状态 0未设置 1备孕中 2怀孕中 3宝宝出生
        if (state  == 1){
            textview.setText("备孕中");
            textview.setTextColor(ContextCompat.getColor(getActivity(),R.color.blue));
        }else if (state  == 2){
            textview.setText("怀孕中");
            textview.setTextColor(ContextCompat.getColor(getActivity(),R.color.blue));
        }else if (state == 3){
            textview.setText("宝宝出生");
            textview.setTextColor(ContextCompat.getColor(getActivity(),R.color.blue));
        }else {
            textview.setText("未设置");
            textview.setTextColor(ContextCompat.getColor(getActivity(),R.color.black_66));
        }
    }
    public String getRightGender() {
        return textview.getText().toString();
    }
}
