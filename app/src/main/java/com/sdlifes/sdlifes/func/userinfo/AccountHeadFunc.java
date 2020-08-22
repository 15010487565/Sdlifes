package com.sdlifes.sdlifes.func.userinfo;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.me.UserinfoActivity;
import com.sdlifes.sdlifes.util.ImageUtils;
import com.sdlifes.sdlifes.view.CircleImageView;

import www.xcd.com.mylibrary.func.BaseFunc;
import www.xcd.com.mylibrary.utils.ShareHelper;


/**
 * 头像
 *
 * @author litfb
 * @version 1.0
 * @date 2014年10月16日
 */
public class AccountHeadFunc extends BaseFunc {

    CircleImageView imageView;

    public AccountHeadFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.account_head;
    }

    @Override
    public int getFuncIcon() {
        return 0;
    }

    @Override
    public int getFuncName() {
        return R.string.account_head;
    }

    @Override
    public void onclick() {
		((UserinfoActivity)getActivity()).getChoiceDialog().show();
//        PermissionsChecker mChecker = new PermissionsChecker(getActivity());
//        if (mChecker.lacksPermissions(AUTHORIMAGE)) {
//            // 请求权限
//            PermissionsActivity.startActivityForResult(getActivity(), CAMERA_REQUESTCODE, AUTHORIMAGE);
//        } else {
//            // 全部权限都已获取
//            EasyPhotos.createCamera(getActivity())
//                    .setFileProviderAuthority("com.sdlifes.sdlifes.fileprovider")
//                    .start(101);
//        }
//        EasyPhotos.createAlbum(getActivity(), false, GlideEngine.getInstance())
//                .start(101);

    }

    @Override
    public View initFuncView(boolean isSeparator, Object... params) {
        View funcView = super.initFuncView(isSeparator, params);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.topMargin = (int) getActivity().getResources().getDimension(R.dimen.me_vertical_margin);
        funcView.setLayoutParams(params2);
        return funcView;
    }

    @Override
    protected void initCustomView(LinearLayout customView) {
        super.initCustomView(customView);
        //创建保存布局参数的对象
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                        , LinearLayout.LayoutParams.WRAP_CONTENT);
        imageView = new CircleImageView(getActivity());
        int maxhw = (int) getActivity().getResources().getDimension(R.dimen.margin_35);
        params.height = maxhw;
        params.width = maxhw;
        imageView.setLayoutParams(params);//设置布局参数
//		imageView.setScaleType(ImageView.ScaleType.FIT_XY);//设置图片自动缩放
        customView.setGravity(Gravity.RIGHT);
        customView.addView(imageView);
        String headimg = ShareHelper.getHeadImage();
        refreshHead(headimg);
    }

    public void refreshHead(String headimg) {
        ImageUtils.setImage(imageView, headimg, 3000, R.mipmap.head_portrait);
    }
}
