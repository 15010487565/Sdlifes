package com.sdlifes.sdlifes.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * 怀孕中
 */
public class BabyPTFragment extends Fragment implements HttpInterface, View.OnClickListener {

    private TextView tv_pt_date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boby_pt, container, false);

        view.findViewById(R.id.ll_pt_date).setOnClickListener(this);
        tv_pt_date = view.findViewById(R.id.tv_pt_date);
        // 预产期
        String p_etime = ShareHelper.getP_etime();
        if (!TextUtils.isEmpty(p_etime)){
            tv_pt_date.setText(p_etime);
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
                // 预产期
                String p_etime = data.getP_etime();
                ShareHelper.savePrfparams("p_etime",p_etime);

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
            case R.id.ll_pt_date:
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
                        tv_pt_date.setText(format);
                    }
                });
                if (dialog != null && !dialog.isShowing()){
                    dialog.show();
                }
                break;
            case R.id.tv_boby_sava:
                String p_etime = tv_pt_date.getText().toString();
                if (TextUtils.isEmpty(p_etime)){
                    ToastUtil.showToast("预产期不能为空！");
                    return;
                }

                String userId = ShareHelper.getUserId();
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userId);
                params.put("state", "2");
                params.put("p_etime", p_etime);
                OkHttpHelper.postAsyncHttp(getActivity(), 1000, params, UrlAddr.EDITSTATE, this);
                break;

        }
    }
}
