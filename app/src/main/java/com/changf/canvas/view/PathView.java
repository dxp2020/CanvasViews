package com.changf.canvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class PathView extends View {
    private int layerNum = 6;//蜘蛛网的层数
    private int layerInteval;//每一层的间隔
    private int rectSide = 6;//矩形边数
    private int padding = 60;//左右的padding
    private float width;
    private float height;
    private float angle = (float) (Math.PI*2/rectSide);
    private String[] data = {"a","b","c","d","e","f"};

    public PathView(Context context) {
        this(context,null);
    }

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        layerInteval = (int) ((width-2*padding)/12);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(width/2,height/2);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#666666"));
        paint.setStrokeWidth(3);

        Path path = new Path();
        for(int i=0;i<layerNum;i++){
            int radius = layerInteval*(i+1);
            path.reset();
            for(int j=0;j<rectSide;j++){
                float x = (float) (radius*Math.cos(angle*j));
                float y = (float) (radius*Math.sin(angle*j));
                if(j==0){
                    path.moveTo(x,y);
                }else{
                    path.lineTo(x,y);
                }
            }
            path.close();
            canvas.drawPath(path,paint);
        }

        drawLines(canvas,paint);
        drawText(canvas,paint);
        drawCover(canvas,paint);
    }

    private void drawCover(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        for(int i=0;i<6;i++){
            float radius = (new Random().nextInt(100)+1)*6*layerInteval/100f;
            int x = (int) (radius * Math.cos(angle*i));
            int y = (int) (radius * Math.sin(angle*i));
            if(i==0){
                path.moveTo(x,y);
            }else{
                path.lineTo(x,y);
            }
            canvas.drawCircle(x,y,6f,paint);
        }
        path.close();
        paint.setColor(Color.BLUE);
        paint.setAlpha(128);
        canvas.drawPath(path,paint);
    }

    private void drawText(Canvas canvas, Paint textPaint) {
        textPaint.setTextSize(30);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;

        float radius = layerInteval*layerNum;

        for(int i=0;i<layerNum;i++){
            float x = (float) (radius*Math.cos(angle*i));
            float y = (float) (radius*Math.sin(angle*i));
            float width = textPaint.measureText(data[i]);
            switch (i){
                case 0:
                    canvas.drawText(data[i],x+width,y,textPaint);
                    break;
                case 1:
                    canvas.drawText(data[i],x,y+fontHeight,textPaint);
                    break;
                case 2:
                    canvas.drawText(data[i],x-width,y+fontHeight,textPaint);
                    break;
                case 3:
                    canvas.drawText(data[i],x-2*width,y,textPaint);
                    break;
                case 4:
                    canvas.drawText(data[i],x-width,y-width,textPaint);
                    break;
                case 5:
                    canvas.drawText(data[i],x,y-width,textPaint);
                    break;
            }
        }
    }

    private void drawLines(Canvas canvas, Paint paint) {
        Path path = new Path();
        int radius = layerInteval*layerNum;
        for(int i=0;i<layerNum;i++){
            path.reset();
            path.moveTo(0,0);
            float x = (float) (radius*Math.cos(angle*i));
            float y = (float) (radius*Math.sin(angle*i));
            path.lineTo(x,y);
            canvas.drawPath(path,paint);
        }
    }
}
