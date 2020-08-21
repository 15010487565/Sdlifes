package com.sdlifes.sdlifes.func.userinfo;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.me.UserinfoActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import www.xcd.com.mylibrary.func.BaseFunc;
import www.xcd.com.mylibrary.utils.ShareHelper;

/**
 * Created by gs on 2020/8/11.
 * 生日
 */
public class AccountBirthdayFunc extends BaseFunc {

    public AccountBirthdayFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.account_birthday;
    }

    @Override
    public int getFuncIcon() {
        return 0;
    }

    @Override
    public int getFuncName() {
        return R.string.account_birthday;
    }

    @Override
    public void onclick() {
        DatePickDialog dialog = new DatePickDialog(getActivity());
        //设置上下年分限制
        dialog.setYearLimt(100);
        //设置标题
//            dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(DateType.TYPE_YMD);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String format = simpleDateFormat.format(date);
                ((UserinfoActivity)getActivity()).updataBirthday(format);
            }
        });
        dialog.show();
    }

    @Override
    public View initFuncView(boolean isSeparator, Object... params) {
        View funcView = super.initFuncView(true, params);
        return funcView;
    }
    TextView textview;
    @Override
    protected void initCustomView(LinearLayout customView) {
        super.initCustomView(customView);
        //创建保存布局参数的对象
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        , LinearLayout.LayoutParams.MATCH_PARENT);
        textview = new TextView(getActivity());
        textview.setLayoutParams(params);//设置布局参数
        String birthday = ShareHelper.getbirthday();
        resetRightName(birthday);
        textview.setTextSize(12);
        textview.setGravity(Gravity.RIGHT);
        textview.setTextColor(getActivity().getResources().getColor(R.color.blue));
        customView.addView(textview);
    }
    public void resetRightName(String name){
        textview.setText(name);
    }
}