package com.meix_android_material.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

import com.meix.library.utils.DisplayUtil;


/**
 * author steven
 * date 2021/12/21
 * description
 */
public class DoodleView extends AppCompatImageView {
    private Context mContext;
    private Paint mPathPaint;
    private Paint mMosaicPaint;
    private float startX, startY;
    private float moveX, moveY;
    private boolean hasDown = false;
    private boolean hasMove = false;
    private Paint mBitmapPaint;
    private Bitmap mOriginBitmap;
    private int mWidth, mHeight, centerX, centerY;
    private int mViewHeight;
    private float mScale;//图片的比例
    private float rotate = 0;//旋转角度
    private int top = 0;
    private boolean hasEdit;

    public interface FinishDrawListener {
        void finish();
    }

    private FinishDrawListener finishDrawListener;

    public void setFinishDrawListener(FinishDrawListener finishDrawListener) {
        this.finishDrawListener = finishDrawListener;
    }

    public DoodleView(Context context) {
        super(context);
    }

    public DoodleView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);
    }

    public DoodleView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        setMode(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            mWidth = w;
            mHeight = h;
            centerX = mWidth / 2;
            centerY = mHeight / 2;
            initOriginBitmap();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        // 画原始图
        if (mOriginBitmap != null) {
            top = 0;
            if (mHeight > mViewHeight) {
                top = (mHeight - mViewHeight) / 2;
            }
            Matrix matrixRotate = new Matrix();
            matrixRotate.postRotate(rotate, centerX, centerY);
            Matrix matrixTranslate = new Matrix();
            matrixTranslate.postTranslate(0, top);
            Matrix matrix = new Matrix();
            matrix.setConcat(matrixRotate, matrixTranslate);
            float values[]=new float[9];
            matrix.getValues(values);
            Matrix matrixScale = new Matrix();
            matrixScale.postScale(1f, 1f, centerX, centerY - top);
            matrix.postConcat(matrixScale);
            canvas.drawBitmap(mOriginBitmap, matrix, mBitmapPaint);
        }
        if (hasEdit) {
            drawPath(canvas);
        }
        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                hasEdit = true;
                hasDown = true;
                hasMove = false;
                startX = event.getX();
                startY = event.getY();
                moveX = event.getX();
                moveY = event.getY();
                postInvalidate();
                Log.i("dispatchTouchEvent", "dispatchTouchEvent: ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                hasMove = true;
                moveX = event.getX();
                moveY = event.getY();
                if (Math.abs(startX - moveY) > 50 || Math.abs(startY - moveY) > 50) {
                    hasDown = false;
                    postInvalidate();
                    Log.i("dispatchTouchEvent", "dispatchTouchEvent: ACTION_MOVE");
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                moveX = event.getX();
                moveY = event.getY();
                if (finishDrawListener != null) {
                    finishDrawListener.finish();
                }
                Log.i("dispatchTouchEvent", "dispatchTouchEvent: ACTION_UP");
                break;
        }
        return true;
    }

    private Path path = new Path();

    private void drawPath(Canvas canvas) {
        if (hasDown) {
            path.moveTo(startX, startY);
            Log.i("dispatchTouchEvent", "moveTo: 执行了");
        }
        path.lineTo(moveX, moveY);
        canvas.drawPath(path, mPathPaint);
        Log.i("dispatchTouchEvent", "startX: " + startX + "startY:" + startY + "moveX:" + moveX + "moveY:" + moveY);
    }

    private void drawMosaicPath(Canvas canvas) {

    }

    public void setMode(int mode) {
        switch (mode) {
            case 1:
                mPathPaint = new Paint();
                mPathPaint.setStyle(Paint.Style.STROKE);
                mPathPaint.setStrokeCap(Paint.Cap.ROUND);
                mPathPaint.setAntiAlias(true);
                mPathPaint.setStrokeWidth(DisplayUtil.dip2px(mContext, 5));
                mPathPaint.setColor(Color.parseColor("#E94222"));
                mPathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                break;
            case 2:
                mMosaicPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mMosaicPaint.setFilterBitmap(false);
                mMosaicPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                break;
        }
    }

    private void saveCanvas(Canvas canvas) {
    }

    /**
     * 设置原始的截图
     *
     * @param originBitmap drawable
     */
    public void setOriginBitmap(Bitmap originBitmap) {
        mOriginBitmap = originBitmap;
        mScale = (float) originBitmap.getWidth() / originBitmap.getHeight();
        initOriginBitmap();
    }

    private void initOriginBitmap() {
        if (mOriginBitmap != null && mWidth > 0 && mHeight > 0) {
            mViewHeight = (int) (mWidth / mScale);
            mOriginBitmap = Bitmap.createScaledBitmap(mOriginBitmap, mWidth, mViewHeight, true);
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            mBitmapPaint.setColor(Color.parseColor("#ffffff"));
            postInvalidate();
        }
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
        invalidate();
    }
}
