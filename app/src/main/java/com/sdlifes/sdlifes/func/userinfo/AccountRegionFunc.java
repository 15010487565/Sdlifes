package com.sdlifes.sdlifes.func.userinfo;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lljjcoder.citypickerview.widget.CityPicker;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.me.UserinfoActivity;

import www.xcd.com.mylibrary.func.BaseFunc;
import www.xcd.com.mylibrary.utils.ShareHelper;

/**
 * 2020年8月11日13:43:48
 * 地区
 */
public class AccountRegionFunc extends BaseFunc {

    TextView textview;
    public AccountRegionFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.account_region;
    }

    @Override
    public int getFuncIcon() {
        return 0;
    }

    @Override
    public int getFuncName() {
        return R.string.account_region;
    }

    @Override
    public void onclick() {
        CityPicker mCP = new CityPicker.Builder(getActivity())
                .textSize(20)
                //地址选择
                .title("地区")

//                .backgroundPop(ContextCompat.getColor(getActivity(),R.color.black_f5))
                //文字的颜色
                .titleBackgroundColor("#E0E0E0")
                .titleTextColor("#000000")
                .backgroundPop(ContextCompat.getColor(getActivity(),R.color.transparent))
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("北京市")
                .city("北京市")
                .district("xx区")
                //滑轮文字的颜色
                .textColor(Color.parseColor("#000000"))
                //省滑轮是否循环显示
                .provinceCyclic(true)
                //市滑轮是否循环显示
                .cityCyclic(false)
                //地区（县）滑轮是否循环显示
                .districtCyclic(false)
                //滑轮显示的item个数
                .visibleItemsCount(7)
                //滑轮item间距
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        mCP.show();
        mCP.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... strings) {
                //省
                String province = strings[0];
                //市
                String city = strings[1];
                //区。县。（两级联动，必须返回空）
                String district = strings[2];

                //邮证编码
                String code = strings[3];

                ((UserinfoActivity)getActivity()).updataRegion(province+"-"+city+"-"+district);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public View initFuncView(boolean isSeparator, Object... params) {
        View funcView = super.initFuncView(true, params);
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
        params.leftMargin = (int) getActivity().getResources().getDimension(R.dimen.margin_20);
        textview.setLayoutParams(params);//设置布局参数
        textview.setTextSize(12);
        textview.setGravity(Gravity.RIGHT);
        textview.setTextColor(getActivity().getResources().getColor(R.color.blue));
        textview.setLines(1);
        customView.addView(textview);
        String region = ShareHelper.getRegion();
        refreshRigion(region);
    }
    public void refreshRigion(String refresh){
        textview.setText(refresh);
    }
    public String getTextRigion(){
        return textview.getText().toString().trim();
    }
}
