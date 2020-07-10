package com.growatt.grohome.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.growatt.grohome.R;


/**
 * Created by Administrator on 2019/12/6.
 */

public class ScaleConstraintLayout extends ConstraintLayout {
    private Context context;
    public ScaleConstraintLayout(Context context) {
        super(context);
        this.context=context;
    }

    public ScaleConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public ScaleConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                beginScale(R.anim.scale_in);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                beginScale(R.anim.scale_out);
                break;
            case MotionEvent.ACTION_CANCEL:
                beginScale(R.anim.scale_out);
                break;
        }
        return true;
    }

    private synchronized void beginScale(int animation) {
        Animation an = AnimationUtils.loadAnimation(context, animation);
        an.setDuration(80);
        an.setFillAfter(true);
        this.startAnimation(an);
    }


}
