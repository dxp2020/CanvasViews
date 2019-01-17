package com.changf.canvas.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changf.canvas.R;

public class RotateRingView extends RelativeLayout {
    private RingView rRingView;
    private TextView tvCountView;
    private ObjectAnimator objectAnimator;
    private boolean isStarted;
    private long startTimeMill;
    private Handler handler;

    public RotateRingView(Context context) {
        this(context,null);
    }

    public RotateRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.layout_route_ring_view,this);
        rRingView = view.findViewById(R.id.r_ring_view);
        tvCountView = view.findViewById(R.id.tv_count_down);
        init();
    }

    private void init(){
        objectAnimator = ObjectAnimator.ofFloat(rRingView, "rotation", 0f, 360f);//添加旋转动画，旋转中心默认为控件中点
        objectAnimator.setDuration(3000);//设置动画时间
        objectAnimator.setInterpolator(new LinearInterpolator());//动画时间线性渐变
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                handler.sendEmptyMessageDelayed(0,1000);
                tvCountView.setText(formatDuring((System.currentTimeMillis() - startTimeMill)));
            }
        };
    }

    /**
     * @param mss 要转换的毫秒数
     * @return 该毫秒数转换为 minutes * seconds 后的格式
     */
    public static String formatDuring(long mss) {
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return formart(minutes) + ":" + formart(seconds) ;
    }

    private static String formart(long seconds) {
        if(seconds<10){
            return "0"+seconds;
        }else{
            return String.valueOf(seconds);
        }
    }

    public void onResume() {
        objectAnimator.start();//动画开始
        if(!isStarted){
            isStarted = true;
            startTimeMill = System.currentTimeMillis();
        }
        handler.sendEmptyMessage(0);
    }

    public void onPause() {
        objectAnimator.end();//动画结束
        handler.removeMessages(0);
    }
}
