package com.meix.library.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.meix.library.R;
import com.meix.library.utils.DisplayUtil;

/**
 * author steven
 * date 2021/12/15
 * description 支持圆角和边框的TextView
 */
public class ShapeTextView extends AppCompatTextView {
    private Context mContext;
    private int width;
    private int height;
    /**
     * 属性默认值
     */
    private int defaultStrokeWidth = 1;
    private int defaultStrokeColor = Color.parseColor("#F2F2F2");
    private int defaultTextSize = 14;
    private int defaultTextColor = Color.parseColor("#333333");
    private int defaultShapeColor = Color.parseColor("#333333");
    private int defaultCorner = 4;
    /**
     * 边框画笔
     */
    private Paint mStrokePaint;
    /**
     * 背景画笔
     */
    private Paint mShapePaint;
    /**
     * 文本画笔
     */
    private Paint mTextPaint;

    /**
     * 圆角大小
     */
    private int corner = 0;
    /**
     * 边框的宽度
     */
    private int strokeWidth = 0;
    /**
     * 边框的颜色
     */
    private int strokeColor = 0;
    /**
     * 文字大小
     */
    private int textSize = 0;
    /**
     * 文字的颜色
     */
    private int textColor = 0;
    /**
     * 文字内容
     */
    private String textContent;
    /**
     * 圆角背景颜色
     */
    private int shapeColor = 0;


    public ShapeTextView(Context context) {
        super(context);
        initConfig(context, null);
    }

    public ShapeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initConfig(context, attrs);
    }

    public ShapeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initConfig(context, attrs);
    }

    private void initConfig(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ShapeTextView);
        textContent = array.getString(R.styleable.ShapeTextView_text_content);
        corner = array.getDimensionPixelSize(R.styleable.ShapeTextView_corner, DisplayUtil.dip2px(context, defaultCorner));
        strokeWidth = array.getDimensionPixelSize(R.styleable.ShapeTextView_stroke_width, DisplayUtil.dip2px(context, defaultStrokeWidth));
        strokeColor = array.getColor(R.styleable.ShapeTextView_stroke_color, defaultStrokeColor);
        shapeColor = array.getColor(R.styleable.ShapeTextView_shape_color, defaultShapeColor);
        textSize = array.getDimensionPixelSize(R.styleable.ShapeTextView_text_size, DisplayUtil.sp2px(context, defaultTextSize));
        textColor = array.getColor(R.styleable.ShapeTextView_text_color, defaultTextColor);
        array.recycle();
        initPaint();
    }

    private void initPaint() {
        mStrokePaint = new Paint();
        mStrokePaint.setStrokeWidth(strokeWidth);
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(strokeColor);

        mShapePaint = new Paint();
        mShapePaint.setColor(shapeColor);
        mShapePaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, 100, 0, 800, 100);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        RectF rectF = new RectF(strokeWidth, strokeWidth, width - strokeWidth, height - strokeWidth);
        canvas.drawRoundRect(rectF, corner, corner, mShapePaint);
        canvas.drawRoundRect(rectF, corner, corner, mStrokePaint);
        Paint.FontMetricsInt metrics = mTextPaint.getFontMetricsInt();
        float y = height / 2 + ((Math.abs(metrics.ascent) + metrics.descent) / 2 - metrics.descent);
        canvas.drawText(textContent, width / 2, y, mTextPaint);
        canvas.restore();
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}
