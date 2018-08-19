package com.changf.canvas.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import com.changf.canvas.R;

public class RotatingPlaneView extends View {
    private float currentValue = 0;     // 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度
    private float[] pos;                // 当前点的实际位置
    private float[] tan;                // 当前点的tangent值,用于计算图片所需旋转的角度
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private int width;
    private int height;
    private Paint paint;

    public RotatingPlaneView(Context context) {
        this(context,null);
    }

    public RotatingPlaneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void init(Context context) {
        pos = new float[2];
        tan = new float[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;       // 缩放图片
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow, options);
        mMatrix = new Matrix();

        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(width/2,height/2);

        Path path = new Path();
        path.addCircle(0,0,150, Path.Direction.CW);

        PathMeasure pathMeasure = new PathMeasure(path,false);
        currentValue+=0.005;
        if(currentValue>1){
            currentValue = 0;
        }
//        方式一
//        pathMeasure.getPosTan(currentValue*pathMeasure.getLength(),pos,tan);
//
//        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI); // 计算图片旋转角度
//        mMatrix.reset();
//        mMatrix.postRotate(degrees, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);   // 旋转图片
//        mMatrix.postTranslate(pos[0]-mBitmap.getWidth() / 2,pos[1]-mBitmap.getHeight() / 2);
//        方式二 getMatrix会得到pos值和tan值 并设置到Matrix中
        // 获取当前位置的坐标以及趋势的矩阵
        pathMeasure.getMatrix(pathMeasure.getLength() * currentValue, mMatrix, PathMeasure.TANGENT_MATRIX_FLAG | PathMeasure.POSITION_MATRIX_FLAG);
        mMatrix.preTranslate(-mBitmap.getWidth() / 2, -mBitmap.getHeight() / 2);   // <-- 将图片绘制中心调整到与当前点重合(注意:此处是前乘pre)

        canvas.drawBitmap(mBitmap,mMatrix,null);
        canvas.drawPath(path,paint);

        invalidate();
    }

}
