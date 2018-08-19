package com.changf.canvas.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class SearchView extends View {
    private static final int ANIMATE_DURATION = 2000;
    private float width;
    private float height;
    private float percent;//百分比
    private Paint paint;
    private Path circlePath;
    private Path searchPath;
    private PathMeasure measure;
    private ValueAnimator startingAnimator;
    private ValueAnimator searchingAnimator;
    private ValueAnimator endingAnimator;
    private State mCurrentState;

    public SearchView(Context context) {
        this(context,null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void init() {
        initPaint();
        initPath();
        initAnimator();
        mCurrentState = State.STARTING;
        startingAnimator.start();
    }

    private void initAnimator() {
        startingAnimator = ValueAnimator.ofFloat(0,1).setDuration(ANIMATE_DURATION);
        searchingAnimator = ValueAnimator.ofFloat(0,1).setDuration(ANIMATE_DURATION);
        searchingAnimator.setRepeatCount(5);
        searchingAnimator.setRepeatMode(ValueAnimator.RESTART);
        endingAnimator = ValueAnimator.ofFloat(1,0).setDuration(ANIMATE_DURATION);

        startingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        searchingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        endingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        startingAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentState = State.SEARCHING;
                searchingAnimator.start();
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        searchingAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentState = State.ENDING;
                endingAnimator.start();
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        endingAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                initAnimator();
                mCurrentState = State.STARTING;
                startingAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initPath() {
        //注意,不要到360度,否则内部会自动优化,测量不能取到需要的数值（不能准确的获取到长度为0位置的xy值）
        circlePath = new Path();
        circlePath.addArc(new RectF(-100,-100,100,100),45,-359.9f);

        searchPath = new Path();
        searchPath.addArc(new RectF(-50,-50,50,50),45,359.9f);

        float[] pos = new float[2];
        measure = new PathMeasure();
        measure.setPath(circlePath,false);
        measure.getPosTan(0,pos,null);
        searchPath.lineTo(pos[0],pos[1]);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(width/2,height/2);
        canvas.drawColor(Color.parseColor("#0082D7"));

        drawCycle(canvas);
    }

    private void drawCycle(Canvas canvas) {
        Path dst = new Path();
        switch (mCurrentState){
            case NONE:
                canvas.drawPath(searchPath,paint);
                break;
            case STARTING:
                measure.setPath(searchPath, false);
                measure.getSegment(measure.getLength()*percent,measure.getLength(),dst,true);
                canvas.drawPath(dst,paint);
                break;
            case SEARCHING:
                measure.setPath(circlePath, false);
                float stop = measure.getLength() * percent;
                float start = (float) (stop - ((0.5 - Math.abs(percent - 0.5)) * 200f));
                measure.getSegment(start,stop,dst,true);
                canvas.drawPath(dst,paint);
                break;
            case ENDING:
                measure.setPath(searchPath, false);
                measure.getSegment(measure.getLength()*percent,measure.getLength(),dst,true);
                canvas.drawPath(dst,paint);
                break;
        }
    }

    // 这个视图拥有的状态
    public enum State {
        NONE,
        STARTING,
        SEARCHING,
        ENDING
    }
}
