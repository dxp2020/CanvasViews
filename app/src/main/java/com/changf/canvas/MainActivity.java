package com.changf.canvas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.changf.canvas.activitys.CanvasRotateCircleActivity;
import com.changf.canvas.activitys.ElasticBallViewActivity;
import com.changf.canvas.activitys.RoundViewActivity;
import com.changf.canvas.activitys.SolidRoundViewActivity;
import com.changf.canvas.activitys.XfermodeRoundViewActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void circleAngleImage(View view){
        startActivity(new Intent(this,RoundViewActivity.class));
    }

    public void circleAngleImage2(View view){
        startActivity(new Intent(this,XfermodeRoundViewActivity.class));
    }

    public void solidRoundView(View view){
        startActivity(new Intent(this,SolidRoundViewActivity.class));
    }

    public void elasticBall(View view){
        startActivity(new Intent(this,ElasticBallViewActivity.class));
    }

    public void canvasRotateCircle(View view){
        startActivity(new Intent(this,CanvasRotateCircleActivity.class));
    }
}
