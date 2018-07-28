package com.example.brunocoelho.navalbattle.Game;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.brunocoelho.navalbattle.R;

public class BattlefieldView extends View implements GestureDetector.OnGestureListener{

    private GestureDetector gestureDetector;


    public BattlefieldView(Context context) {
        super(context);
        gestureDetector = new GestureDetector(context, this);

        setBackgroundColor(Color.GREEN);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
            return true;
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Position position = new Position((int)(e.getX()*8 / getWidth()), ((int)(e.getY()*8 / getHeight())));

        Log.d("onDown", position.toString());

        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
