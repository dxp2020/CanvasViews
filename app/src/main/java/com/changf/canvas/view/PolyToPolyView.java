package com.changf.canvas.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.changf.canvas.R;

public class PolyToPolyView extends View {
    private Bitmap mBitmap;             // 要绘制的图片
    private Matrix mPolyMatrix;         // 测试setPolyToPoly用的Matrix
    private Paint pointPaint;
    private int testPoint = 4;
    private int triggerRadius = 180;    // 触发半径为180px
    private float outWidth;
    private float outHeight;
    private float[] src = new float[8];
    private float[] dst = new float[8];

    public PolyToPolyView(Context context) {
        this(context,null);
    }

    public PolyToPolyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.meinv);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        outWidth = MeasureSpec.getSize(widthMeasureSpec);
        outHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension((int)(getPaddingLeft()+outWidth+getPaddingRight()),(int)(getPaddingTop()+outHeight+getPaddingBottom()));
    }

    private void initBitmapAndMatrix() {
        float[] temp = {0, 0,                                    // 左上
                mBitmap.getWidth(), 0,                          // 右上
                mBitmap.getWidth(), mBitmap.getHeight(),        // 右下
                0, mBitmap.getHeight()};                        // 左下
        src = temp.clone();
        dst = temp.clone();

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStrokeWidth(50);
        pointPaint.setColor(0xffd19165);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);

        mPolyMatrix = new Matrix();
        mPolyMatrix.setPolyToPoly(src, 0, src, 0, 4);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                float tempX = event.getX();
                float tempY = event.getY();

                // 根据触控位置改变dst
                for (int i=0; i<testPoint*2; i+=2 ) {
                    if (Math.abs(tempX - dst[i]) <= triggerRadius && Math.abs(tempY - dst[i+1]) <= triggerRadius){
                        dst[i]   = tempX-100;
                        dst[i+1] = tempY-100;
                        break;  // 防止两个点的位置重合
                    }
                }

                resetPolyMatrix(testPoint);
                invalidate();
                break;
        }
        return true;
    }

    public void resetPolyMatrix(int pointCount){
        mPolyMatrix.reset();
        // 核心要点
        mPolyMatrix.setPolyToPoly(src, 0, dst, 0, pointCount);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(100,100);

        mBitmap = getScaledBitmap();
        if(pointPaint==null){
            initBitmapAndMatrix();
        }
        // 根据Matrix绘制一个变换后的图片
        canvas.drawBitmap(mBitmap, mPolyMatrix, null);

        float[] dst = new float[8];
        mPolyMatrix.mapPoints(dst,src);

        // 绘制触控点
        for (int i=0; i<testPoint*2; i+=2 ) {
            canvas.drawPoint(dst[i], dst[i+1],pointPaint);
        }
    }

    private Bitmap getScaledBitmap() {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.mipmap.meinv);
        int outWidth = dp2px(200);
        int outHeight = outWidth;
        float widthScale = (float)outWidth/src.getWidth();
        float heightScale = widthScale;

        Matrix matrix = new Matrix();
        matrix.setScale(widthScale,heightScale);

        BitmapShader bitmapShader = new BitmapShader(src, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
        bitmapShader.setLocalMatrix(matrix);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

        Bitmap target = Bitmap.createBitmap(outWidth,outHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);

        canvas.drawRect(new Rect(0,0,outWidth,outHeight),paint);

        return target;
    }


    public int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}

