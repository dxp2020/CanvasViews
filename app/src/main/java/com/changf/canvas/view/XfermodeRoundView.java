package com.changf.canvas.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.changf.canvas.R;

public class XfermodeRoundView extends View {
    private static final String TAG = "XfermodeRoundView";

    private Bitmap src;
    private int outWidth;
    private int outHeight;
    private int style;//0为clrcle、1为round
    private float radius;//圆角半径
    private String[] straightMode = new String[]{};


    public XfermodeRoundView(Context context) {
        this(context,null);
    }

    public XfermodeRoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.RoundView, 0, 0);
        radius = a.getDimension(R.styleable.RoundView_radius,10);
        style = a.getInt(R.styleable.RoundView_style,1);
        String straightModeStr = a.getString(R.styleable.RoundView_straight_mode);
        a.recycle();
        src = BitmapFactory.decodeResource(getResources(), R.mipmap.meinv);

        if(straightModeStr!=null){
            straightMode = straightModeStr.split("\\|");
        }
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
        canvas.drawBitmap(getRoundBitmap(),getPaddingLeft(),getPaddingTop(),null);
    }

    private Bitmap getRoundBitmap() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        // 初始化目标bitmap
        Bitmap targetBitmap = Bitmap.createBitmap(outWidth,outHeight,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        // 在画布上绘制圆角
        canvas.drawRoundRect(new RectF(0,0,outWidth,outHeight),radius,radius,paint);
        for(String mode:straightMode){
            switch (mode){
                case "lt":
                    //左上直角
                    canvas.drawRect(new RectF(0,0,radius,radius),paint);
                    break;
                case "lb":
                    //左下直角
                    canvas.drawRect(new RectF(0,outHeight-radius,radius,outHeight),paint);
                    break;
                case "rt":
                    //右上直角
                    canvas.drawRect(new RectF(outWidth-radius,0,outWidth,radius),paint);
                    break;
                case "rb":
                    //右下直角
                    canvas.drawRect(new RectF(outWidth-radius,outHeight-radius,outWidth,outHeight),paint);
                    break;
            }
        }
        // 设置叠加模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //宽高等比缩放
        float scaleWidth = (float)outWidth/src.getWidth();
        float scaleHeight = scaleWidth;
        Matrix matrix = new Matrix();
        matrix.setScale(scaleWidth,scaleHeight);

        Bitmap overlayBitmap = Bitmap.createBitmap(src,0,0,src.getWidth(),src.getHeight(),matrix,true);
        Rect ret = new Rect(0,0, outWidth, outHeight);
        //叠加图
        canvas.drawBitmap(overlayBitmap,ret,ret,paint);

        src.recycle();

        return targetBitmap;
    }


}
