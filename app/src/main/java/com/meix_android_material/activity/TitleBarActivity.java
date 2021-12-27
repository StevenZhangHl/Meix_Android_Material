package com.meix_android_material.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.meix.library.ui.TitleBar;
import com.meix_android_material.R;

public class TitleBarActivity extends AppCompatActivity {
    /**
     * 正常标题栏
     */
    private TitleBar mTitlebarOne;
    /**
     * 自定义返回按钮点击事件
     */
    private TitleBar mTitlebarTwo;
    /**
     * 右侧带图标的标题栏
     */
    private TitleBar mTitlebarThree;
    /**
     * 右侧带文字的标题栏
     */
    private TitleBar mTitlebarFour;
    /**
     * 自定义中间标题的标题栏
     */
    private TitleBar mTitlebarFive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_bar);
        initView();
    }

    private void initView() {
        mTitlebarOne = findViewById(R.id.titleBar_one);
        mTitlebarTwo = findViewById(R.id.titleBar_two);
        mTitlebarThree = findViewById(R.id.titleBar_three);
        mTitlebarFour = findViewById(R.id.titleBar_four);
        mTitlebarFive = findViewById(R.id.titleBar_five);
        mTitlebarOne.getBuilder(this)
                .setMiddleTitle("正常标题栏")
                .create();
        mTitlebarTwo.getBuilder(this)
                .setMiddleTitle("自定义返回键")
                .create();
        mTitlebarThree.getBuilder(this)
                .setMiddleTitle("右侧带图标的标题栏")
                .setRightImgs(new int[]{R.mipmap.iocn_search, R.mipmap.icon_add})
                .create();
    }
}