package com.sdlifes.sdlifes.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdlifes.sdlifes.R;

import java.util.List;

/**
 * Data:  2020/4/27
 * Auther: xcd
 * Description:
 */
public class BabyDialogFragment extends DialogFragment implements BaseQuickAdapter.OnItemClickListener {

    private View frView;
    private Window window;
    private RecyclerView recyclerView;
    private UserInfoBottomOptionAdapter adapter;
    private LinearLayoutManager layoutManager;
    private SexClickListener listener;
    private List<String> list;
    public void setData(List<String> list,SexClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 去掉默认的标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        frView = inflater.inflate(R.layout.dialog_fr_bottom, null);

        return frView;
    }

    @Override
    public void onStart() {
        super.onStart();
        window = getDialog().getWindow();
        // 如果不设置这句代码, 那么弹框就会与四边都有一定的距离
        window.setBackgroundDrawableResource(android.R.color.white);
        // 设置动画
        window.setWindowAnimations(R.style.bottomDialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
        params.height = getResources().getDisplayMetrics().heightPixels;
        window.setAttributes(params);
        final Dialog dialog = getDialog();
        // 下面这些设置必须在此方法(onStart())中才有效
            recyclerView = (RecyclerView) frView.findViewById(R.id.recycler_view);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new UserInfoBottomOptionAdapter(R.layout.dialog_bottom, list);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(getRecyclerViewDivider(R.drawable.inset_recyclerview_divider_1px));

        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//            double v = dm.widthPixels * 0.75* 0.75;
//            tv_content.setMinimumHeight((int) v);
            if (list != null && list.size() > 7) {
                window.setLayout(-1, (int) (dm.heightPixels * 0.4));
            } else {
                window.setLayout(-1, -2);
            }
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<String> data = adapter.getData();
        String value = data.get(position);
        int sex = -1;
        if ("男".equals(value)){
            sex = 1;
        }else if ("女".equals(value)){
            sex = 2;
        }else {
            sex = 0;
        }
        listener.OnItemClick(sex);
        dismiss();
    }
    public interface SexClickListener{
        void OnItemClick(int sex);
    }
    public RecyclerView.ItemDecoration getRecyclerViewDivider(@DrawableRes int drawableId) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(drawableId));
        return itemDecoration;
    }
}
