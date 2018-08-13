package com.changf.canvas.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.changf.canvas.R;
import com.changf.canvas.view.CheckView;

public class SolidRoundViewActivity  extends Activity {
    private CheckView cv_check_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_solid_round_view);

        cv_check_view = findViewById(R.id.cv_check_view);
        cv_check_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv_check_view.check();
            }
        });
    }
}