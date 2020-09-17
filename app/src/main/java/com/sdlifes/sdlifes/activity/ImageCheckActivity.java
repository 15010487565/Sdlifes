package com.sdlifes.sdlifes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.adapter.ImageCheckAdapter;
import com.sdlifes.sdlifes.dialog.UpLoadDialogFragment;
import com.sdlifes.sdlifes.view.ImageCheckViewPager;

import java.util.ArrayList;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;

public class ImageCheckActivity extends SimpleTopbarActivity {

    ImageCheckViewPager viewPager;
    ArrayList<String> imageInfoList = new ArrayList<>();
    TextView numView;
    String josnString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_check);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        imageInfoList = intent.getStringArrayListExtra("listjson");

        numView = (TextView) findViewById(R.id.num);

        viewPager = (ImageCheckViewPager) findViewById(R.id.viewpager);
        ImageCheckAdapter imageCheckAdapter = new ImageCheckAdapter(this, imageInfoList);
        viewPager.setAdapter(imageCheckAdapter);

        viewPager.setCurrentItem(position);
        numView.setText(position + 1 + "/" + imageInfoList.size());
        if(imageInfoList.size() > 1)
        {
            numView.setVisibility(View.VISIBLE);
        }else {
            numView.setVisibility(View.GONE);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                numView.setText(position + 1 + "/" + imageInfoList.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 显示保存图片提示Dialog
     *
     * @param imgUrl 要保存的图片URL
     */
    public void showDialog(String imgUrl) {

        UpLoadDialogFragment dialogFr = new UpLoadDialogFragment();
        dialogFr.setData(imgUrl);
        dialogFr.show(getSupportFragmentManager(), "ImageCheckActivity");

    }
}
