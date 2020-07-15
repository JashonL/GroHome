package com.growatt.grohome.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.growatt.grohome.R;
import com.yechaoa.yutils.YUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guozhk
 * create time on 2018/1/5.
 */

public class NodeProgressView extends View {

    private Context context;

    //node 默认
    private Paint mNodePaint;
    //node 已完成
    private Paint mNodeProgressPaint;

    private Paint mTextPaint;
    private Paint mTextProgressPaint;

    private Paint mLinePaint;
    private Paint mLineProgressPaint;

    private int mNodeColor;
    private int mNodeProgressColor;

    private int mTextColor;
    private int mTextProgressColor;

    private int mLineColor;
    private int mLineProgressColor;


/*    private Bitmap mNodeBitmap;
    private Bitmap mNodeProgressBitmap;*/


    //node 半径
    private int mNodeRadio;
    //节点个数
    private int mNumber;
    private List<Node> nodes;
    private int[] nodeBitmaps = {R.drawable.net_search_visit, R.drawable.net_cloud_visit, R.drawable.net_initialize_visit};
    private int[] progressBitmap = {R.drawable.net_search_visited, R.drawable.net_cloud_visited, R.drawable.net_initialize_visited};
    private String[] nodeDes;


    private int mCurentNode;


    private int mWidth;
    private int mHeight;

    public NodeProgressView(Context context) {
        this(context, null);
    }

    public NodeProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NodeProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setBackgroundColor(Color.WHITE);
        initAttrs(attrs);
        init();
        initData(context);
    }


    private void initAttrs(AttributeSet attrs) {
        TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.nodeProgress);
        mNodeColor = mTypedArray.getColor(R.styleable.nodeProgress_node_color, Color.GRAY);
        mNodeProgressColor = mTypedArray.getColor(R.styleable.nodeProgress_node_progresscolor, ContextCompat.getColor(context, R.color.color_theme_green));

        mTextColor = mTypedArray.getColor(R.styleable.nodeProgress_node_tip, Color.GRAY);
        mTextProgressColor = mTypedArray.getColor(R.styleable.nodeProgress_node_progress_tip, ContextCompat.getColor(context, R.color.color_theme_green));

        mLineColor = mTypedArray.getColor(R.styleable.nodeProgress_node_bar, Color.GRAY);
        mLineProgressColor = mTypedArray.getColor(R.styleable.nodeProgress_node_progress_bar, ContextCompat.getColor(context, R.color.color_theme_green));

        mNumber = mTypedArray.getInt(R.styleable.nodeProgress_node_num, 2);
        mCurentNode = mTypedArray.getInt(R.styleable.nodeProgress_node_current, 0);
        mNodeRadio = mTypedArray.getDimensionPixelSize(R.styleable.nodeProgress_node_radio, 16);
     /*   BitmapDrawable drawable = (BitmapDrawable) mTypedArray.getDrawable(R.styleable.nodeProgress_node_bg);
        mNodeBitmap = drawable.getBitmap();
        BitmapDrawable drawable1 = (BitmapDrawable) mTypedArray.getDrawable(R.styleable.nodeProgress_node_progress_bg);
        mNodeProgressBitmap = drawable1.getBitmap();*/


    }

    private void init() {
//        mNodeRadio = MyUtils.dip2px(getContext(), mNodeRadio);

        mNodePaint = new Paint();
        mNodePaint.setAntiAlias(true);
        mNodePaint.setStyle(Paint.Style.FILL);
        mNodePaint.setColor(mNodeColor);

        mNodeProgressPaint = new Paint();
        mNodeProgressPaint.setAntiAlias(true);
        mNodeProgressPaint.setStyle(Paint.Style.FILL);
        mNodeProgressPaint.setColor(mNodeProgressColor);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(YUtils.dp2px(12));

        mTextProgressPaint = new Paint();
        mTextProgressPaint.setAntiAlias(true);
        mTextProgressPaint.setStyle(Paint.Style.STROKE);
        mTextProgressPaint.setColor(mTextProgressColor);
        mTextProgressPaint.setTextAlign(Paint.Align.CENTER);
        mTextProgressPaint.setTextSize(YUtils.dp2px(12));


        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(YUtils.dp2px( 0.5f));
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(mLineColor);

        mLineProgressPaint = new Paint();
        mLineProgressPaint.setAntiAlias(true);
        mLineProgressPaint.setStrokeWidth(YUtils.dp2px(0.5f));
        mLineProgressPaint.setStyle(Paint.Style.FILL);
        mLineProgressPaint.setColor(mLineProgressColor);

    }

    private void initData(Context context) {
        nodes = new ArrayList<>();
        nodeDes = new String[]{context.getString(R.string.m55_find_device), context.getString(R.string.m56_register_cloud), context.getString(R.string.m57_device_initialization)};
        for (int i = 0; i < mNumber; i++) {
            //进度点
            Node node = new Node();
            Point point = new Point(0, 0);
            node.setPoint(point);
            nodes.add(node);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        int d = (mWidth - getPaddingLeft() - getPaddingRight()) / (mNumber - 1);
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            Point point = node.getPoint();
            if (i < nodeBitmaps.length) {
                node.setmNormalRes(nodeBitmaps[i]);
            }
            if (i < progressBitmap.length) {
                node.setmProgreesRes(progressBitmap[i]);
            }

            if (i < nodeDes.length) {
                node.setDes(nodeDes[i]);
            }

            if (i == 0) {
                point.set(getPaddingLeft() + mNodeRadio, mHeight / 2);
            } else if (i == mNumber - 1) {
                point.set(mWidth - getPaddingRight() - mNodeRadio, mHeight / 2);
            } else {
                point.set(getPaddingLeft() + d * i, mHeight / 2);
            }
        }
    }




    private Canvas mCanvas;

    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;
        drawLineProgress(canvas);
        drawNodeProgress(canvas);
        drawTextProgress(canvas);
    }

    public void clear() {
        // mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mCanvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        // setBackgroundColor(Color.WHITE);
        //invalidate();
    }


    private void drawLineProgress(Canvas canvas) {
        int startX = getPaddingLeft() + mNodeRadio * 2;
        for (int i = 0; i < nodes.size() - 1; i++) {
            Node node = nodes.get(i + 1);
            Point point = node.getPoint();
            int x = point.x;
            int y = point.y;
            x = x - mNodeRadio;
            if (mCurentNode - 1 >= i) {
                canvas.drawLine(startX, y, x, y, mLineProgressPaint);
            } else {
                canvas.drawLine(startX, y, x, y, mLinePaint);
            }
            startX = x + mNodeRadio * 2;

        }

    }

    private void drawNodeProgress(Canvas canvas) {
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            Point point = node.getPoint();

            if (mCurentNode - 1 >= i) {//当前进度
                int progreesRes = node.getmProgreesRes();
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), progreesRes);
                if (bitmap != null) {
                    Rect b1 = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                    Rect b = new Rect(point.x - mNodeRadio, point.y - mNodeRadio,
                            point.x + mNodeRadio, point.y + mNodeRadio);
                    canvas.drawBitmap(bitmap, b1, b, mNodePaint);
                } else {
                    canvas.drawCircle(point.x, point.y, mNodeRadio, mNodeProgressPaint);
                }
            } else {
                int normalRes = node.getmNormalRes();
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), normalRes);
                if (bitmap != null) {
                    Rect b1 = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                    Rect b = new Rect(point.x - mNodeRadio, point.y - mNodeRadio,
                            point.x + mNodeRadio, point.y + mNodeRadio);
                    canvas.drawBitmap(bitmap, b1, b, mNodePaint);
                } else {
                    canvas.drawCircle(point.x, point.y, mNodeRadio, mNodePaint);
                }

            }

        }
    }


    private void drawTextProgress(Canvas canvas) {
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            Point point = node.getPoint();
            String text = node.getDes();
            Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
            if (!TextUtils.isEmpty(text)) {
                if (mCurentNode - 1 >= i) {
                    canvas.drawText(text, point.x, point.y + mNodeRadio + fmi.bottom - fmi.top, mTextProgressPaint);
                } else {
                    canvas.drawText(text, point.x, point.y + mNodeRadio + fmi.bottom - fmi.top, mTextPaint);
                }
            }
        }

    }


    public int[] getNodeBitmaps() {
        return nodeBitmaps;
    }

    public void setNodeBitmaps(int[] nodeBitmaps) {
        this.nodeBitmaps = nodeBitmaps;
    }

    public int[] getProgressBitmap() {
        return progressBitmap;
    }

    public void setProgressBitmap(int[] progressBitmap) {
        this.progressBitmap = progressBitmap;
    }

    public String[] getNodeDes() {
        return nodeDes;
    }

    public void setNodeDes(String[] nodeDes) {
        this.nodeDes = nodeDes;
    }

    public void setmNumber(int mNumber) {
        this.mNumber = mNumber;
    }

    public void setCurentNode(int curentNode) {
        this.mCurentNode = curentNode;
        invalidate();
    }

    public void reDraw() {
        clear();
        invalidate();
    }

    class Node {

        private Point point;
        private String des;

        private int mProgreesRes;
        private int mNormalRes;

        public Point getPoint() {
            return point;
        }

        public void setPoint(Point point) {
            this.point = point;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public int getmProgreesRes() {
            return mProgreesRes;
        }

        public void setmProgreesRes(int mProgreesRes) {
            this.mProgreesRes = mProgreesRes;
        }

        public int getmNormalRes() {
            return mNormalRes;
        }

        public void setmNormalRes(int mNormalRes) {
            this.mNormalRes = mNormalRes;
        }
    }


    private void testDraw(Canvas canvas) {


        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(3);
        paint.setTextSize(40);
        Paint.FontMetricsInt fmi = paint.getFontMetricsInt();
        String testString = "测试：ijkJQKA:1234";
        Rect bounds1 = new Rect();
        paint.getTextBounds("测", 0, 1, bounds1);
        Rect bounds2 = new Rect();
        paint.getTextBounds("测试：ijk", 0, 6, bounds2);
        // 随意设一个位置作为baseline
        int x = 10;
        int y = 400;
        // 把testString画在baseline上
        canvas.drawText(testString, x, y, paint);
        // bounds1
        paint.setStyle(Paint.Style.STROKE);  // 画空心矩形
        canvas.save();
        canvas.translate(x, y);  // 注意这里有translate。getTextBounds得到的矩形也是以baseline为基准的
        paint.setColor(Color.GREEN);
        canvas.drawRect(bounds1, paint);
        canvas.restore();

        // bounds2
        canvas.save();
        paint.setColor(Color.MAGENTA);
        canvas.translate(x, y);
        canvas.drawRect(bounds2, paint);
        canvas.restore();

        // baseline
        paint.setColor(Color.RED);
        canvas.drawLine(x, y, 1024, y, paint);
        // ascent
        paint.setColor(Color.YELLOW);
        canvas.drawLine(x, y + fmi.ascent, 1024, y + fmi.ascent, paint);
        // descent
        paint.setColor(Color.BLUE);
        canvas.drawLine(x, y + fmi.descent, 1024, y + fmi.descent, paint);
        // top
        paint.setColor(Color.DKGRAY);
        canvas.drawLine(x, y + fmi.top, 1024, y + fmi.top, paint);
        // bottom
        paint.setColor(Color.GREEN);
        canvas.drawLine(x, y + fmi.bottom, 1024, y + fmi.bottom, paint);
    }

}

