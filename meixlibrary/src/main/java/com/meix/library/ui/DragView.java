package com.meix.library.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

/**
 * author steven
 * date 2021/12/14
 * description 任意可以拖动的view
 */
public class DragView extends RelativeLayout {
    /**
     * 要拖动的view
     */
    private View mDragView;
    private ViewDragHelper viewDragHelper;
    private Context mContext;

    /**
     * 是否正在拖动
     */
    private boolean mDragging = false;
    /**
     * 距离顶部的距离
     */
    private int mTop;
    /**
     * 距离左侧的距离
     */
    private int mLeft;

    /**
     * 拖动结束后是否保持在当前位置
     */
    private boolean hasNeedKeep = false;
    /**
     * 拖动结束后是否隐藏一半到屏幕外面（hasNeedKeep=false时生效）
     */
    private boolean hasHideSelfHalf = false;

    /**
     * 是否可以滑动到屏幕外边
     */
    private boolean hasCanScrollOutSide = false;

    public DragView(Context context) {
        super(context);
        init(context);
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(Context context) {
        this.mContext = context;
        viewDragHelper = ViewDragHelper.create(this, 1f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                return child == mDragView;
            }

            @Override
            public void onViewDragStateChanged(int state) {
                if (state == ViewDragHelper.STATE_DRAGGING) {
                    mDragging = true;
                } else {
                    mDragging = false;
                }
                super.onViewDragStateChanged(state);
            }

            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (releasedChild == mDragView) {
                    if (hasNeedKeep) {
                        return;
                    }
                    mTop = releasedChild.getTop();
                    if ((releasedChild.getLeft() + releasedChild.getMeasuredWidth() / 2) > getMeasuredWidth() / 2) {
                        if (hasHideSelfHalf) {
                            mLeft = getMeasuredWidth() - releasedChild.getMeasuredWidth() / 2;
                        } else {
                            mLeft = getMeasuredWidth() - releasedChild.getMeasuredWidth();
                        }
                    } else {
                        if (hasHideSelfHalf) {
                            mLeft = -releasedChild.getMeasuredWidth() / 2;
                        } else {
                            mLeft = 0;
                        }
                    }
                    viewDragHelper.smoothSlideViewTo(releasedChild, mLeft, mTop);
                    ViewCompat.postInvalidateOnAnimation(DragView.this);
                }
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                if (left < 0 && !hasCanScrollOutSide) {
                    left = 0;
                } else if (left > getMeasuredWidth() - child.getMeasuredWidth() && !hasCanScrollOutSide) {
                    left = getMeasuredWidth() - child.getMeasuredWidth();
                }
                return left;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                if (top < 0 && !hasCanScrollOutSide) {
                    top = 0;
                } else if (top > getMeasuredHeight() - child.getMeasuredHeight()&& !hasCanScrollOutSide) {
                    top = getMeasuredHeight() - child.getMeasuredHeight();
                }
                return top;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return mDragging;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(DragView.this);
        }
    }


    /**
     * 设置拖动的view
     *
     * @param mDragView
     */
    public void setDragView(View mDragView) {
        this.mDragView = mDragView;
    }

    /**
     * 设置边界显示一半
     *
     * @param hideSelfHalf
     */
    public void setHasHideSelfHalf(boolean hideSelfHalf) {
        hasHideSelfHalf = hideSelfHalf;
    }

    public void setHasNeedKeep(boolean hasNeedKeep) {
        this.hasNeedKeep = hasNeedKeep;
    }

    public void setHasCanScrollOutSide(boolean hasCanScrollOutSide) {
        this.hasCanScrollOutSide = hasCanScrollOutSide;
    }
}
