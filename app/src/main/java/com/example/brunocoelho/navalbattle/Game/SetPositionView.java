package com.example.brunocoelho.navalbattle.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.brunocoelho.navalbattle.Game.Models.Position;
import com.example.brunocoelho.navalbattle.Game.Models.Ship;
import com.example.brunocoelho.navalbattle.R;

import java.util.List;

public class SetPositionView extends View{

String TAG ="minha";
    float initialX, initialY;
    private NavalBattleGame navalBattleGame;

    private Ship selectedShip;

    Position lastPoint = null;


    public SetPositionView(Context context, NavalBattleGame navalBattleGame) {
        super(context);
        this.navalBattleGame = navalBattleGame;
        this.selectedShip = null;

//        setBackgroundColor(Color.RED);
//        setBackgroundResource(R.drawable.grid_set_positions);
        setBackgroundColor(Color.parseColor("#FFDB8E"));


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                initialY = event.getY();

                Position onDownPosition = new Position((int)(initialY*9 / getWidth()), ((int)(initialX*9 / getHeight())));

//                if(onDownPosition.getLetter() == 0 || onDownPosition.getNumber() == 0)
//                    break;

                selectedShip = navalBattleGame.getShip(onDownPosition);

                Log.d("onDown", "\n---" +onDownPosition.toString());

                if(selectedShip!=null)
                {
                    Log.d("onDown", selectedShip.toString());

                    lastPoint = selectedShip.getPointPosition();
                }


                break;

            case MotionEvent.ACTION_MOVE:
                float finalX = event.getX();
                float finalY = event.getY();



                Position onMovePosition = new Position((int)(finalY*9 / getWidth()), ((int)(finalX*9 / getHeight())));



//                if(onMovePosition.getLetter() == 0 || onMovePosition.getNumber() == 0)
//                    break;

//                if(onMovePosition.getLetter() >= 9 || onMovePosition.getNumber() >=9 )
//                    break;




//                if(onMovePosition.getNumber()<8 && onMovePosition.getLetter()<8)
//                {
//                    Log.d("onMOVE", onMovePosition.toString());

                    if(selectedShip!= null)
                    {
                        selectedShip.setPointPosition(onMovePosition);

                        if(navalBattleGame.isValidatedShip(selectedShip))
                            selectedShip.setColor(Constants.SHIP_VALIDPOSITION_COLOR);
                        else
                            selectedShip.setColor(Constants.SHIP_INVALIDPOSITION_COLOR);

                        invalidate();
                    }


//                }

                break;

            case MotionEvent.ACTION_UP:
                finalX = event.getX();
                finalY = event.getY();

                Position onUpPosition = new Position((int)(finalY*9 / getWidth()), ((int)(finalX*9 / getHeight())));

//                if(onUpPosition.getLetter() <= 0 || onUpPosition.getNumber() <= 0)
//                    break;

//                if(onUpPosition.getLetter() >= 9 || onUpPosition.getNumber() >=9 )
//                    break;


                Log.d("onUP", onUpPosition.toString() + "---\n");

                if(selectedShip!=null)
                {

                    if(navalBattleGame.isValidatedShip(selectedShip))
                    {
                        selectedShip.setPointPosition(onUpPosition);
                        selectedShip.setColor(Constants.SHIP_DEFAULT_COLOR);
                    }

                    else
                    {
                        selectedShip.restoreInitialPosition();
                        selectedShip.setColor(Constants.SHIP_DEFAULT_COLOR);

                    }

                    selectedShip = null;

                    invalidate();
                }
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


    public static Bitmap createImage(int width, int height, String colorString) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor(colorString));
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }

    private void paintMap(Canvas canvas) {
        Bitmap realImage;
        Bitmap newBitmap;

        //size of the ship
        int wPeca = this.getWidth()/9;
        int hPeca = this.getHeight()/9;

        //point where ship will be painted
        float letterPoint;
        float numberPoint;

        for(int number = 0; number<9; number++)
        {
            for(int letter= 0; letter<9; letter++)
            {
                if(number == 0 || letter == 0)
                {
                    //vai buscar o tamanho real
                    realImage = BitmapFactory.decodeResource(getResources(), Constants.LETTER_A);

                    //convert to point of the size of the table
                    newBitmap = Bitmap.createScaledBitmap(realImage, wPeca,hPeca
                            , false);
                }
                else
                {
                    //vai buscar o tamanho real
                    realImage = BitmapFactory.decodeResource(getResources(), Constants.BLANK_SQUARE);
                }

                //convert to point of the size of the table
                newBitmap = Bitmap.createScaledBitmap(realImage, wPeca,hPeca
                        , false);

                numberPoint =  number * (this.getHeight()/9);
                letterPoint = letter * (this.getWidth()/9);
                canvas.drawBitmap(newBitmap, letterPoint , numberPoint, null);

            }
        }
    }
    private void paintShips(Canvas canvas, List<Ship> team) {
        Bitmap realImage;
        Bitmap newBitmap;

        //size of the ship
        int wPeca = this.getWidth()/9;
        int hPeca = this.getHeight()/9;

        //point where ship will be painted
        float letterPoint;
        float numberPoint;

        for(Ship ship : team)
        {
            int marginShip = 0;

            //vai buscar o tamanho real
            realImage = BitmapFactory.decodeResource(getResources(), Constants.FULL_SQUARE);
//            realImage = createImage(50,50, ship.getColor());

            //convert to point of the size of the table
            newBitmap = Bitmap.createScaledBitmap(realImage, wPeca-marginShip,hPeca-marginShip
                    , false);

            for(Position position : ship.getPositionList()){

                numberPoint =  position.getNumber() * (this.getHeight()/9) + marginShip/2;
                letterPoint = position.getLetter() * (this.getWidth()/9) + marginShip/2;

                canvas.drawBitmap(newBitmap, letterPoint , numberPoint, null);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paintMap(canvas);
        paintShips(canvas, navalBattleGame.getTeamA());

    }
}
