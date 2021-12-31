package com.meix_android_material.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.meix.library.ui.ThreeDView;
import com.meix.library.ui.TitleBar;
import com.meix_android_material.R;

/**
 * 3D
 */
public class ThreeDimensionsActivity extends AppCompatActivity {
    private AppCompatSeekBar seekBar;
    private TitleBar mTitleBar;
    private ThreeDView mThree3d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_dimensions);
        initView();
    }

    private void initView() {
        mTitleBar = findViewById(R.id.titleBar);
        seekBar = findViewById(R.id.progress_bar);
        mThree3d = findViewById(R.id.three_3d);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mThree3d.setRadius(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}