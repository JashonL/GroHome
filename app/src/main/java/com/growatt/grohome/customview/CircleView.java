package com.growatt.grohome.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.growatt.grohome.R;

public class CircleView extends View {

    private int type = 2;

    private Paint mPaint;

    private Bitmap mBitmap;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBitmap(canvas);
     /*   if (type == 1) {
            drawCircle(canvas);
        } else {
            drawBitmap(canvas);
        }*/
    }

    private void drawBitmap(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.colour_n);
        Rect b1 = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect b = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bitmap, b1, b, mPaint);

      /*  if (mBitmap != null) {
            Rect b1 = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
            Rect b = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
            canvas.drawBitmap(mBitmap, b1, b, mPaint);
        } else {
            drawCircle(canvas);
        }*/
    }

    private void drawCircle(Canvas canvas) {
        int radius = getWidth() / 2;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);
    }

    /**
     * 刷新颜色zhi
     *
     * @param color
     */
    public void setColor(int color) {
        type = 1;
        mPaint.setColor(color);
        invalidate();
    }

    /**
     * 取当前颜色
     *
     * @return 颜色值
     */
    public int getColor() {
        return mPaint.getColor();
    }


    public void setBitmap(Bitmap bitmap) {
        type = 2;
        mBitmap = bitmap;
        invalidate();
    }

    public void refresh() {
    }
}
