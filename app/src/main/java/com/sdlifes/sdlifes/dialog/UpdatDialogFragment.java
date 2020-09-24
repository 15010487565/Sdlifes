package com.sdlifes.sdlifes.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.MainActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;


/**
 * Data:  2019/6/21
 * Auther: xcd
 * Description:
 */
public class UpdatDialogFragment extends DialogFragment implements View.OnClickListener{

    TextView tv_content;
    String state;
    String desc;

    /**
     *
     * @param state 更新状态 0 不更新 1普通更新（可以更新可以不更新）2强制更新（必须更新）
     * @param desc 更新内容
     */
    public void setData(String state,String desc) {
        this.state = state;
        this.desc = desc;
//        this.listener = listener;
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_fragment_update, container);
            TextView tv_close = view.findViewById(R.id.tv_close);
            tv_close.setOnClickListener(this);

            TextView tv_upload = view.findViewById(R.id.tv_upload);
            tv_upload.setOnClickListener(this);

            View line1 = view.findViewById(R.id.line1);
            if ("2".equals(state)){
                line1.setVisibility(View.GONE);
                tv_close.setVisibility(View.GONE);
            }else {
                line1.setVisibility(View.VISIBLE);
                tv_close.setVisibility(View.VISIBLE);
            }

            tv_content = view.findViewById(R.id.tv_content);
            tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());

            tv_content.setText(desc);

            getDialog().setCancelable(false);
            getDialog().setCanceledOnTouchOutside(false);
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                    return false;
                }
            });
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            return view;
        }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_close:

                dismiss();
                break;
            case R.id.tv_upload:
                ((MainActivity)getActivity()).updateTip();
                break;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowParams = window.getAttributes();
//        windowParams.dimAmount = 0.5f;
        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(windowParams);
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//            double v = dm.widthPixels * 0.75* 0.75;
//            tv_content.setMinimumHeight((int) v);
            window.setLayout((int) (dm.widthPixels * 0.75), -2);
        }
    }
    @Override
    public void show(FragmentManager manager, String tag) {
//        super.show(manager, tag);
        try {
            Class c=Class.forName("android.support.v4.app.DialogFragment");
            Constructor con = c.getConstructor();
            Object obj = con.newInstance();
            Field dismissed = c.getDeclaredField(" mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(obj,false);
            Field shownByMe = c.getDeclaredField("mShownByMe");
            shownByMe.setAccessible(true);
            shownByMe.set(obj,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

}
