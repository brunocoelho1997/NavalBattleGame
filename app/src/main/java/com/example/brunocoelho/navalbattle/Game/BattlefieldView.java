package com.example.brunocoelho.navalbattle.Game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.brunocoelho.navalbattle.Game.Models.Position;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.Ship;
import com.example.brunocoelho.navalbattle.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BattlefieldView extends View{

    float initialX, initialY;
    private NavalBattleGame navalBattleGame;



    private Context context;


    public BattlefieldView(Context context, NavalBattleGame navalBattleGame) {
        super(context);
        this.navalBattleGame = navalBattleGame;
        this.context = context;

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

                navalBattleGame.onDown(onDownPosition);


                break;

            case MotionEvent.ACTION_MOVE:
                float finalX = event.getX();
                float finalY = event.getY();

                Position onMovePosition = new Position((int)(finalY*9 / getWidth()), ((int)(finalX*9 / getHeight())));
                Log.d("onMovePosition", onMovePosition.toString() + "---\n");

                navalBattleGame.onMove(onMovePosition);

                invalidate();

                break;

            case MotionEvent.ACTION_UP:
                finalX = event.getX();
                finalY = event.getY();

                Position onUpPosition = new Position((int)(finalY*9 / getWidth()), ((int)(finalX*9 / getHeight())));

                Log.d("onUP", onUpPosition.toString() + "---\n");

                navalBattleGame.onUp(onUpPosition);



                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.d("tag","Action was CANCEL");
                break;

            case MotionEvent.ACTION_OUTSIDE:
                Log.d("tag", "Movement occurred outside bounds of current screen element");
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
                    int icon;

                    if(number == 0 && letter == 1)
                        icon = Constants.LETTER_A;
                    else if(number == 0 && letter == 2)
                        icon = Constants.LETTER_B;
                    else if(number == 0 && letter == 3)
                        icon = Constants.LETTER_C;
                    else if(number == 0 && letter == 4)
                        icon = Constants.LETTER_D;
                    else if(number == 0 && letter == 5)
                        icon = Constants.LETTER_E;
                    else if(number == 0 && letter == 6)
                        icon = Constants.LETTER_F;
                    else if(number == 0 && letter == 7)
                        icon = Constants.LETTER_G;
                    else if(number == 0 && letter == 8)
                        icon = Constants.LETTER_H;
                    else if(number == 1 && letter == 0)
                        icon = Constants.NUMBER_1;
                    else if(number == 2 && letter == 0)
                        icon = Constants.NUMBER_2;
                    else if(number == 3 && letter == 0)
                        icon = Constants.NUMBER_3;
                    else if(number == 4 && letter == 0)
                        icon = Constants.NUMBER_4;
                    else if(number == 5 && letter == 0)
                        icon = Constants.NUMBER_5;
                    else if(number == 6 && letter == 0)
                        icon = Constants.NUMBER_6;
                    else if(number == 7 && letter == 0)
                        icon = Constants.NUMBER_7;
                    else if(number == 8 && letter == 0)
                        icon = Constants.NUMBER_8;
                    else
                        icon = Constants.BLANK_SQUARE;

                    //vai buscar o tamanho real
                    realImage = BitmapFactory.decodeResource(getResources(), icon);

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

    private void paintFiredPositionsTeam(Canvas canvas, List<Position> positions) {
        Bitmap realImage;
        Bitmap newBitmap;

        //size of the ship
        int wPeca = this.getWidth()/9;
        int hPeca = this.getHeight()/9;

        //point where ship will be painted
        float letterPoint;
        float numberPoint;


        int marginShip = 0;

        for(Position position : positions){


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

    private void paintTempFiredPositions(Canvas canvas) {
            Bitmap realImage;
            Bitmap newBitmap;

            //size of the ship
            int wPeca = this.getWidth()/9;
            int hPeca = this.getHeight()/9;

            //point where ship will be painted
            float letterPoint;
            float numberPoint;

            for(Position position : navalBattleGame.getFiredPositionsTemp())
            {
                //vai buscar o tamanho real
                realImage = BitmapFactory.decodeResource(getResources(), Constants.FIRED_SQUARE);
//              realImage = createImage(50,50, ship.getColor());

                //convert to point of the size of the table
                newBitmap = Bitmap.createScaledBitmap(realImage, wPeca,hPeca
                        , false);

                numberPoint =  position.getNumber() * (this.getHeight()/9) ;
                letterPoint = position.getLetter() * (this.getWidth()/9) ;

                canvas.drawBitmap(newBitmap, letterPoint , numberPoint, null);
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

        //if the game already started and we may NOT change position in a ship...
        //if game already started... just show destroyed positions from another team
        if(navalBattleGame.isStarted() && !navalBattleGame.isMayChangeShipPosition())
        {

            if(navalBattleGame.isMyTurnToPlay())
            {
                if(navalBattleGame.getFiredPositionsTemp().size()!=3)
                {
                    paintTempFiredPositions(canvas);
                    Log.d("onDraw", "Painted last " + navalBattleGame.getFiredPositionsTemp().size() + "  fired positions of team A.");
                }

                paintFiredPositionsTeam(canvas, navalBattleGame.getAtualTeam().getFiredPositions());
                Log.d("onDraw", "Painted fired positions of team A:" + navalBattleGame.getAtualTeam().getFiredPositions().toString());

            }
            else
            {
                Log.d("onDraw", "AI will play.");
                navalBattleGame.AIFire();

//                paintTempFiredPositions(canvas);
//                Log.d("onDraw", "Painted last " + navalBattleGame.getFiredPositionsTemp().size() + "  fired positions of team B.");

                paintShips(canvas, navalBattleGame.getOpositeTeam().getShips());
                paintFiredPositionsTeam(canvas, navalBattleGame.getAtualTeam().getFiredPositions());
                Log.d("onDraw", "Painted fired positions of team B:" + navalBattleGame.getAtualTeam().toString());

            }



            if(navalBattleGame.verifyEndOfGame())
                createAlertDialog();


        }
        //if the game has not started yet or user may choice new position to as ship.. show all game to user change positions
        else
        {

            if(navalBattleGame.isMayChangeShipPosition())
                createToast(R.string.choose_new_position, Toast.LENGTH_SHORT);


            if(navalBattleGame.isAmITeamA())
                paintShips(canvas, navalBattleGame.getTeamA().getShips());
            else
                paintShips(canvas, navalBattleGame.getTeamB().getShips());

            paintInvalidPositions(canvas);

        }
    }


    private void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        int str;

        if(navalBattleGame.isTeamATurn())
            str = R.string.won_a;

        else
            str = R.string.won_b;


        builder
                .setTitle(R.string.end_game)
                .setCancelable(false)
                .setMessage(str)
                .setPositiveButton(R.string.new_game, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) context).finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
//                if(isTeamATurn())
//
//                    Log.d("verifyFiredPosition","TeamA won!!!");
//                else
//                    Log.d("verifyFiredPosition","TeamB won!!!");
    }

    private void createToast(int str, int duration)
    {
        CharSequence text = getResources().getString(str);
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
