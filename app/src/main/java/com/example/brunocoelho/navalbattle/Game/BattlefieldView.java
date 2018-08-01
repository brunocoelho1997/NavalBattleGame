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
import com.example.brunocoelho.navalbattle.Game.Models.Ships.Ship;

import java.util.List;

public class BattlefieldView extends View{

String TAG ="minha";
    float initialX, initialY;
    private NavalBattleGame navalBattleGame;

    private Ship selectedShip;

    Position lastPoint = null;

    Position onDownPosition = null;
    Position onMovePosition= null;
    Position onUpPosition = null;

    Position lasValidPosition = null;

    public BattlefieldView(Context context, NavalBattleGame navalBattleGame) {
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

                onDownPosition = new Position((int)(initialY*9 / getWidth()), ((int)(initialX*9 / getHeight())));

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

                onMovePosition = new Position((int)(finalY*9 / getWidth()), ((int)(finalX*9 / getHeight())));
                Log.d("onMovePosition", onMovePosition.toString() + "---\n");

                if(selectedShip!= null)
                {
                    selectedShip.setPointPosition(onMovePosition);

                    if(navalBattleGame.isInsideView(selectedShip))
                        lasValidPosition = onMovePosition;
                    else
                        selectedShip.setPointPosition(lasValidPosition);

                    navalBattleGame.refreshInvalidPositions(selectedShip);

                    invalidate();
                }


                break;

            case MotionEvent.ACTION_UP:
                finalX = event.getX();
                finalY = event.getY();

                onUpPosition = new Position((int)(finalY*9 / getWidth()), ((int)(finalX*9 / getHeight())));

                Log.d("onUP", onUpPosition.toString() + "---\n");

                if(selectedShip!=null)
                {

                    //if down, move and up same position so this is a rotate
                    if(onDownPosition.equals(onUpPosition))
                    {
                        selectedShip.rotate();
                        //rotate while doesn't find valid rotation
                        while(true)
                        {
                            selectedShip.setPointPosition(onUpPosition);
                            if(!navalBattleGame.isInsideView(selectedShip))
                                selectedShip.rotate();
                            else
                                break;
                        }
                    }



                    selectedShip.setPointPosition(onUpPosition);

                    navalBattleGame.refreshInvalidPositions(selectedShip);


                    if(navalBattleGame.isInsideView(selectedShip))
                        lasValidPosition = onUpPosition;
                    else
                        selectedShip.setPointPosition(lasValidPosition);

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

            for(Position position : ship.getPositionList()){

                //vai buscar o tamanho real
                realImage = BitmapFactory.decodeResource(getResources(), position.getColor());
//              realImage = createImage(50,50, ship.getColor());

                //convert to point of the size of the table
                newBitmap = Bitmap.createScaledBitmap(realImage, wPeca-marginShip,hPeca-marginShip
                        , false);

                numberPoint =  position.getNumber() * (this.getHeight()/9) + marginShip/2;
                letterPoint = position.getLetter() * (this.getWidth()/9) + marginShip/2;

                canvas.drawBitmap(newBitmap, letterPoint , numberPoint, null);
            }
        }
    }

    private void paintInvalidPositions(Canvas canvas) {
        Bitmap realImage;
        Bitmap newBitmap;

        //size of the ship
        int wPeca = this.getWidth()/9;
        int hPeca = this.getHeight()/9;

        //point where ship will be painted
        float letterPoint;
        float numberPoint;

        for(Position position : navalBattleGame.getInvalidPositions())
        {
            //vai buscar o tamanho real
            realImage = BitmapFactory.decodeResource(getResources(), Constants.RED_SQUARE);
//              realImage = createImage(50,50, ship.getColor());

            //convert to point of the size of the table
            newBitmap = Bitmap.createScaledBitmap(realImage, wPeca,hPeca
                    , false);

            numberPoint =  position.getNumber() * (this.getHeight()/9) ;
            letterPoint = position.getLetter() * (this.getWidth()/9) ;

            canvas.drawBitmap(newBitmap, letterPoint , numberPoint, null);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paintMap(canvas);

        //if game already started... just show asdasdadasdasdasdasddsas
        if(navalBattleGame.isStarted())
        {
            if(navalBattleGame.isPlayerATurn())
                paintShips(canvas, navalBattleGame.getTeamA());
            else
                paintShips(canvas, navalBattleGame.getTeamB());
        }
        //if the game has not started yet show all game to user change positions
        else
        {
            if(navalBattleGame.isAmITeamA())
                paintShips(canvas, navalBattleGame.getTeamA());
            else
                paintShips(canvas, navalBattleGame.getTeamB());
        }

        paintInvalidPositions(canvas);

    }
}
