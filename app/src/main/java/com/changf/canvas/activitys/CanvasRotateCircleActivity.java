package com.changf.canvas.activitys;

import android.app.Activity;
import android.os.Bundle;

import com.changf.canvas.R;
import com.changf.canvas.view.RotateRingView;

public class CanvasRotateCircleActivity extends Activity {
    private RotateRingView v_rotate_ring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_canvas_rotate_circle_view);
        v_rotate_ring = findViewById(R.id.v_rotate_ring);
    }

    @Override
    protected void onResume() {
        super.onResume();
        v_rotate_ring.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        v_rotate_ring.onPause();
    }
}