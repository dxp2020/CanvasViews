package com.changf.canvas.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.changf.canvas.R;

public class RoundView extends View {
    private static final String TAG = "RoundView";

    private Bitmap src;
    private int outWidth;
    private int outHeight;
    private int style;//0为clrcle、1为round
    private int straightMode;
    private float radius;//圆角半径


    public RoundView(Context context) {
        this(context,null);
    }

    public RoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.RoundView, 0, 0);
        radius = a.getDimension(R.styleable.RoundView_radius,10);
        style = a.getInt(R.styleable.RoundView_style,1);
        straightMode = a.getInt(R.styleable.RoundView_straight_mode,0);
        a.recycle();
        src = BitmapFactory.decodeResource(getResources(), R.mipmap.meinv);

        Log.e(TAG,"straightMode--"+straightMode);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultWidthSize(widthMeasureSpec);
        int height = getDefaultHeightSize(heightMeasureSpec);
        outWidth = outHeight = Math.min(width,height);

        if (style==0) {
            radius = outWidth/2;
        }

        setMeasuredDimension(getPaddingLeft()+outWidth+getPaddingRight(),getPaddingTop()+outHeight+getPaddingBottom());
    }

    private int getDefaultWidthSize(int widthMeasureSpec) {
        switch (MeasureSpec.getMode(widthMeasureSpec)){
            case MeasureSpec.EXACTLY:
                return MeasureSpec.getSize(widthMeasureSpec);
            default:
                int screenWidth = getScreenWidth();
                return src.getWidth()>screenWidth?screenWidth:src.getWidth();
        }
    }

    private int getDefaultHeightSize(int heightMeasureSpec) {
        switch (MeasureSpec.getMode(heightMeasureSpec)){
            case MeasureSpec.EXACTLY:
                return MeasureSpec.getSize(heightMeasureSpec);
            default:
                return src.getHeight();
        }
    }

    private int getScreenWidth(){
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(getRoundBitmap(),0,0,null);
    }

    private Bitmap getRoundBitmap() {
        //宽高等比缩放
        float scaleWidth = (float)outWidth/src.getWidth();
        float scaleHeight = scaleWidth;
        Matrix matrix = new Matrix();
        matrix.setScale(scaleWidth,scaleHeight);

        BitmapShader bitmapShader = new BitmapShader(src, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
        bitmapShader.setLocalMatrix(matrix);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

        src.recycle();

        //由于decode的bitmap不能进行加工，只能创建一个新的bitmap来进行绘制
        Bitmap targetBitmap = Bitmap.createBitmap(outWidth,outHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        canvas.drawRoundRect(new RectF(getPaddingLeft(),getPaddingTop(),outWidth,outHeight),radius,radius,paint);

        drawStraight(canvas,paint,straightMode);

        return targetBitmap;
    }

    private void drawStraight(Canvas canvas,Paint paint,int straightMode){
        switch (straightMode){
            case 1://left
                canvas.drawRect(new RectF(getPaddingLeft(),getPaddingTop(),radius,outHeight),paint);
                break;
            case 2://right
                canvas.drawRect(new RectF(getWidth()-getPaddingRight()-radius,getPaddingTop(),getWidth(),getHeight()),paint);
                break;
            case 4://top
                canvas.drawRect(new RectF(getPaddingLeft(),getPaddingTop(),getWidth(),getPaddingTop()+radius),paint);
                break;
            case 8://bottom
                canvas.drawRect(new RectF(getPaddingLeft(),getHeight()-getPaddingBottom()-radius,getWidth(),getHeight()),paint);
                break;
            case 5://left | top
                canvas.drawRect(new RectF(getPaddingLeft(),getPaddingTop(),radius,radius+getPaddingTop()),paint);
                break;
            case 9://left | bottom
                canvas.drawRect(new RectF(getPaddingLeft(),getHeight()-getPaddingBottom()-radius,getPaddingLeft()+radius,getHeight()),paint);
                break;
            case 10://right | bottom
                canvas.drawRect(new RectF(getWidth()-getPaddingRight()-radius,getHeight()-getPaddingBottom()-radius,getWidth(),getHeight()),paint);
                break;
            case 3://left | right
                canvas.drawRect(new RectF(getPaddingLeft(),getPaddingTop(),getWidth(),getHeight()),paint);
                break;
            case 6://right | top
                canvas.drawRect(new RectF(getWidth()-getPaddingRight()-radius,getPaddingTop(),getWidth()-getPaddingRight(),radius+getPaddingTop()),paint);
                break;
            case 16://left_right_top
                drawStraight(canvas,paint,1);
                drawStraight(canvas,paint,6);
                break;
            case 17://left_right_bottom
                drawStraight(canvas,paint,1);
                drawStraight(canvas,paint,10);
                break;
            case 18://left_top_right
                drawStraight(canvas,paint,5);
                drawStraight(canvas,paint,2);
                break;
            case 19://left_bottom_right
                drawStraight(canvas,paint,9);
                drawStraight(canvas,paint,2);
                break;
            case 15://left | right | top | bottom
                canvas.drawRect(new RectF(getPaddingLeft(),getPaddingTop(),getWidth(),getHeight()),paint);
                break;
        }
    }

}
