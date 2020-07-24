package com.growatt.grohome.customview.colorpicker;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class BlurMaskCircularView extends View {

    private int width;
    private BlurMaskFilter mMaskCircularView;

    private Paint mPaint;
    private int radius = 0;

    public BlurMaskCircularView(Context context) {
        this(context, null);
    }

    public BlurMaskCircularView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public BlurMaskCircularView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        mPaint.setColor(Color.CYAN);
        mMaskCircularView = new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //发光圆，radius是光晕半径
        mPaint.setMaskFilter(mMaskCircularView);
        radius = getWidth() / 2-20;
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

    public void refresh() {
    }
}
