package com.meix_android_material.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.meix.library.ui.ShapeTextView;
import com.meix_android_material.R;
import com.meix_android_material.model.PageModel;

import java.util.List;

/**
 * author steven
 * date 2021/12/20
 * description
 */
public class PageAdapter extends BaseQuickAdapter<PageModel, BaseViewHolder> {

    public PageAdapter(int layoutResId, List<PageModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, PageModel item) {
        ShapeTextView page_name = holder.getView(R.id.page_name);
        page_name.setTextContent(item.getPageName());
    }
}
