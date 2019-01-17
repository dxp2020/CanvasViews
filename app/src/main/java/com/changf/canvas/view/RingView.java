package com.changf.canvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

public class RingView extends View {
    private int outWidth;
    private int outHeight;
    private int ringWidth;
    private Paint paint;
    //圆环颜色
    private int[] colors = new int[]{Color.TRANSPARENT, Color.WHITE};

    public RingView(Context context) {
        this(context,null);
    }

    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setStrokeWidth(ringWidth);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultWidthSize(widthMeasureSpec);
        int height = getDefaultHeightSize(heightMeasureSpec);
        outWidth = outHeight = Math.min(width,height);
        ringWidth = outWidth/8;
        setMeasuredDimension(outWidth,outHeight);

        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setShader(new SweepGradient(outWidth / 2, outHeight / 2, colors, null));
        RectF rectF = new RectF(ringWidth/2,ringWidth/2,outWidth-ringWidth/2,outHeight-ringWidth/2);
        canvas.drawArc(rectF, 75, 360, false, paint);
    }

    private int getDefaultWidthSize(int widthMeasureSpec) {
        switch (MeasureSpec.getMode(widthMeasureSpec)){
            case MeasureSpec.EXACTLY:
                return MeasureSpec.getSize(widthMeasureSpec);
            default:
                return getScreenWidth();
        }
    }

    private int getDefaultHeightSize(int heightMeasureSpec) {
        switch (MeasureSpec.getMode(heightMeasureSpec)){
            case MeasureSpec.EXACTLY:
                return MeasureSpec.getSize(heightMeasureSpec);
            default:
                return getScreenHeight();
        }
    }


    private int getScreenWidth(){
        return getResources().getDisplayMetrics().widthPixels;
    }

    private int getScreenHeight(){
        return getResources().getDisplayMetrics().heightPixels;
    }
}
