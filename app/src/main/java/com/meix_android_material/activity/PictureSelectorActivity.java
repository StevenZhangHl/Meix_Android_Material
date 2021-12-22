package com.meix_android_material.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Region;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

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
    private FrameLayout fl_parent;
    private ShowArticlePictureAdapter pictureAdapter;
    private List<LocalMedia> mSelectPictures = new ArrayList<>();
    private int selectPictureCount = 0;
    private int maxSelectCount = 9;
    private int spanCount = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_selector);
        recycler_view_picture = findViewById(R.id.recycler_view_picture);
        fl_parent = findViewById(R.id.fl_parent);
        recycler_view_picture.setLayoutManager(new GridLayoutManager(this, spanCount));
        recycler_view_picture.addItemDecoration(new SpaceItemDecoration(spanCount, DisplayUtil.dip2px(this, 5), false));
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
                ArrayList<String> mPreviewPaths = new ArrayList<>();
                if (selectPictureCount < maxSelectCount) {
                    for (int i = 0; i < mSelectPictures.size(); i++) {
                        if (mSelectPictures.get(i).getId() != 0) {
                            mPreviewPaths.add(mSelectPictures.get(i).getRealPath());
                        }
                    }
                }
                PreviewPictureActivity.startPreViewActivity(PictureSelectorActivity.this, position, mPreviewPaths);
            }
        });
        fl_parent.bringChildToFront(recycler_view_picture);
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

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                c.save();
                if (dX > 0) {
                    c.clipRect(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + dX, itemView.getBottom(), Region.Op.UNION);
                    c.translate(itemView.getLeft(), itemView.getTop());
                } else {
                    c.clipRect(itemView.getRight() + dX, itemView.getTop(),
                            itemView.getRight(), itemView.getBottom(), Region.Op.UNION);
                    c.translate(itemView.getRight() + dX, itemView.getTop());
                }
                pictureAdapter.onItemSwiping(c, viewHolder, dX, dY, isCurrentlyActive);
                c.restore();
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