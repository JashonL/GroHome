package com.growatt.grohome.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.growatt.grohome.R;

public class WeekSelectView extends FrameLayout implements View.OnClickListener {

    private ConstraintLayout rlParent;
    private TextView tvText;
    private ImageView ivSelected;

    private boolean ischecked = false;//是否勾选
    private IselectChangeListener iselectChangeListener;


    public WeekSelectView(@NonNull Context context) {
        this(context, null);
    }

    public WeekSelectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekSelectView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
        initAtrr(context, attrs);
    }



    private void initViews(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_week_selector, null);
        rlParent = view.findViewById(R.id.cl_parent);
        tvText = view.findViewById(R.id.tvText);
        ivSelected = view.findViewById(R.id.ivSelect);
        rlParent.setOnClickListener(this);
        addView(view);
    }


    private void initAtrr(Context context, AttributeSet attrs) {
        //默认文字大小
        float defaultTextSize = context.getResources().getDimensionPixelSize(R.dimen.size_content_sp_14);
        //默认文字颜色
        int defaultTextColor = ContextCompat.getColor(context, R.color.color_text_33);


        //获取属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeekSelectView);
        String text = typedArray.getString(R.styleable.WeekSelectView_itemGroup_textContent);
        int textColor = typedArray.getColor(R.styleable.WeekSelectView_itemGroup_textColor, defaultTextColor);
        float textSize = typedArray.getDimension(R.styleable.WeekSelectView_itemGroup_textSize, defaultTextSize);
        ischecked = typedArray.getBoolean(R.styleable.WeekSelectView_itemGroup_ischecked, false);
        int paddingLeft = (int) typedArray.getDimension(R.styleable.WeekSelectView_itemGroup_paddingLeft, 0);
        int paddingRight = (int) typedArray.getDimension(R.styleable.WeekSelectView_itemGroup_paddingRight, 0);
        int paddingTop = (int) typedArray.getDimension(R.styleable.WeekSelectView_itemGroup_paddingTop, 0);
        int paddingBottom = (int) typedArray.getDimension(R.styleable.WeekSelectView_itemGroup_paddingBottom, 0);
        typedArray.recycle();
        //初始化控件
        rlParent.setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
        tvText.setText(text);
        tvText.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        tvText.setTextColor(textColor);
        ivSelected.setImageResource(ischecked ? R.drawable.scenes_selected : R.drawable.scenes_unselected);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cl_parent) {
            if (ischecked) {
                iselectChangeListener.selectedListen(false);
            } else {
                iselectChangeListener.selectedListen(true);
            }
        }
    }


    public void setIschecked(boolean ischecked){
        this.ischecked=ischecked;
        ivSelected.setImageResource(ischecked ? R.drawable.scenes_selected : R.drawable.scenes_unselected);
    }

    public boolean isIschecked(){
        return ischecked;
    }

    public void setIselectChangeListener(IselectChangeListener listener){
        this.iselectChangeListener=listener;
    }

    public interface IselectChangeListener{
        void selectedListen(boolean isChecked);
    }
}
