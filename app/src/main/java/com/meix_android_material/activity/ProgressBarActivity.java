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
 * description
 */
public class ProgressBarActivity extends AppCompatActivity {
    private CircleProgressBar progress_circular;
    private CircleProgressBar unlock_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressbar);
        progress_circular = findViewById(R.id.progress_circular);
        unlock_bar = findViewById(R.id.unlock_bar);
        progress_circular.with()
                .setBuilderCurrentProgress(60)
                .setBuilderDuration(1200)
                .setBuilderTextScale(0);
        unlock_bar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startRun();
                return false;
            }
        });
    }

    private void startRun() {
        startAnim(100);
    }

    private ValueAnimator valueAnimator;

    private void startAnim(float currentProgress) {
        valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(0, currentProgress);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setEvaluator(null);
        valueAnimator.removeAllUpdateListeners();
        ValueAnimator.AnimatorUpdateListener listener = animation -> {
            float mCurrentProgress = NumberUtil.formatFloatPoint((Float) animation.getAnimatedValue(), 0);
            unlock_bar.setCurrentProgress(mCurrentProgress);
        };
        valueAnimator.addUpdateListener(listener);
        valueAnimator.setTarget(null);
        valueAnimator.start();
    }
}
