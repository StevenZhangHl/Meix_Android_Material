package com.meix_android_material.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.photoview.OnViewTapListener;
import com.luck.picture.lib.photoview.PhotoView;
import com.luck.picture.lib.widget.PreviewViewPager;
import com.meix_android_material.R;

import java.util.ArrayList;

/**
 * 图片预览
 */
public class PreviewPictureActivity extends AppCompatActivity {
    public final static String EXTRA_IMAGE_PATHS = "imagePaths";
    public final static String EXTRA_CURRENT_INDEX = "curIdx";
    private PreviewViewPager view_pager;
    private TextView tv_edit;
    private int mCurIdx;
    private ArrayList<String> mFilePathList = null;

    public static void startPreViewActivity(Context context, int mCurIdx, ArrayList<String> mPreviewPaths) {
        Intent intent = new Intent();
        intent.putExtra(PreviewPictureActivity.EXTRA_CURRENT_INDEX, mCurIdx);
        intent.putStringArrayListExtra(PreviewPictureActivity.EXTRA_IMAGE_PATHS, mPreviewPaths);
        intent.setClass(context, PreviewPictureActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_picture);
        view_pager = findViewById(R.id.view_pager);
        tv_edit = findViewById(R.id.tv_edit);
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPictureActivity.startEditPictureActivity(PreviewPictureActivity.this, mFilePathList.get(mCurIdx));
            }
        });
        Intent intent = getIntent();
        mCurIdx = intent.getIntExtra(EXTRA_CURRENT_INDEX, 0);
        mFilePathList = intent.getStringArrayListExtra(EXTRA_IMAGE_PATHS);
        view_pager.setAdapter(new PhotoPagerAdapter());
        view_pager.setCurrentItem(mCurIdx);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurIdx = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class PhotoPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mFilePathList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            String filePath = mFilePathList.get(position);
            Glide.with(PreviewPictureActivity.this)
                    .load(filePath)
                    .into(photoView);
            photoView.setTag(position);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            photoView.setOnViewTapListener(new OnViewTapListener() {

                @Override
                public void onViewTap(View view, float x, float y) {
                    PreviewPictureActivity.this.finish();
                }
            });
            photoView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    int tag = (Integer) v.getTag();
                    return false;
                }
            });

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}