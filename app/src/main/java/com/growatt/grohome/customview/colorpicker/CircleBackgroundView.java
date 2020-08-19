package com.growatt.grohome.customview.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CircleBackgroundView extends View {


    private Paint mPaint;

    public CircleBackgroundView(Context context) {
        this(context, null);
    }

    public CircleBackgroundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public CircleBackgroundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = getWidth() / 2;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);
    }

    /**
     * 刷新颜色zhi
     *
     * @param color
     */
    public void setColor(int color) {
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


    public void setDrawable(){

    }

    public void refresh() {
    }
}
