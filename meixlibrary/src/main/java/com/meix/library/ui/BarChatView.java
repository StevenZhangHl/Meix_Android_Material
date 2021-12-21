package com.meix.library.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.meix.library.utils.DisplayUtil;

/**
 * author steven
 * date 2021/12/17
 * description
 */
public class BarChatView extends View {
    private Context mContext;
    private int width;
    private int height;
    /**
     * 背景线画笔
     */
    private Paint mStrokePaint;
    /**
     * 背景线框的粗细
     */
    private int lineHeight = 0;
    private int leftAxisWidth;
    private int barWidth = 0;

    public BarChatView(Context context) {
        super(context);
        initConfig(context, null);
    }

    public BarChatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initConfig(context, attrs);
    }

    public BarChatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initConfig(context, attrs);
    }

    private void initConfig(Context context, AttributeSet attrs) {
        this.mContext = context;
        lineHeight = DisplayUtil.dip2px(mContext, 1);
        barWidth = DisplayUtil.dip2px(mContext, 10);
        initPaint();
    }

    private void initPaint() {
        mStrokePaint = new Paint();
        mStrokePaint.setStrokeWidth(lineHeight);
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setColor(Color.parseColor("#999999"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        leftAxisWidth = DisplayUtil.dip2px(mContext, 20);
        draGrid(canvas);
        drawBar(canvas);
        canvas.restore();
    }

    /**
     * 画背景网格
     *
     * @param canvas
     */
    private void draGrid(Canvas canvas) {
        int gridLineCount = 5;
        int dash = (height - getPaddingBottom() - getPaddingTop()) / gridLineCount;
        for (int i = 0; i < gridLineCount + 1; i++) {
            int startX = leftAxisWidth;
            int startY = dash * i + getPaddingTop();
            int endX = width;
            int endY = dash * i + getPaddingTop();
            canvas.drawLine(startX, startY, endX, endY, mStrokePaint);
            Paint.FontMetricsInt metrics = mStrokePaint.getFontMetricsInt();
            float y = startY + ((Math.abs(metrics.ascent) + metrics.descent) / 2 - metrics.descent);
            canvas.drawText("30%", 0, y, mStrokePaint);
        }
    }

    /**
     * 画柱状
     *
     * @param canvas
     */
    private void drawBar(Canvas canvas) {
        int barDash = DisplayUtil.dip2px(mContext, 10);
        int barCount = (width - leftAxisWidth) / (barDash + barWidth);
        for (int i = 0; i < barCount; i++) {
            int left = leftAxisWidth + i * (barDash + barWidth);
            int top = getPaddingTop() + (int) (Math.random() * (height - getPaddingTop()));
            int right = leftAxisWidth + i * (barDash + barWidth) + barWidth;
            int bottom = height - getPaddingBottom();
            Rect rect = new Rect(left, top, right, bottom);
            canvas.drawRect(rect, mStrokePaint);
        }
    }
}
