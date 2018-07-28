package com.example.brunocoelho.navalbattle.Game;

import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.brunocoelho.navalbattle.R;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class SetPositionView extends View{

String TAG ="minha";
    float initialX, initialY;

    public SetPositionView(Context context) {
        super(context);
        setBackgroundColor(Color.RED);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                initialY = event.getY();

                Position position = new Position((int)(initialX*16 / getWidth()), ((int)(initialY*16 / getHeight())));
                Log.d("onDown", position.toString());


                break;

//            case MotionEvent.ACTION_MOVE:
//                Log.d(TAG, "Action was MOVE");
//                break;

            case MotionEvent.ACTION_UP:
                float finalX = event.getX();
                float finalY = event.getY();

                Position position2 = new Position((int)(finalX*16 / getWidth()), ((int)(finalY*16 / getHeight())));
                Log.d("onUP", position2.toString());
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG,"Action was CANCEL");
                break;

            case MotionEvent.ACTION_OUTSIDE:
                Log.d(TAG, "Movement occurred outside bounds of current screen element");
                break;
        }
        return true;
    }


}
