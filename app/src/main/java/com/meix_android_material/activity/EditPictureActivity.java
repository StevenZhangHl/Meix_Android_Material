package com.meix_android_material.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.photoview.OnMatrixChangedListener;
import com.luck.picture.lib.photoview.PhotoView;
import com.meix.library.ui.ShapeTextView;
import com.meix_android_material.R;
import com.meix_android_material.widget.DoodleView;

import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * 编辑图片
 */
public class EditPictureActivity extends AppCompatActivity {
    public final static String EXTRA_IMAGE_PATH = "imagePath";
    private DoodleView iv_picture;
    private LinearLayout ll_function;
    private LinearLayout ll_bottom_function;
    private TextView tv_back;
    private TextView tv_translate;
    private AppCompatSeekBar scale_seekBar;
    private String mPath;

    public static void startEditPictureActivity(Context context, String path) {
        Intent intent = new Intent();
        intent.putExtra(EditPictureActivity.EXTRA_IMAGE_PATH, path);
        intent.setClass(context, EditPictureActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_picture);
        iv_picture = findViewById(R.id.iv_picture);
        ll_function = findViewById(R.id.ll_function);
        ll_bottom_function = findViewById(R.id.ll_bottom_function);
        tv_translate =findViewById(R.id.tv_translate);
        tv_back = findViewById(R.id.tv_back);
        scale_seekBar = findViewById(R.id.scale_seekBar);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_function.setVisibility(View.INVISIBLE);
                ll_bottom_function.setVisibility(View.VISIBLE);
            }
        });
        tv_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_function.setVisibility(View.VISIBLE);
                ll_bottom_function.setVisibility(View.INVISIBLE);
            }
        });
        scale_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               iv_picture.setRotate(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Intent intent = getIntent();
        mPath = intent.getStringExtra(EXTRA_IMAGE_PATH);
        iv_picture.setFinishDrawListener(() -> {

        });
        Bitmap bitmap = BitmapFactory.decodeFile(mPath);
        iv_picture.setOriginBitmap(bitmap);
    }
}