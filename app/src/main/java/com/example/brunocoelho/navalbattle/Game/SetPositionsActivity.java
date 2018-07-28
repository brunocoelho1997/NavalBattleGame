package com.example.brunocoelho.navalbattle.Game;

import android.content.ClipDescription;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.brunocoelho.navalbattle.R;

public class SetPositionsActivity extends Activity {

    private FrameLayout frameLayout;
    private SetPositionView setPositionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_positions);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        frameLayout = findViewById(R.id.positionsField);
        setPositionView = new SetPositionView(this);
        frameLayout.addView(setPositionView);
    }

    public void onCloseSetPositions(View v) {

        Log.d("onCloseSetPositions", "Clicked onCloseSetPositions");

        finish();
    }

}
