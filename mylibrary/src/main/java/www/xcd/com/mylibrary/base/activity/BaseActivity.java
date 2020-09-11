package www.xcd.com.mylibrary.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import www.xcd.com.mylibrary.R;
import www.xcd.com.mylibrary.utils.AppManager;


/**
 * 薛传东
 * Created by xcd15 on 2017/5/3.
 */

public abstract class BaseActivity extends FragmentActivity implements View.OnFocusChangeListener{


    private boolean isActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        super.onCreate(savedInstanceState);
//        StatusNavUtils.setStatusBarColor(this,0x00000000);
		AppManager.getInstance().addActivity(this);
        setTransparent(this, ContextCompat.getColor(this, R.color.white));
    }

    /**
     * 设置状态栏全透明
     *
     * @param activity 需要设置的activity
     */
    public static void setTransparent(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(activity,color);
        setRootView(activity);
    }

    /**
     * 设置根布局参数
     */
    private static void setRootView(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
//                Log.e("TAG_工作日志","设置FitsSystemWindows");
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    private static void transparentStatusBar(Activity activity, @ColorInt int color) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);

        } else {
            //让contentView延伸到状态栏并且设置状态栏颜色透明
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Context context = activity;
        if (color == ContextCompat.getColor(context, R.color.white)) {
            //黑色字体
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            window.setBackgroundDrawableResource(R.drawable.shape_gradient);
        } else {
            //白色字体
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    /**
     * 获取当前页面需要的权限
     *
     * @return
     */
    protected String[] getPermissions() {
        return null;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isActive = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    public boolean dialogIsActivity() {
        return isActive;
    }

    @Override
    public void finish() {
        super.finish();
        AppManager.getInstance().removeActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        afterSetContentView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        afterSetContentView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        afterSetContentView();
    }



    protected void afterSetContentView() {

    }

    /**
     * 根据resId获得一个Bitmap
     *
     * @param resId
     * @return
     */
    public Bitmap getDrawableBitmap(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap();
        } else {
            return null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyDeatchView();
    }
    protected void onDestroyDeatchView() {
    }
    /**
     * 初始化踢下线弹出框
     */
//    public void initDialog() {
//        // 冲突踢下线
//        CustomDialog.Builder builder = new CustomDialog.Builder(BaseActivity.this);
//        builder.setTitle("帐号下线");
//        builder.setMessage("您的帐号已在其他移动端登录，已断开连接。");
//        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent in = new Intent("android.intent.action.LOGIN");
//                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(in);
//                AppManager.getInstance().finishAllActivity();
//            }
//        });
//        dialog = builder.create();
//        dialog.setCanceledOnTouchOutside(false);
//    }
    /**
     * 展示踢人dialog
     */
//    protected void showKickDialog(){
//        Log.e("TAG_踢下线","activityIsActivity="+dialogIsActivity()+";isActive="+isActive);
//        if (dialog != null && !dialog.isShowing()&&dialogIsActivity()) {
//            dialog.show();
//        }
//    }
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        EditText textView = (EditText) view;
        if (textView==null){
            Log.e("TAG_键盘","EditText==null");
            return;
        }
        if (textView.getHint()==null){
            Log.e("TAG_键盘","textView.getHint()==null");
            return;
        }
        String hint = textView.getHint().toString();
        Log.e("TAG_键盘","hasFocus="+hasFocus);
        if (hasFocus) {
            textView.setTag(hint);
            textView.setHint("");
        } else {
            hint = textView.getTag().toString();
            textView.setHint(hint);
        }
        if (!hasFocus) {
            InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }else {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if(hideInputMethod(this, v)) {
//                    return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    //判断当前点击屏幕的地方是否是软键盘
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    //隐藏软键盘的方法
    public static Boolean hideInputMethod(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }
    protected int getStatusBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
    protected int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public void startBaseActivity(Context context,Class<?> cla){
//        startWebViewActivity(Config.LOGINWEBVIEW);
        startActivity(new Intent(context,cla));
    }
}
