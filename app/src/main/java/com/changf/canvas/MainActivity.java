package com.changf.canvas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.changf.canvas.activitys.RoundViewActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void circleAngleImage(View view){
        startActivity(new Intent(this,RoundViewActivity.class));
    }
}
