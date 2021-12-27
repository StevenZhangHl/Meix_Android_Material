package com.meix_android_material.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.luck.picture.lib.tools.ToastUtils;
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
     * 右侧带文字和图标的标题栏
     */
    private TitleBar mTitlebarFive;

    /**
     * 自定义中间标题的标题栏
     */
    private TitleBar mTitlebarSix;

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
        mTitlebarSix = findViewById(R.id.titleBar_six);

        mTitlebarOne.getBuilder(this)
                .setMiddleTitle("正常")
                .create();

        mTitlebarTwo.getBuilder(this)
                .setMiddleTitle("自定义返回键")
                .setOnClickLeftBackListener(() -> {
                    ToastUtils.s(this, "确定退出嘛？");
                })
                .create();

        mTitlebarThree.getBuilder(this)
                .setMiddleTitle("右侧带图标")
                .setRightImgs(new int[]{R.mipmap.iocn_search, R.mipmap.icon_add})
                .setOnClickRightImgsListener(position -> {
                    switch (position) {
                        case 0:
                            ToastUtils.s(this, "点击了搜索");
                            break;
                        case 1:
                            ToastUtils.s(this, "点击了新增");
                            break;
                    }
                })
                .create();

        mTitlebarFour.getBuilder(this)
                .setMiddleTitle("右侧带文字")
                .setRightStrs(new String[]{"完成"})
                .setOnClickRightStrsListener(position -> {
                    ToastUtils.s(this, "完成");
                })
                .create();

        mTitlebarFive.getBuilder(this)
                .setMiddleTitle("带文字和图标")
                .setRightStrs(new String[]{"完成"})
                .setRightImgs(new int[]{R.mipmap.iocn_search})
                .setOnClickRightImgsListener(position -> {
                    ToastUtils.s(this, "点击了搜索");
                })
                .setOnClickRightStrsListener(position -> {
                    ToastUtils.s(this, "完成");
                })
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.include_middle_title, null);
        mTitlebarSix.getBuilder(this)
                .setMiddleTitle("自定义中间标题view")
                .setSelfMiddleView(view)
                .create();
    }
}