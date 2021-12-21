package com.meix_android_material;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.meix.library.ui.CircleProgressBar;
import com.meix.library.ui.DragView;
import com.meix.library.utils.NumberUtil;
import com.meix.library.utils.ProviderUtil;
import com.meix_android_material.activity.ChatActivity;
import com.meix_android_material.activity.CustomDialogActivity;
import com.meix_android_material.activity.PictureSelectorActivity;
import com.meix_android_material.activity.ProgressBarActivity;
import com.meix_android_material.activity.ShapeTextViewActivity;
import com.meix_android_material.adapter.PageAdapter;
import com.meix_android_material.model.PageModel;
import com.meix_android_material.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recycler_view;
    private PageAdapter pageAdapter;
    private List<PageModel> pageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        pageAdapter = new PageAdapter(R.layout.item_page, new ArrayList<>());
        recycler_view.setAdapter(pageAdapter);
        pageAdapter.setOnItemClickListener((adapter, view, position) -> {
            PageModel info = pageList.get(position);
            goPageById(info);
        });
        pageList = GsonUtil.json2List(ProviderUtil.getStrFromAssets("pageJson", this), PageModel.class);
        updateAdapter();
    }

    private void updateAdapter() {
        pageAdapter.setNewData(pageList);
    }

    private void goPageById(PageModel info){
        Intent intent = new Intent();
        intent.putExtra("pageName", info.getPageName());
        switch (info.getPageId()) {
            case 1:
                intent.setClass(MainActivity.this, ProgressBarActivity.class);
                break;
            case 2:
                intent.setClass(MainActivity.this, ShapeTextViewActivity.class);
                break;
            case 3:
                intent.setClass(MainActivity.this, ChatActivity.class);
                break;
            case 4:
                intent.setClass(MainActivity.this, CustomDialogActivity.class);
                break;
            case 5:
                intent.setClass(MainActivity.this, PictureSelectorActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }

}