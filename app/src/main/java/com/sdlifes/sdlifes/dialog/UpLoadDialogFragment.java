package com.sdlifes.sdlifes.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.util.HelpOpenFileUtils;
import com.sdlifes.sdlifes.util.ImageUtils;

/**
 * Data:  2020/4/27
 * Auther: xcd
 * Description:
 */
public class UpLoadDialogFragment extends DialogFragment implements View.OnClickListener{

    private View frView;
    private Window window;
    String imgUrl;

    public void setData(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 去掉默认的标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        frView = inflater.inflate(R.layout.dialog_fr_upload, null);

        return frView;
    }

    @Override
    public void onStart() {
        super.onStart();
        window = getDialog().getWindow();
        frView.findViewById(R.id.tv_cancel).setOnClickListener(this);
        frView.findViewById(R.id.tv_sava).setOnClickListener(this);
        WindowManager.LayoutParams params = window.getAttributes();
//        params.gravity = Gravity.BOTTOM;
        // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
        params.height = getResources().getDisplayMetrics().heightPixels;
        window.setAttributes(params);
        final Dialog dialog = getDialog();

        if (dialog != null) {
//            DisplayMetrics dm = new DisplayMetrics();
//            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

            window.setLayout((int) (ImageUtils.getWidthPixel()*0.75), -2);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_sava:
                HelpOpenFileUtils.downloadFile(getActivity(),imgUrl);
                dismiss();
            break;
        }
    }
}
