package com.meix.library.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.Nullable;

import com.meix.library.R;
import com.meix.library.utils.DisplayUtil;
import com.meix.library.utils.NumberUtil;

/**
 * author steven
 * date 2021/12/14
 * description 环形进度条 支持链式调用
 */
public class CircleProgressBar extends View {
    private Context mContext;
    /**
     * 进度条画笔
     */
    private Paint mThumbPaint;
    /**
     * 轴道背景画笔
     */
    private Paint mTrackPaint;
    /**
     * 内圆画笔
     */
    private Paint mCenterCirclePaint;
    /**
     * 文字画笔
     */
    private Paint mTextPaint;
    /**
     * 环的宽度
     */
    private int mRingWidth;
    /**
     * 控件的宽度
     */
    private int width = 0;
    /**
     * 控件的高度
     */
    private int height = 0;
    /**
     * 圆心的x坐标
     */
    private int mViewCenterX;
    /**
     * 圆心的y坐标
     */
    private int mViewCenterY;
    /**
     * 内圆的半径
     */
    private int mInsideRadius = 0;
    /**
     * 圆环的矩形区域
     */
    private RectF mRectF;
    private int trackColor = -1;
    private int thumbColor = -1;
    private int centerColor = -1;
    private int textColor = -1;
    private int textSize = 18;
    private float maxProgress = 100.0f;
    private float mCurrentProgress = 0;
    /**
     * 进度数值保留多少位小数
     */
    private int textScale = 0;
    /**
     * 是否需要动画
     */
    private boolean hasNeedAnim = true;

    /**
     * 动画执行时间
     */
    private int duration = 1000;

    private ValueAnimator valueAnimator;

    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        mRingWidth = a.getInt(R.styleable.CircleProgressBar_ringWidth, 10);
        trackColor = a.getColor(R.styleable.CircleProgressBar_trackColor, Color.parseColor("#FFF3F2"));
        thumbColor = a.getColor(R.styleable.CircleProgressBar_thumbColor, Color.parseColor("#E94222"));
        centerColor = a.getColor(R.styleable.CircleProgressBar_centerColor, Color.parseColor("#FFFFFF"));
        textColor = a.getColor(R.styleable.CircleProgressBar_centerTextColor, Color.parseColor("#2C3542"));
        textSize = a.getDimensionPixelSize(R.styleable.CircleProgressBar_centerTextSize, DisplayUtil.sp2px(mContext, 16));
        mCurrentProgress = a.getInt(R.styleable.CircleProgressBar_progress, 0);
        a.recycle();
        initPaint();
    }

    private void initPaint() {
        mTrackPaint = new Paint();
        mTrackPaint.setAntiAlias(true);
        mTrackPaint.setStyle(Paint.Style.STROKE);
        mTrackPaint.setStrokeCap(Paint.Cap.ROUND);
        mTrackPaint.setStrokeWidth(mRingWidth);
        mTrackPaint.setColor(trackColor);

        mThumbPaint = new Paint();
        mThumbPaint.setAntiAlias(true);
        mThumbPaint.setStyle(Paint.Style.STROKE);
        mThumbPaint.setStrokeCap(Paint.Cap.ROUND);
        mThumbPaint.setStrokeWidth(mRingWidth);
        mThumbPaint.setColor(thumbColor);

        mCenterCirclePaint = new Paint();
        mCenterCirclePaint.setAntiAlias(true);
        mCenterCirclePaint.setColor(centerColor);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        this.setWillNotDraw(false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        mViewCenterX = width / 2;
        mViewCenterY = height / 2;
        mInsideRadius = (int) (mViewCenterX - mRingWidth);
        //画矩形
        mRectF = new RectF(mViewCenterX - mInsideRadius - mRingWidth / 2, mViewCenterY - mInsideRadius - mRingWidth / 2, mViewCenterX + mInsideRadius + mRingWidth / 2, mViewCenterY + mInsideRadius + mRingWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawCircle(mViewCenterX, mViewCenterY, mInsideRadius, mCenterCirclePaint);
        drawTrack(canvas);
        drawThumb(canvas);
        drawText(canvas);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                event.getY();
                event.getX();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnim(mCurrentProgress);
    }

    public CircleProgressBar with() {
        return CircleProgressBar.this;
    }

    /**
     * 画进度条
     *
     * @param canvas
     */
    private void drawThumb(Canvas canvas) {
        int sweepAngle = (int) (360 * mCurrentProgress / 100);
        canvas.drawArc(mRectF, -90, sweepAngle, false, mThumbPaint);
    }

    private void drawText(Canvas canvas) {
        Paint.FontMetricsInt metrics = mTextPaint.getFontMetricsInt();
        float y = mViewCenterY + ((Math.abs(metrics.ascent) + metrics.descent) / 2 - metrics.descent);
        String textValue = (textScale > 0 ? mCurrentProgress : Math.round(mCurrentProgress) + "") + "%";
        canvas.drawText(textValue, mViewCenterX, y, mTextPaint);
    }

    /**
     * 画背景轴道
     *
     * @param canvas
     */
    private void drawTrack(Canvas canvas) {
        canvas.drawArc(mRectF, 360, 360, false, mTrackPaint);
    }

    private void startAnim(float currentProgress) {
        valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(0, currentProgress);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setEvaluator(null);
        valueAnimator.removeAllUpdateListeners();
        ValueAnimator.AnimatorUpdateListener listener = animation -> {
            mCurrentProgress = NumberUtil.formatFloatPoint((Float) animation.getAnimatedValue(), textScale);
            Log.i("mCurrentProgress", Math.round(mCurrentProgress) + "-------------" + animation.getAnimatedValue() + "");
            invalidate();
            postInvalidate();
        };
        valueAnimator.addUpdateListener(listener);
        valueAnimator.setTarget(null);
        valueAnimator.start();
    }

    /**
     * 设置进度文字保留小数位
     *
     * @param textScale
     */
    public void setTextScale(int textScale) {
        this.textScale = textScale;
    }

    /**
     * 设置当前进度
     *
     * @param currentProgress
     */
    public void setCurrentProgress(float currentProgress) {
        this.mCurrentProgress = currentProgress;
        invalidate();
    }

    /**
     * 设置动画执行时间
     *
     * @param duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /*以下是链式调用方法*/

    /**
     * 设置进度文字保留小数位
     *
     * @param textScale
     */
    public CircleProgressBar setBuilderTextScale(int textScale) {
        this.textScale = textScale;
        return CircleProgressBar.this;
    }

    /**
     * 设置当前进度
     *
     * @param currentProgress
     */
    public CircleProgressBar setBuilderCurrentProgress(float currentProgress) {
        this.mCurrentProgress = currentProgress;
        invalidate();
        return CircleProgressBar.this;
    }

    /**
     * 设置动画执行时间
     *
     * @param duration
     */
    public CircleProgressBar setBuilderDuration(int duration) {
        this.duration = duration;
        return CircleProgressBar.this;
    }
}
