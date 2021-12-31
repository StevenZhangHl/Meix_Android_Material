package com.meix.library.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.meix.library.R;

/**
 * author steven
 * date 2021/12/27
 * description 3D特效
 */
public class ThreeDView extends AppCompatImageView {
    private Paint paint;
    private int radius;
    private Bitmap bitmap;

    public ThreeDView(Context context) {
        super(context);
        init(context);
    }

    public ThreeDView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ThreeDView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAlpha(100);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_girl);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        Matrix matrix = new Matrix();
        canvas.drawBitmap(bitmap, getWidth() / 2 - bitmap.getWidth() / 2, 0, paint);
        Camera camera = new Camera();
        camera.save();
        camera.rotate(0,radius,0);
        camera.getMatrix(matrix);
        camera.applyToCanvas(canvas);
        matrix.preTranslate(-getWidth() / 2 , -bitmap.getHeight() / 2);
        matrix.postTranslate(getWidth() / 2 , bitmap.getHeight() / 2);
        canvas.setMatrix(matrix);
        camera.restore();
        super.onDraw(canvas);
        canvas.restore();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        postInvalidate();
    }
}
