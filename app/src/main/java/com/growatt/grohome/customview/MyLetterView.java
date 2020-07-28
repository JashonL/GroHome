package com.growatt.grohome.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class MyLetterView extends View {

    public static String[] letters = {
            "#",
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private Paint paint;

    /*字母背景画笔*/
    private Paint bgPaint;

    private OnTouchLetterListener listener;

    public void setOnTouchLetterListener(OnTouchLetterListener listener) {
        this.listener = listener;
    }

    public MyLetterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11, getResources().getDisplayMetrics()));
        bgPaint =new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight() / letters.length;
        int width = getWidth();

        for (int i = 0; i < letters.length; i++) {
            Rect bounds = new Rect();
            paint.getTextBounds(letters[i], 0, letters[i].length(), bounds);
            float x = width / 2 - bounds.width() / 2;
            float y = height / 2 + bounds.height() / 2 + height * i;
            canvas.drawText(letters[i], x, y, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float yPos = event.getY();
                int idx = (int) ((yPos * letters.length) / getHeight());
                if (idx >= 0 && idx < letters.length) {
                    if (listener != null) {
                        listener.onTouchLetter(letters[idx]);
                    }
                }

                break;

            default:
                if (listener != null) {
                    listener.onRelaseLetter();
                }
                break;
        }

        return true;
    }

    public static String[] getLetters() {
        return letters;
    }

    public static void setLetters(String[] letters) {
        MyLetterView.letters = letters;
    }

    public interface OnTouchLetterListener {
        void onTouchLetter(String letter);

        void onRelaseLetter();
    }

}
