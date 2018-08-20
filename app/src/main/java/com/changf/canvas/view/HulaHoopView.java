package com.changf.canvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class HulaHoopView extends View {

    private int outWidth;
    private int outHeight;
    private float radius;//圆角半径

    public HulaHoopView(Context context) {
        this(context,null);
    }

    public HulaHoopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultWidthSize(widthMeasureSpec);
        int height = getDefaultHeightSize(heightMeasureSpec);
        outWidth = outHeight = Math.min(width,height);
        radius = outWidth/2;

        setMeasuredDimension(getPaddingLeft()+outWidth+getPaddingRight(),getPaddingTop()+outHeight+getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth()/2,getHeight()/2);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(0,0,radius,paint);
        canvas.drawCircle(0,0,radius-20,paint);

        for(int i=0;i<=360;i+=10){
            canvas.drawLine(radius-20,0,radius,0,paint);
            canvas.rotate(10);
        }
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

