package com.meix_android_material.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import com.meix.library.ui.CircleProgressBar;
import com.meix.library.utils.NumberUtil;
import com.meix_android_material.R;

/**
 * author steven
 * date 2021/12/20
 * description 图表
 */
public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}
