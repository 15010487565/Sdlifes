package com.sdlifes.sdlifes.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.adapter.SearchAdapter;
import com.sdlifes.sdlifes.adapter.SearchLikeAdapter;
import com.sdlifes.sdlifes.db.RcCursorAdapter;
import com.sdlifes.sdlifes.db.RecordSQLiteOpenHelper;
import com.sdlifes.sdlifes.listener.OnRcItemClickListener;
import com.sdlifes.sdlifes.model.SearchLikeModel;
import com.sdlifes.sdlifes.model.SearchModel;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.ActivityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.http.HttpInterface;
import www.xcd.com.mylibrary.utils.ShareHelper;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class SearchActivity extends AppCompatActivity implements HttpInterface,
        View.OnClickListener, BaseQuickAdapter.OnItemClickListener ,
        TextView.OnEditorActionListener, TextWatcher ,
        OnRcItemClickListener,
        SwipeRefreshLayout.OnRefreshListener{

    private LinearLayout llHint;
    private RecyclerView recyclerView, rcHistory, rcSearchLike;
    private LinearLayoutManager layoutManager;
    private SearchAdapter adapter;
    EditText etSearch;
    private LinearLayout llHistory, llSearchLike;
    private ImageView ivCleanHistory;
    private NestedScrollView nsv;
    private SwipeRefreshLayout ly_pull_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ly_pull_refresh = findViewById(R.id.ly_pull_refresh);
        ly_pull_refresh.setOnRefreshListener(this);
        //设置样式刷新显示的位置
        ly_pull_refresh.setProgressViewOffset(true, -20, 100);
        ly_pull_refresh.setColorSchemeResources(R.color.red, R.color.blue, R.color.black);
        ly_pull_refresh.setRefreshing(false);

        llHint = findViewById(R.id.ll_Hint);
        llHint.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.rc_Search);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nsv = findViewById(R.id.nsv);
        //搜索
        List<SearchModel.DataBean> list = new ArrayList();
        adapter = new SearchAdapter(R.layout.item_search_adapter, list);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(getRecyclerViewDivider(R.drawable.inset_recyclerview_divider));
        findViewById(R.id.iv_del).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);

        etSearch = findViewById(R.id.et_Serch);
        etSearch.setOnEditorActionListener(this);
        etSearch.addTextChangedListener(this);
        //历史记录
        llHistory = findViewById(R.id.ll_History);
        ivCleanHistory = findViewById(R.id.iv_cleanHistory);
        ivCleanHistory.setOnClickListener(this);
        rcHistory = findViewById(R.id.rc_History);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rcHistory.setLayoutManager(gridLayoutManager);
        cursorAdapter = new RcCursorAdapter(SearchActivity.this);
        cursorAdapter.setOnItemClickListener(this);
        rcHistory.setAdapter(cursorAdapter);
        //自定义的分隔线
        DividerItemDecoration itemDecorationVer = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.HORIZONTAL);
        itemDecorationVer.setDrawable(new ColorDrawable(Color.parseColor("#EEEEEE")));
        rcHistory.addItemDecoration(itemDecorationVer);
        rcHistory.addItemDecoration(getRecyclerViewDivider(R.drawable.inset_recyclerview_divider_white));
        //实例化历史搜索数据
        getHistorySearchData();
        //猜你喜欢
        llSearchLike = findViewById(R.id.ll_SearchLike);
        rcSearchLike = findViewById(R.id.rc_SearchLike);
        GridLayoutManager likeLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rcSearchLike.setLayoutManager(likeLayoutManager);
        List<String> listLike = new ArrayList();
        likeAdapter = new SearchLikeAdapter(R.layout.item_search_like_adapter, listLike);
        likeAdapter.setOnItemClickListener(this);
        rcSearchLike.setAdapter(likeAdapter);
        //自定义的分隔线
        DividerItemDecoration likeDecorationVer = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.HORIZONTAL);
        itemDecorationVer.setDrawable(new ColorDrawable(Color.parseColor("#EEEEEE")));
        rcSearchLike.addItemDecoration(likeDecorationVer);
//        rcSearchLike.addItemDecoration(getRecyclerViewDivider(R.drawable.shape_red_round),DividerItemDecoration.VERTICAL);
        rcSearchLike.addItemDecoration(getRecyclerViewDivider(R.drawable.inset_recyclerview_divider_white));

        initSearchLike();

    }

    private void initSearchLike() {
        OkHttpHelper.getRestfulHttp(
                SearchActivity.this, 1000,
                UrlAddr.SEARCH_LIKE, SearchActivity.this);
    }
    // 数据库变量
    // 用于存放历史搜索记录
    private RecordSQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private RcCursorAdapter cursorAdapter;
    private SearchLikeAdapter likeAdapter;

    private void getHistorySearchData() {
        // 2. 实例化数据库SQLiteOpenHelper子类对象
        helper = new RecordSQLiteOpenHelper(this);

        // 3. 第1次进入时查询所有的历史搜索记录
        queryData("");
    }

    /**
     * 关注1
     * 模糊查询数据 & 显示到ListView列表上
     */
    private void queryData(String tempName) {

        // 1. 模糊搜索
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc limit 10", null);
//        // 2. 创建adapter适配器对象 & 装入模糊搜索的结果
//        adapter = new SimpleCursorAdapter(this, R.layout.item_historysearch, cursor, new String[]{"name"},
//                new int[]{R.id.historysearch_title}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 3. 设置适配器
        cursorAdapter.changeCursor(cursor);
//        Log.e("TAG_历史记录","cursor="+cursor.getCount());
        // 当输入框为空 & 数据库中有搜索记录时，显示 "删除搜索记录"按钮
        if (cursor.getCount() != 0) {

            llHistory.setVisibility(View.VISIBLE);

        } else {

            llHistory.setVisibility(View.GONE);
        }

    }

    /**
     * 关注2：清空数据库
     */
    private void deleteData() {

        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }

    /**
     * 关注3
     * 检查数据库中是否已经有该搜索记录
     */
    private boolean hasData(String tempName) {
        // 从数据库中Record表里找到name=tempName的id
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //  判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 关注4
     * 插入数据到数据库，即写入搜索字段到历史搜索记录
     */
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }
    //删除数据信息
    public void deleteData(String tempName){
        db = helper.getWritableDatabase();
        Log.e("TAG_删除","db="+db.isOpen());
        if(db.isOpen()){
            db.execSQL("delete from records where name=('" + tempName + "')" );
            db.close();
        }
    }

    public RecyclerView.ItemDecoration getRecyclerViewDivider(@DrawableRes int drawableId) {

        return getRecyclerViewDivider(drawableId,DividerItemDecoration.VERTICAL);
    }
    public RecyclerView.ItemDecoration getRecyclerViewDivider(@DrawableRes int drawableId,int orientation) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, orientation);
        itemDecoration.setDrawable(getResources().getDrawable(drawableId));
        return itemDecoration;
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        ly_pull_refresh.setRefreshing(false);
        switch (requestCode){
           case 1000:
               SearchLikeModel likeModel = JSON.parseObject(returnData, SearchLikeModel.class);
               List<String> data1 = likeModel.getData();
               likeAdapter.setNewData(data1);
               break;
           case 1001:
               llHint.setVisibility(View.VISIBLE);
               nsv.setVisibility(View.GONE);
               SearchModel searchModel = JSON.parseObject(returnData, SearchModel.class);
               List<SearchModel.DataBean> data = searchModel.getData();
               adapter.setNewData(data);
               break;
       }
    }

    @Override
    public void onErrorResult(int errorCode, String errorExcep) {
        ly_pull_refresh.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_del:
                etSearch.setText("");
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.iv_cleanHistory:
                // 清空数据库->>关注2
                deleteData();
                // 模糊搜索空字符 = 显示所有的搜索历史（此时是没有搜索记录的）
                queryData("");
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter instanceof  SearchAdapter){
            SearchAdapter searchAdapter = (SearchAdapter) adapter;
            List<SearchModel.DataBean> data = searchAdapter.getData();
            SearchModel.DataBean dataBean = data.get(position);
            String title = dataBean.getTitle();
            String content = dataBean.getContent();
            ActivityUtils.startDetailsActivity(this,
                    String.valueOf(dataBean.getId()),
                    title,content,dataBean.getFocus());

        }else if (adapter instanceof  SearchLikeAdapter){
            SearchLikeAdapter likeAdapter = (SearchLikeAdapter) adapter;
            List<String> data = likeAdapter.getData();
            executeData(data.get(position));
        }

    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if(actionId == EditorInfo.IME_ACTION_SEARCH){
            //完成自己的事件
            String searchStr = etSearch.getText().toString().trim();
            if (TextUtils.isEmpty(searchStr)){
                ToastUtil.showToast("搜索内容不能为空！");
            }else {
                executeData(searchStr);
            }
        }
        return false;
    }

    private void executeData(String searchStr) {
        // 1. 点击搜索键后，对该搜索字段在数据库是否存在进行检查（查询）->> 关注1
        boolean hasData = hasData(searchStr);
        // 2. 若存在，则不保存；若不存在，则将该搜索字段保存（插入）到数据库，并作为历史搜索记录
        if (!hasData) {
            insertData(searchStr);
            queryData("");
        }else {
            deleteData(searchStr);
            insertData(searchStr);
            queryData("");
        }
        ly_pull_refresh.setRefreshing(true);
        etSearch.setText(searchStr);
        String userId = ShareHelper.getUserId();
        OkHttpHelper.getRestfulHttp(
                SearchActivity.this, 1001,
                UrlAddr.SEARCH +(TextUtils.isEmpty(userId)?"0":userId)+"/" +searchStr, SearchActivity.this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //完成自己的事件
        String searchStr = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(searchStr)){
            llHint.setVisibility(View.GONE);
            nsv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void OnItemClick(View view, int position) {
        LinearLayout layout = (LinearLayout)view;
        TextView textView = (TextView) layout.findViewById(R.id.historysearch_title);
        if (textView !=null&&textView.getText()!=null)
        executeData(textView.getText().toString());
    }

    @Override
    public void onRefresh() {
        String searchStr = etSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(searchStr)){
            executeData(searchStr);
        }else {
            ly_pull_refresh.setRefreshing(false);
        }
    }
}
