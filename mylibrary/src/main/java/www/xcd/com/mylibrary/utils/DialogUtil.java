package www.xcd.com.mylibrary.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;

import www.xcd.com.mylibrary.R;


public class DialogUtil {


    public static Dialog getProgressDialog(Activity context, int string) {

        final Dialog dialog = new Dialog(context, R.style.FullScreenDialog);
        dialog.setCancelable(false);//返回键是否能关闭
        dialog.setContentView(R.layout.custom_progress_dialog);
        return dialog;
    }

    public static Dialog getProgressDialog(Context context) {

        final Dialog dialog = new Dialog(context, R.style.FullScreenDialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_progress_dialog);
        return dialog;
    }

    public interface OnClickListener{
        void onClick(View view, String  message);
    }
    private Context context;
    private AlertDialog dialog;
    private boolean cancelable = true;
    private CharSequence title, message,hint, cancelText, sureText;
    private int inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD;
    private OnClickListener sureClickListener, cancelClickListener;

    private static DialogUtil single = null;

    private DialogUtil() {

    }

    public static DialogUtil getInstance() {

        if (single == null) {
            single = new DialogUtil();
        }
        return single;
    }

    public Context getContext() {
        return context;
    }

    public DialogUtil setContext(Context context) {
        this.context = context;
        return this;
    }
    //能否返回键取消
    public DialogUtil setCancelable(Boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public DialogUtil title(CharSequence title) {
        this.title = title;
        return this;
    }

    public DialogUtil message(CharSequence message) {
        this.message = message;
        return this;
    }
    public DialogUtil hint(CharSequence hint) {
        this.hint = hint;
        return this;
    }
    public DialogUtil cancelText(CharSequence str) {
        this.cancelText = str;
        return this;
    }

    //确定按钮文本
    public DialogUtil sureText(CharSequence str) {
        this.sureText = str;
        return this;
    }
    //能否返回键取消
    public DialogUtil setInputType(int inputType) {
        this.inputType = inputType;
        return this;
    }

    public DialogUtil setSureOnClickListener(OnClickListener listener) {
        this.sureClickListener = listener;
        return this;
    }

    public DialogUtil setCancelOnClickListener(OnClickListener listener) {
        this.cancelClickListener = listener;
        return this;
    }

    public AlertDialog showDefaultDialog() {
        LayoutInflater factor = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View serviceView = factor.inflate(R.layout.custom_dialog_layout_default, null);
        //标题
        TextView dialogTitle = serviceView.findViewById(R.id.dialog_Title);
        if (TextUtils.isEmpty(title)) {
            dialogTitle.setVisibility(View.GONE);
        } else {
            dialogTitle.setVisibility(View.VISIBLE);
            dialogTitle.setText(title);
        }
        //正文
        final TextView dialogMessage = serviceView.findViewById(R.id.dialog_Message);
        if (TextUtils.isEmpty(message)) {
            dialogMessage.setVisibility(View.GONE);
        } else {
            dialogMessage.setVisibility(View.VISIBLE);
            dialogMessage.setText(message);
        }
        dialogMessage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (dialogMessage.getLineCount() == 1) {
                    dialogMessage.setGravity(Gravity.CENTER);
                }
                return true;
            }
        });

        //取消
        TextView dialogCancel = serviceView.findViewById(R.id.dialog_Cancel);
        if (TextUtils.isEmpty(cancelText)) {
            dialogCancel.setVisibility(View.GONE);
        } else {
            dialogCancel.setVisibility(View.VISIBLE);
            dialogCancel.setText(cancelText);
        }

        //确定
        TextView dialogOk = serviceView.findViewById(R.id.dialog_Ok);
        if (TextUtils.isEmpty(sureText)) {
            dialogOk.setVisibility(View.GONE);
        } else {
            dialogOk.setVisibility(View.VISIBLE);
            dialogOk.setText(sureText);
        }


        Activity activity = (Activity) context;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        dialog = builder.create();
        dialog.setCancelable(cancelable);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return !cancelable;
                } else {
                    return false; //默认返回 false
                }
            }
        });
        dialog.show();
        dialog.setContentView(serviceView);
        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(Gallery.LayoutParams.FILL_PARENT, Gallery.LayoutParams.WRAP_CONTENT);
        //layout.setMargins(WallspaceUtil.dip2px(this, 10), 0, FeatureFunction.dip2px(this, 10), 0);
        serviceView.setLayoutParams(layout);
        //设置点击监听
        if (sureClickListener != null) {
            dialogOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sureClickListener.onClick(view,"");
                    dialog.dismiss();
                }
            });
        }
        if (cancelClickListener != null) {
            dialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelClickListener.onClick(view,"");
                    dialog.dismiss();
                }
            });
        }
        return dialog;
    }
    public AlertDialog showEditDialog() {
        LayoutInflater factor = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View serviceView = factor.inflate(R.layout.custom_dialog_layout, null);
        //标题
        TextView dialogTitle = serviceView.findViewById(R.id.dialog_Title);
        if (TextUtils.isEmpty(title)) {
            dialogTitle.setVisibility(View.GONE);
        } else {
            dialogTitle.setVisibility(View.VISIBLE);
            dialogTitle.setText(title);
        }
        //正文
        final EditText dialogMessage = serviceView.findViewById(R.id.dialog_Message);
        dialogMessage.setInputType(inputType);
        if (TextUtils.isEmpty(hint)) {
            if (TextUtils.isEmpty(message)){
                dialogMessage.setText(message);
            }else {
                dialogMessage.setText("");
            }

        } else {
            dialogMessage.setHint(hint);
        }
        dialogMessage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (dialogMessage.getLineCount() == 1) {
                    dialogMessage.setGravity(Gravity.CENTER);
                }
                return true;
            }
        });

        //取消
        TextView dialogCancel = serviceView.findViewById(R.id.dialog_Cancel);
        if (TextUtils.isEmpty(cancelText)) {
            dialogCancel.setVisibility(View.GONE);
        } else {
            dialogCancel.setVisibility(View.VISIBLE);
            dialogCancel.setText(cancelText);
        }

        //确定
        TextView dialogOk = serviceView.findViewById(R.id.dialog_Ok);
        if (TextUtils.isEmpty(sureText)) {
            dialogOk.setVisibility(View.GONE);
        } else {
            dialogOk.setVisibility(View.VISIBLE);
            dialogOk.setText(sureText);
        }


        Activity activity = (Activity) context;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        dialog = builder.create();
        dialog.setCancelable(cancelable);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return !cancelable;
                } else {
                    return false; //默认返回 false
                }
            }
        });
        dialog.show();
        dialog.setContentView(serviceView);
        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //layout.setMargins(WallspaceUtil.dip2px(this, 10), 0, FeatureFunction.dip2px(this, 10), 0);
        serviceView.setLayoutParams(layout);
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        // 就是这个属性导致不能获取焦点,默认的是FLAG_NOT_FOCUSABLE,故名思义不能获取输入焦点
        window.getAttributes().flags = window.getAttributes().FLAG_DIM_BEHIND;
        window.setAttributes(window.getAttributes());
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置点击监听
        if (sureClickListener != null) {
            dialogOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String trim = dialogMessage.getText().toString().trim();
                    if (TextUtils.isEmpty(trim)){
                        ToastUtil.showToast("内容不能为空");
                        return;
                    }
                    sureClickListener.onClick(view,trim);
                    dialog.dismiss();
                }
            });
        }
        if (cancelClickListener != null) {
            dialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelClickListener.onClick(view,"");
                    dialog.dismiss();
                }
            });
        }

        return dialog;
    }

}
