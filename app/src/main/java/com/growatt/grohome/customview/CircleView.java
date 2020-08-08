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

    public static final int CIRCLE_VIEW_TYPE_STROKE=1;//
    public static final int CIRCLE_VIEW_TYPE_SOLID=2;
    public static final int CIRCLE_VIEW_TYPE_NONE=3;


    private int type = CIRCLE_VIEW_TYPE_STROKE;


    private Paint mPaint;
    private Paint mStrokePaint;

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
        mBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
        init();

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.YELLOW);

        mStrokePaint=new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (type){
            case CIRCLE_VIEW_TYPE_STROKE:
                drawCircle_stroke(canvas);
                break;
            case CIRCLE_VIEW_TYPE_SOLID:
                drawCircle_solid(canvas);
                break;
            case CIRCLE_VIEW_TYPE_NONE:
                drawBitmap(canvas);
                break;


        }
    }

    private void drawBitmap(Canvas canvas) {
        if (mBitmap!=null){
            Rect b1 = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
            Rect b = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
            canvas.drawBitmap(mBitmap, b1, b, mPaint);
        }else {
            drawCircle_stroke(canvas);
        }

    }

    private void drawCircle_solid(Canvas canvas) {
        int radius = getWidth() / 2;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);
    }


    private void drawCircle_stroke(Canvas canvas) {
        int radius = getWidth() / 2;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);
        int stroke_width = radius / 10;
        mStrokePaint.setStrokeWidth(stroke_width);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius-stroke_width/2, mStrokePaint);
    }


    /**
     * 绘制类型
     */
    public void setType(int type) {
      this.type=type;
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


    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        invalidate();
    }

    public void refresh() {
    }
}
