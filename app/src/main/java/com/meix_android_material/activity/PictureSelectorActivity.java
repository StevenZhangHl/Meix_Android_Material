package com.meix_android_material.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.meix.library.utils.DisplayUtil;
import com.meix.library.utils.SpaceItemDecoration;
import com.meix_android_material.R;
import com.meix_android_material.adapter.ShowArticlePictureAdapter;
import com.meix_android_material.utils.GlideEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PictureSelectorActivity extends AppCompatActivity {
    private RecyclerView recycler_view_picture;
    private ShowArticlePictureAdapter pictureAdapter;
    private List<LocalMedia> mSelectPictures = new ArrayList<>();
    private int selectPictureCount = 0;
    private int maxSelectCount = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_selector);
        recycler_view_picture = findViewById(R.id.recycler_view_picture);
        recycler_view_picture.setLayoutManager(new GridLayoutManager(this, 3));
        recycler_view_picture.addItemDecoration(new SpaceItemDecoration(3, DisplayUtil.dip2px(this, 5), false));
        pictureAdapter = new ShowArticlePictureAdapter(R.layout.item_show_picture, new ArrayList<>());
        recycler_view_picture.setAdapter(pictureAdapter);
        updateItemTouchHelper();
        pictureAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            LocalMedia info = mSelectPictures.get(position);
            if (info.getId() == 0) {
                PictureSelector.create(PictureSelectorActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .maxSelectNum(maxSelectCount - selectPictureCount)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            } else {
                List<LocalMedia> mPreviewPictures = new ArrayList<>();
                if (selectPictureCount < maxSelectCount) {
                    for (int i = 0; i < mSelectPictures.size(); i++) {
                        if (mSelectPictures.get(i).getId() != 0) {
                            mPreviewPictures.add(mSelectPictures.get(i));
                        }
                    }
                }
                PictureSelector.putIntentResult(mPreviewPictures);
                PictureSelector.create(PictureSelectorActivity.this);
            }
        });
        initPictureData();
    }

    private void updateItemTouchHelper() {
        ItemDragAndSwipeCallback swipeCallback = new ItemDragAndSwipeCallback(pictureAdapter) {
            @Override
            public boolean canDropOver(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder current, @NonNull RecyclerView.ViewHolder target) {
                return target.getLayoutPosition() != recyclerView.getAdapter().getItemCount() - 1 && super.canDropOver(recyclerView, current, target);
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getLayoutPosition();
                if (position == mSelectPictures.size() - 1) {
                    return 0;
                }
                return super.getMovementFlags(recyclerView, viewHolder);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Collections.swap(mSelectPictures, viewHolder.getLayoutPosition(), target.getLayoutPosition());
                pictureAdapter.notifyItemMoved(viewHolder.getLayoutPosition(), target.getLayoutPosition());
                return false;
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(recycler_view_picture);
        pictureAdapter.enableDragItem(itemTouchHelper, R.id.iv_show_picture, true);
    }


    private void initPictureData() {
        addFooterData();
        pictureAdapter.setNewData(mSelectPictures);
    }

    private void addFooterData() {
        LocalMedia localMedia = new LocalMedia();
        localMedia.setId(0);
        for (int i = 0; i < mSelectPictures.size(); i++) {
            if (mSelectPictures.get(i).getId() == 0) {
                mSelectPictures.remove(i);
            }
        }
        if (selectPictureCount != 9) {
            mSelectPictures.add(localMedia);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> result = PictureSelector.obtainMultipleResult(data);
                    mSelectPictures.addAll(result);
                    selectPictureCount = selectPictureCount + result.size();
                    addFooterData();
                    pictureAdapter.setNewData(mSelectPictures);
                    break;
                default:
                    break;
            }
        }
    }
}