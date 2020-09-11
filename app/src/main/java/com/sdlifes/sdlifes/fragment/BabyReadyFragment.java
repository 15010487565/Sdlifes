package com.sdlifes.sdlifes.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.UserStateSaveActivity;
import com.sdlifes.sdlifes.model.UserinfoModel;
import com.sdlifes.sdlifes.network.UrlAddr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.http.HttpInterface;
import www.xcd.com.mylibrary.utils.ShareHelper;
import www.xcd.com.mylibrary.utils.ToastUtil;

/**
 * 备孕中
 */
public class BabyReadyFragment extends Fragment implements HttpInterface, View.OnClickListener {

    private TextView tv_ready_lasttime;
    private EditText et_ready_days,et_ready_interval;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boby_ready, container, false);

        view.findViewById(R.id.ll_ready_date).setOnClickListener(this);
        tv_ready_lasttime = view.findViewById(R.id.tv_ready_lasttime);
        et_ready_days = view.findViewById(R.id.et_ready_days);
        et_ready_interval = view.findViewById(R.id.et_ready_interval);

        //上次月经开始时间
        String m_lasttime = ShareHelper.getM_lasttime();
       if (!TextUtils.isEmpty(m_lasttime)){
           tv_ready_lasttime.setText(m_lasttime);
       }
        //月经一般持续天数
        int m_days = ShareHelper.getM_day();
        if (m_days > 0){
            et_ready_days.setText(String.valueOf(m_days));
        }
        //月经周期天数
        int m_interval = ShareHelper.getM_interval();
        if (m_interval > 0){
            et_ready_interval.setText(String.valueOf(m_interval));
        }

        view.findViewById(R.id.tv_boby_sava).setOnClickListener(this);

        return view;
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        switch (requestCode){
            case 1000:
                UserinfoModel userinfoModel = JSON.parseObject(returnData, UserinfoModel.class);
                UserinfoModel.DataBean data = userinfoModel.getData();
                //用户状态 0未设置 1备孕中 2怀孕中 3宝宝出生
                int state = data.getState();
                ShareHelper.savePrfparams("state",state);
                //上次月经开始时间
                String m_lasttime = data.getM_lasttime();
                ShareHelper.savePrfparams("m_lasttime",m_lasttime);
                //月经一般持续天数
                int m_days = data.getM_days();
                ShareHelper.savePrfparams("m_days",m_days);
                //月经周期天数
                int m_interval = data.getM_interval();
                ShareHelper.savePrfparams("m_interval",m_interval);

                ((UserStateSaveActivity)getActivity()).saveStartActivity();
                break;
        }
    }

    @Override
    public void onErrorResult(int requestCode, String returnMsg) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_ready_date:
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
                        tv_ready_lasttime.setText(format);
                    }
                });
                if (dialog != null && !dialog.isShowing()){
                    dialog.show();
                }
                break;
            case R.id.tv_boby_sava:
                String m_lasttime = tv_ready_lasttime.getText().toString();
                if (TextUtils.isEmpty(m_lasttime)){
                    ToastUtil.showToast("月经开始时间不能为空！");
                    return;
                }
                String m_days = et_ready_days.getText().toString();
                if (TextUtils.isEmpty(m_days)){
                    ToastUtil.showToast("月经持续天数不能为空！");
                    return;
                }
                String m_interval = et_ready_interval.getText().toString();
                if (TextUtils.isEmpty(m_interval)){
                    ToastUtil.showToast("月经周期不能为空！");
                    return;
                }
                String userId = ShareHelper.getUserId();
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userId);
                params.put("state", "1");
                params.put("m_lasttime", m_lasttime);
                params.put("m_days", m_days);
                params.put("m_interval", m_interval);
                OkHttpHelper.postAsyncHttp(getActivity(), 1000, params, UrlAddr.EDITSTATE, this);
                break;

        }
    }
}
