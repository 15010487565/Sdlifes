package www.xcd.com.mylibrary.base.activity;

import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

/**
 * Created by gs on 2020-08-30.
 */
public abstract class NoTitleActivity extends FragmentActivity {

    public RecyclerView.ItemDecoration getRecyclerViewDivider(@DrawableRes int drawableId) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(drawableId));
        return itemDecoration;
    }
}
