package com.changf.canvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class CirclePointsTransformView extends View {
    private int width;
    private int height;
    private int smallRadius = dp2px(2.5f);//圆点小半径
    private int bigRadius = dp2px(3.5f);;//圆点大半径
    private int status;//0,1,2
    private int interval = dp2px(7);//圆点之间的间隔
    private Paint paint;

    public CirclePointsTransformView(Context context) {
        this(context,null);
    }

    public CirclePointsTransformView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#E5E5E5"));
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(status>2){
                status=0;
            }
            invalidate();
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //3个小圆的直径+2个圆之间间隔的距离+第一个和最后一个大圆减小圆的距离
        width = smallRadius*3*2+2*interval+(bigRadius-smallRadius)*2;
        height = bigRadius*2;
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircles(canvas);
        status++;
        handler.sendEmptyMessageDelayed(0,1000);
    }

    private void drawCircles(Canvas canvas) {
        canvas.translate(bigRadius-smallRadius,0);
        drawCircle(canvas,0);
        drawCircle(canvas,1);
        drawCircle(canvas,2);
    }

    private void drawCircle(Canvas canvas,int index) {
        int radius = smallRadius;
        if(status==index){
            radius = bigRadius;
        }
        canvas.drawCircle((index*2+1)*smallRadius+index*interval,height/2,radius,paint);
    }

    public void startAnima(){
        handler.sendEmptyMessageDelayed(0,1000);
    }

    public void endAnima(){
        handler.removeMessages(0);
    }

    public int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
