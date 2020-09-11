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
import com.sdlifes.sdlifes.dialog.BabyDialogFragment;
import com.sdlifes.sdlifes.model.UserinfoModel;
import com.sdlifes.sdlifes.network.UrlAddr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.http.HttpInterface;
import www.xcd.com.mylibrary.utils.ShareHelper;
import www.xcd.com.mylibrary.utils.ToastUtil;


public class BabyFragment extends Fragment implements HttpInterface, View.OnClickListener,BabyDialogFragment.SexClickListener {

    private EditText ed_baby_name;
    private TextView tv_baby_birthday, tv_baby_sex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boby, container, false);
        ed_baby_name = view.findViewById(R.id.ed_baby_name);

        view.findViewById(R.id.ll_baby_birthday).setOnClickListener(this);
        tv_baby_birthday = view.findViewById(R.id.tv_baby_birthday);

        view.findViewById(R.id.ll_baby_sex).setOnClickListener(this);
        tv_baby_sex = view.findViewById(R.id.tv_baby_sex);

        // 宝宝昵称
        String b_name = ShareHelper.getB_name();
        if (!TextUtils.isEmpty(b_name)){
            ed_baby_name.setText(b_name);
        }
        // 宝宝生日
        String b_birthday = ShareHelper.getB_birthday();
        if (!TextUtils.isEmpty(b_birthday)){
            tv_baby_birthday.setText(b_birthday);
        }
        // 宝宝性别 1男 2女
        int b_sex = ShareHelper.getB_sex();
        if (b_sex == 1){
            tv_baby_sex.setText("男");
        }else if (b_sex == 2){
            tv_baby_sex.setText("女");
        }else {
            tv_baby_sex.setText("");
        }

        view.findViewById(R.id.tv_boby_sava).setOnClickListener(this);
        return view;
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        switch (requestCode) {
            case 1000:
                UserinfoModel userinfoModel = JSON.parseObject(returnData, UserinfoModel.class);
                UserinfoModel.DataBean data = userinfoModel.getData();
                //用户状态 0未设置 1备孕中 2怀孕中 3宝宝出生
                int state = data.getState();
                ShareHelper.savePrfparams("state",state);
                // 宝宝昵称
                String b_name = data.getB_name();
                ShareHelper.savePrfparams("b_name",b_name);
                // 宝宝生日
                String b_birthday = data.getB_birthday();
                ShareHelper.savePrfparams("b_birthday",b_birthday);
                // 宝宝性别 1男 2女
                int b_sex = data.getB_sex();
                ShareHelper.savePrfparams("b_sex",b_sex);
                ((UserStateSaveActivity)getActivity()).saveStartActivity();

                break;
        }
    }

    @Override
    public void onErrorResult(int requestCode, String returnMsg) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_baby_birthday:
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
                        tv_baby_birthday.setText(format);
                    }
                });
                if (dialog != null && !dialog.isShowing()){
                    dialog.show();
                }
                break;
            case R.id.ll_baby_sex: {
                List<String> list = new ArrayList<>();
                list.add("男");
                list.add("女");
                BabyDialogFragment bottomDialogFr = new BabyDialogFragment();
                bottomDialogFr.setData(list,this);
                bottomDialogFr.show((getActivity()).getSupportFragmentManager(),"BabyFragment");
            }
            break;

            case R.id.tv_boby_sava:
                String b_name = ed_baby_name.getText().toString();
                if (TextUtils.isEmpty(b_name)) {
                    ToastUtil.showToast("宝宝昵称不能为空！");
                    return;
                }
                String b_birthday = tv_baby_birthday.getText().toString();
                if (TextUtils.isEmpty(b_birthday)) {
                    ToastUtil.showToast("宝宝生日不能为空！");
                    return;
                }
                String b_sex = tv_baby_sex.getText().toString();
                if (TextUtils.isEmpty(b_sex)) {
                    ToastUtil.showToast("宝宝性别不能为空！");
                    return;
                }
                int sex = 0;
                if ("男".equals(b_sex)){
                    sex = 1;
                }else if ("女".equals(b_sex)){
                    sex = 2;
                }else {
                    sex = 0;
                }
                String userId = ShareHelper.getUserId();
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userId);
                params.put("state", "3");
                params.put("b_name", b_name);
                params.put("b_birthday", b_birthday);
                params.put("b_sex", String.valueOf(sex));
                OkHttpHelper.postAsyncHttp(getActivity(), 1000, params, UrlAddr.EDITSTATE, this);
                break;

        }
    }

    @Override
    public void OnItemClick(int sex) {
        if (sex  == 1){
            tv_baby_sex.setText("男");
        }else if (sex  == 2){
            tv_baby_sex.setText("女");
        }else {
            tv_baby_sex.setText("未知");
        }
    }
}
