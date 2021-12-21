package com.meix_android_material.adapter;

import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.luck.picture.lib.entity.LocalMedia;
import com.meix.library.utils.DisplayUtil;
import com.meix_android_material.R;
import com.meix_android_material.utils.GlideEngine;

import java.util.List;

/**
 * author steven
 * date 2021/12/20
 * description 发表文字图片编辑器
 */
public class ShowArticlePictureAdapter extends BaseItemDraggableAdapter<LocalMedia, BaseViewHolder> {
    public ShowArticlePictureAdapter(int layoutResId, @Nullable List<LocalMedia> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalMedia item) {
        AppCompatImageView iv_show_picture = helper.getView(R.id.iv_show_picture);
        int imageWidth = (DisplayUtil.getScreenWidth(mContext) - DisplayUtil.dip2px(mContext, 32 + 10)) / 3;
        int imageHeight = imageWidth;
        ViewGroup.LayoutParams params = iv_show_picture.getLayoutParams();
        params.height = imageHeight;
        iv_show_picture.setLayoutParams(params);
        if (item.getId() == 0) {
            GlideEngine.createGlideEngine().loadShowGridImage(mContext, R.mipmap.bg_add_img, iv_show_picture);
        }else {
            GlideEngine.createGlideEngine().loadShowGridImage(mContext, item.getPath(), iv_show_picture);
        }
        iv_show_picture.bringToFront();
        helper.addOnClickListener(R.id.iv_show_picture);
    }
}
