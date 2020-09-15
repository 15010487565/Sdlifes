package com.sdlifes.sdlifes.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.ActivityUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * Data:  2019/6/21
 * Auther: xcd
 * Description:
 */
public class AgreementDialogFragment extends DialogFragment implements View.OnClickListener {

    private TextView tvLoginAgreement;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AgreementDialog);

        View view = inflater.inflate(R.layout.dialog_fragment_agreement, container);
        tvLoginAgreement = view.findViewById(R.id.tv_LoginAgreement);
        String str = "我已阅读并同意《服务条款》和《隐私政策》";
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder(str);
        // 单独设置点击事件
        ClickableSpan clickableSpanOne = new ClickableSpan() {
            @Override
            public void onClick(View view) {//服务条款
                ActivityUtils.startWebViewActivity(getContext(),UrlAddr.CLAUSE,"",true);

            }
            @Override
            public void updateDrawState(TextPaint paint) {
                paint.setColor(Color.parseColor("#E13122"));
                // 设置下划线 true显示、false不显示
                paint.setUnderlineText(false);
            }
        };
        spannableBuilder.setSpan(clickableSpanOne, 7, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 在设置点击事件、同时设置字体颜色
        ClickableSpan clickableSpanTwo = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                ActivityUtils.startWebViewActivity(getContext(), UrlAddr.PRIVACY,"",true);

            }

            @Override
            public void updateDrawState(TextPaint paint) {
                paint.setColor(Color.parseColor("#E13122"));
                // 设置下划线 true显示、false不显示
                paint.setUnderlineText(false);
            }
        };
        spannableBuilder.setSpan(clickableSpanTwo, 14, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        tvLoginAgreement.setText(spannableBuilder);
        tvLoginAgreement.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
        tvLoginAgreement.setHighlightColor(Color.TRANSPARENT);

        TextView tv_ColseAgreement = view.findViewById(R.id.tv_ColseAgreement);
        tv_ColseAgreement.setOnClickListener(this);
        TextView tvExitAgreement = view.findViewById(R.id.tv_ExitAgreement);
        tvExitAgreement.setOnClickListener(this);


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
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;
        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(windowParams);
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            window.setLayout((int) (dm.widthPixels * 0.75), (int) (dm.heightPixels * 0.63));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ColseAgreement:
                dismiss();
                if (closeDialogFragment !=null)
                closeDialogFragment.close();
                break;
            case R.id.tv_ExitAgreement:
                dismiss();
                getActivity().finish();
                break;
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

    public CloseDialogFragment closeDialogFragment;
    public void setCloseDialogFragment(CloseDialogFragment closeDialogFragment){
        this.closeDialogFragment = closeDialogFragment;
    }
    public interface CloseDialogFragment {
        void close();
    }
}
