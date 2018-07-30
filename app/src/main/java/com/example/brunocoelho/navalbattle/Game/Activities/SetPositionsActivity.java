package com.example.brunocoelho.navalbattle.Game.Activities;

import android.content.ClipDescription;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.brunocoelho.navalbattle.Game.Models.Position;
import com.example.brunocoelho.navalbattle.Game.Models.Ship;
import com.example.brunocoelho.navalbattle.Game.NavalBattleGame;
import com.example.brunocoelho.navalbattle.Game.SetPositionView;
import com.example.brunocoelho.navalbattle.R;

import java.util.ArrayList;
import java.util.List;

public class SetPositionsActivity extends Activity {

    private FrameLayout frameLayout;
    private SetPositionView setPositionView;
    private NavalBattleGame navalBattleGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_positions);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        navalBattleGame = (NavalBattleGame)getIntent().getSerializableExtra("navalBattleGame");

        View view = this.getWindow().getDecorView();

//        view.setBackgroundColor(Color.parseColor("#FFDB8E"));
        view.setBackgroundResource(R.drawable.background);


        setShips();

        frameLayout = findViewById(R.id.positionsField);
        setPositionView = new SetPositionView(this, navalBattleGame);
        frameLayout.addView(setPositionView);



    }



    private Ship createShip(List<Position> positionList)
    {
        Ship ship = new Ship();

        ship.setPositionList(positionList);
        for(Position position : ship.getPositionList())
        {
            Position p = new Position(position.getNumber(), position.getLetter());
            ship.getInitialPositionList().add(p);
        }

        return ship;
    }
    private void setShips() {

        List<Position> positionList = new ArrayList<>();
        List<Ship> teamA = navalBattleGame.getTeamA();
        Ship ship;

        //2x1
        positionList = new ArrayList<>();
        positionList.add(new Position(0,9));
        ship = createShip(positionList);
        teamA.add(ship);

        positionList = new ArrayList<>();
        positionList.add(new Position(0,11));
        ship = createShip(positionList);
        teamA.add(ship);

        //2x2
        positionList = new ArrayList<>();
        positionList.add(new Position(0,13));
        positionList.add(new Position(0,14));
        ship = createShip(positionList);
        teamA.add(ship);
        positionList = new ArrayList<>();
        positionList.add(new Position(2,9));
        positionList.add(new Position(2,10));
        ship = createShip(positionList);
        teamA.add(ship);

        //2x3
        positionList = new ArrayList<>();
        ship = new Ship();
        positionList.add(new Position(2,13));
        positionList.add(new Position(2,14));
        positionList.add(new Position(2,15));
        ship.setPositionList(positionList);
        ship.setInitialPositionList(new ArrayList<Position>(positionList));
        teamA.add(ship);
        positionList = new ArrayList<>();
        positionList.add(new Position(4,9));
        positionList.add(new Position(4,10));
        positionList.add(new Position(4,11));
        ship = createShip(positionList);
        teamA.add(ship);

        //1x5 T
        positionList = new ArrayList<>();
        positionList.add(new Position(4,13));
        positionList.add(new Position(4,14));
        positionList.add(new Position(4,15));
        positionList.add(new Position(5,14));
        positionList.add(new Position(6,14));
        ship = createShip(positionList);
        teamA.add(ship);

    }




    public void onCloseSetPositions(View v) {

        Log.d("onCloseSetPositions", "Clicked onCloseSetPositions");

        finish();
    }

}
