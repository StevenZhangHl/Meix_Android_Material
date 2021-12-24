package com.meix_android_material.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;

import com.meix.library.ui.StepsView;
import com.meix_android_material.R;

/**
 * 步骤条
 */
public class StepsViewActivity extends AppCompatActivity {
    private AppCompatButton bt_add_step;
    private StepsView step_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_view);
        step_view = findViewById(R.id.step_view);
        bt_add_step = findViewById(R.id.bt_add_step);
        bt_add_step.setOnClickListener(v -> step_view.setCurrent(step_view.getCurrent() + 1));
    }
}