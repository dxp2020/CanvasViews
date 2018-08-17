package com.changf.canvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Bezier extends View {
    private int contentWidth = dp2px(200);
    private int centerX;
    private int centerY;
    private Point start = new Point();
    private Point end = new Point();
    private Point control = new Point();

    public Bezier(Context context) {
        this(context,null);
    }

    public Bezier(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getPaddingLeft()+contentWidth+getPaddingRight(),
                getPaddingTop()+contentWidth+getPaddingBottom());
        centerX = getMeasuredWidth()/2;
        centerY = getMeasuredHeight()/2;
        //起点位置
        start.x = centerX-200;
        start.y = centerY;
        //终点位置
        end.x = centerX+200;
        end.y = centerY;
        //控制点位置
        control.x = centerX;
        control.y = centerY-200;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        control.x = (int) event.getX();
        control.y = (int) event.getY();
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStrokeWidth(20);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);

        drawPoints(canvas,paint);
        drawLines(canvas,paint);
        drawBezier(canvas,paint);
    }

    private void drawLines(Canvas canvas, Paint paint) {
        paint.setStrokeWidth(6);
        canvas.drawLine(start.x,start.y,control.x,control.y,paint);
        canvas.drawLine(end.x,end.y,control.x,control.y,paint);
    }

    private void drawBezier(Canvas canvas, Paint paint) {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(6);

        Path path = new Path();
        path.moveTo(start.x,start.y);
        path.quadTo(control.x,control.y,end.x,end.y);
        canvas.drawPath(path,paint);
    }

    private void drawPoints(Canvas canvas, Paint paint) {
        canvas.drawPoint(start.x,start.y,paint);
        canvas.drawPoint(end.x,end.y,paint);
        canvas.drawPoint(control.x,control.y,paint);
    }

    /**
     * dp转换成px
     */
    private int dp2px(float dpValue){
        float scale=getContext().getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
}
