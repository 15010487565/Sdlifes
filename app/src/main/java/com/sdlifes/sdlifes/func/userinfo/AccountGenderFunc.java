package com.sdlifes.sdlifes.func.userinfo;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.me.UserinfoActivity;
import com.sdlifes.sdlifes.dialog.BottomUserInfoDialogFragment;

import java.util.ArrayList;
import java.util.List;

import www.xcd.com.mylibrary.func.BaseFunc;
import www.xcd.com.mylibrary.utils.ShareHelper;


public class AccountGenderFunc extends BaseFunc {

    private TextView textview;

    public AccountGenderFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.account_gender;
    }

    @Override
    public int getFuncIcon() {
        return 0;
    }

    @Override
    public int getFuncName() {
        return R.string.account_gender;
    }

    @Override
    public void onclick() {
        List<String> list = new ArrayList<>();
        list.add("男");
        list.add("女");
        BottomUserInfoDialogFragment bottomDialogFr = new BottomUserInfoDialogFragment();
        bottomDialogFr.setData(list);
        bottomDialogFr.show(((UserinfoActivity)getActivity()).getSupportFragmentManager(),"UserInfoNewActivity");
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
        int sex = ShareHelper.getSex();
        resetRightGender(sex);
        textview.setTextSize(12);
        textview.setGravity(Gravity.RIGHT);
        textview.setTextColor(getActivity().getResources().getColor(R.color.black_66));
        customView.addView(textview);
    }
    public void resetRightGender(int sex){
        if (sex  == 1){
            textview.setText("男");
        }else if (sex  == 2){
            textview.setText("女");
        }else {
            textview.setText("未知");
        }
    }
    public String getRightGender() {
        return textview.getText().toString();
    }
}
