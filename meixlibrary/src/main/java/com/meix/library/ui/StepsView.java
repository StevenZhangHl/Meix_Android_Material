package com.meix.library.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.meix.library.R;
import com.meix.library.utils.DisplayUtil;

/**
 * author steven
 * date 2021/12/23
 * description 步骤条
 */
public class StepsView extends View {
    private Context mContext;
    /**
     * 指定当前步骤，从 0 开始记数。在子 Step 元素中，可以通过 status 属性覆盖状态
     */
    private int current;
    /**
     * 指定步骤条方向。目前支持水平（horizontal）和竖直（vertical）两种方向
     * 暂未支持
     */
    private String direction;
    /**
     * 指定标签放置位置，默认水平放图标右侧，可选 vertical 放图标下方
     * 暂未支持
     */
    private String labelPlacement;
    /**
     * 步骤数量
     */
    private int stepCount;
    /**
     * 点状步骤条（不显示数字角标）
     */
    private boolean progressDot;
    /**
     * 当前进度的显示模式两点之间还是超出当前点一半，默认一半
     *
     * @apiNote "full"，"half"
     */
    private int progressMode;
    /**
     * 进度条背景画笔
     */
    private Paint mBgProgressPaint;
    /**
     * 进度条背景颜色
     */
    private int mBgProgressBarColor;
    /**
     * 当前进度条画笔
     */
    private Paint mThumbProgressPaint;
    /**
     * 当前进度条颜色
     */
    private int mThumbProgressBarColor;
    /**
     * 进度条的高度
     */
    private int progressBarHeight;

    /**
     * 原点的画笔
     */
    private Paint mPointPaint;
    /**
     * 原点中文字的颜色
     */
    private Paint mPointTextPaint;
    /**
     * 底部坐标文字
     */
    private Paint mBottomAxisPaint;
    private int mViewWidth;
    private int mViewHeight;
    /**
     * 底部坐标和上面的距离
     */
    private int mBottomAxisToTop;
    /**
     * 每段的进度的宽度
     */
    private int mItemLineWidth;
    /**
     * 每个原点的半径
     */
    private int mPointRadius;
    /**
     * 底部第一个坐标的宽度
     */
    private float mFirstTextWidth;
    /**
     * 底部最后一个坐标的宽度
     */
    private float mLastTextWidth;
    /**
     * 当前进度（执行动画时用）
     */
    private float mAnimProgress;
    private boolean hasAnim = true;
    private boolean hasCanAnim = true;
    private boolean hasAnimLoading = false;
    /**
     * 动画执行时间
     */
    private int duration ;

    private ValueAnimator valueAnimator;
    private String titles[] = new String[]{"提交申请", "竞选投票", "当选代言人"};

    public StepsView(Context context) {
        super(context);
        init(context, null);
    }

    public StepsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StepsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            mViewWidth = w;
        }
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.StepsView);
        direction = typedArray.getString(R.styleable.StepsView_step_direction);
        mBgProgressBarColor = typedArray.getColor(R.styleable.StepsView_bg_progressBar_color, Color.parseColor("#f5f8f8"));
        mThumbProgressBarColor = typedArray.getColor(R.styleable.StepsView_thumb_progressBar_Color, Color.parseColor("#498AE6"));
        mPointRadius = typedArray.getDimensionPixelSize(R.styleable.StepsView_point_radius, DisplayUtil.dip2px(context, 10));
        progressBarHeight = typedArray.getDimensionPixelSize(R.styleable.StepsView_progressBarHeight, DisplayUtil.dip2px(context, 3));
        mBottomAxisToTop = typedArray.getDimensionPixelSize(R.styleable.StepsView_bottom_axis_to_top, DisplayUtil.dip2px(context, 10));
        progressMode = typedArray.getInt(R.styleable.StepsView_progressMode, 0);
        current = typedArray.getInt(R.styleable.StepsView_current_step, 0);
        progressDot = typedArray.getBoolean(R.styleable.StepsView_progress_dot, false);
        duration = typedArray.getInt(R.styleable.StepsView_anim_duration,800);
        stepCount = titles.length;
        initPaint();
    }

    private void initPaint() {
        mBgProgressPaint = new Paint();
        mBgProgressPaint.setAntiAlias(true);
        mBgProgressPaint.setStyle(Paint.Style.STROKE);
        mBgProgressPaint.setStrokeWidth(progressBarHeight);
        mBgProgressPaint.setColor(mBgProgressBarColor);

        mThumbProgressPaint = new Paint();
        mThumbProgressPaint.setAntiAlias(true);
        mThumbProgressPaint.setStyle(Paint.Style.STROKE);
        mThumbProgressPaint.setStrokeWidth(progressBarHeight);
        mThumbProgressPaint.setColor(mThumbProgressBarColor);

        mPointPaint = new Paint();
        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(mThumbProgressBarColor);

        mPointTextPaint = new Paint();
        mPointTextPaint.setColor(Color.parseColor("#ffffff"));
        mPointTextPaint.setAntiAlias(true);
        mPointTextPaint.setTextAlign(Paint.Align.CENTER);
        mPointTextPaint.setTextSize(DisplayUtil.sp2px(mContext, 10));

        mBottomAxisPaint = new Paint();
        mBottomAxisPaint.setColor(Color.parseColor("#666666"));
        mBottomAxisPaint.setAntiAlias(true);
        mBottomAxisPaint.setTextAlign(Paint.Align.CENTER);
        mBottomAxisPaint.setTextSize(DisplayUtil.sp2px(mContext, 12));
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int allTextWidth = 0;
        for (int i = 0; i < titles.length - 1; i++) {
            allTextWidth += (int) (mBottomAxisPaint.measureText(titles[i]));
        }
        //保证底部文本能显示完整的最小宽度
        int result = allTextWidth + DisplayUtil.dip2px(mContext, 25 * 3);
        if (specMode == MeasureSpec.EXACTLY) {
            if (specSize > result) {
                result = specSize;
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        Rect rect = new Rect();
        mPointTextPaint.getTextBounds(titles[0], 0, titles[0].length(), rect);
        int h = rect.height();
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = mPointRadius * 2 + mBottomAxisToTop + h;
        if (specMode == MeasureSpec.EXACTLY) {//相当于我们设置为match_parent或者为一个具体的值
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (stepCount <= 1) {
            new NoSuchFieldException("进度长度至少要大于等于2");
            return;
        }
        if (mViewWidth > 0) {
            mFirstTextWidth = mBottomAxisPaint.measureText(titles[0]);
            mLastTextWidth = mBottomAxisPaint.measureText(titles[stepCount - 1]);
            mItemLineWidth = (int) ((mViewWidth - mFirstTextWidth / 2 - mLastTextWidth / 2) / (stepCount - 1));
            drawBgProgress(canvas);
            drawThumbProgress(canvas);
            drawCirclePoint(canvas);
            drawBottomAxis(canvas);
        }
        canvas.restore();
    }

    private void drawCirclePoint(Canvas canvas) {
        float cx;
        float cy = mPointRadius;
        Paint.FontMetricsInt metrics = mPointTextPaint.getFontMetricsInt();
        float y = cy + ((Math.abs(metrics.ascent) + metrics.descent) / 2 - metrics.descent);
        for (int i = 0; i < stepCount; i++) {
            cx = mFirstTextWidth / 2 + mItemLineWidth * i;
            canvas.drawCircle(cx, cy, mPointRadius, mPointPaint);
            if (!progressDot) {
                canvas.drawText(String.valueOf(i + 1), cx, y, mPointTextPaint);
            }
        }
    }

    private void drawBgProgress(Canvas canvas) {
        canvas.drawLine(mFirstTextWidth / 2, mPointRadius, mViewWidth - mLastTextWidth / 2, mPointRadius, mBgProgressPaint);
    }

    private void drawThumbProgress(Canvas canvas) {
        float startX = mFirstTextWidth / 2;
        float startY = mPointRadius;
        float endX = 0;
        float endY = mPointRadius;
        float oldX = mFirstTextWidth / 2;
        if (progressMode == 0) {
            if (current == 0) {
                endX = startX;
            } else {
                if (current > 0 && current < stepCount - 1) {
                    endX = mFirstTextWidth / 2 + mItemLineWidth * current;
                }
                oldX = mFirstTextWidth / 2 + mItemLineWidth * (current - 1);
            }
        } else {
            if (current == 0) {
                endX = mFirstTextWidth / 2 + mItemLineWidth / 2;
            } else {
                if (current > 0 && current < stepCount - 1) {
                    endX = mFirstTextWidth / 2 + mItemLineWidth * current + mItemLineWidth / 2;
                }
                oldX = mFirstTextWidth / 2 + mItemLineWidth * (current - 1) + mItemLineWidth / 2;
            }
        }
        //最后一个不区分模式，特殊处理
        if (current == stepCount - 1) {
            endX = mViewWidth - mLastTextWidth / 2;
        }
        if (hasAnim) {
            if (hasCanAnim && !hasAnimLoading) {
                startAnim(oldX, endX);
            }
        } else {
            mAnimProgress = endX;
        }
        canvas.drawLine(startX, startY, mAnimProgress, endY, mThumbProgressPaint);
    }

    /**
     * 底部坐标文字
     *
     * @param canvas
     */
    private void drawBottomAxis(Canvas canvas) {
        float cx = 0;
        float cy = mPointRadius * 2 + mBottomAxisToTop;
        Paint.FontMetricsInt metrics = mPointTextPaint.getFontMetricsInt();
        float y = cy + ((Math.abs(metrics.ascent) + metrics.descent) / 2 - metrics.descent);
        for (int i = 0; i < stepCount; i++) {
            float textWidth = mBottomAxisPaint.measureText(titles[i]);
            cx = textWidth / 2;
            if (i > 0 && i < stepCount - 1) {
                cx = mBottomAxisPaint.measureText(titles[0]) / 2 + mItemLineWidth * i;
            }
            if (i == stepCount - 1) {
                cx = mViewWidth - textWidth / 2;
            }
            canvas.drawText(titles[i], cx, y, mBottomAxisPaint);
        }
    }

    private void startAnim(float oldX, float endX) {
        hasAnimLoading = true;
        valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(oldX, endX);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setEvaluator(null);
        valueAnimator.removeAllUpdateListeners();
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                hasAnimLoading = false;
                hasCanAnim = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        ValueAnimator.AnimatorUpdateListener listener = animation -> {
            mAnimProgress = (float) animation.getAnimatedValue();
            invalidate();
        };
        valueAnimator.addUpdateListener(listener);
        valueAnimator.setTarget(null);
        valueAnimator.start();
    }

    public void setCurrent(int current) {
        this.current = current;
        if (current > stepCount - 1) {
            Toast.makeText(mContext, "已经到最后一步了", Toast.LENGTH_SHORT).show();
            return;
        }
        hasCanAnim = true;
        invalidate();
    }

    public int getCurrent() {
        return current;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            valueAnimator.cancel();
        }
    }
}
