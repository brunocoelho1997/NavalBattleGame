package com.example.brunocoelho.navalbattle.Game.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.brunocoelho.navalbattle.Game.Models.Position;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.Ship;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipFive;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipOne;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipThree;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipTwo;
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

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            navalBattleGame = (NavalBattleGame) savedInstanceState.getSerializable("restoredNavalBattleGame");
        } else {
            // when is new game...
            navalBattleGame = (NavalBattleGame)getIntent().getSerializableExtra("navalBattleGame");
            setShips();

        }

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        View view = this.getWindow().getDecorView();

        view.setBackgroundColor(Color.parseColor("#FFDB8E"));
//        view.setBackgroundResource(R.drawable.background);




        frameLayout = findViewById(R.id.positionsField);
        setPositionView = new SetPositionView(this, navalBattleGame);
        frameLayout.addView(setPositionView);



    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putSerializable("restoredNavalBattleGame", navalBattleGame);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    private Ship createShip(List<Position> positionList)
    {

        Ship ship = null;

        if(positionList.size() ==1)
            ship = new ShipOne();
        else if(positionList.size() == 2)
            ship = new ShipTwo();
        else if(positionList.size() == 3)
            ship = new ShipThree();
        else if(positionList.size() == 5)
            ship = new ShipFive();


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
        positionList.add(new Position(1,1));
        ship = createShip(positionList);
        teamA.add(ship);

        positionList = new ArrayList<>();
        positionList.add(new Position(1,3));
        ship = createShip(positionList);
        teamA.add(ship);

        //2x2
        positionList = new ArrayList<>();
        positionList.add(new Position(1,5));
        positionList.add(new Position(1,6));
        ship = createShip(positionList);
        teamA.add(ship);

        positionList = new ArrayList<>();
        positionList.add(new Position(3,2));
        positionList.add(new Position(3,3));
        ship = createShip(positionList);
        teamA.add(ship);

        //2x3
        positionList = new ArrayList<>();
        positionList.add(new Position(3,5));
        positionList.add(new Position(3,6));
        positionList.add(new Position(3,7));
        ship = createShip(positionList);
        teamA.add(ship);
        positionList = new ArrayList<>();
        positionList.add(new Position(5,1));
        positionList.add(new Position(5,2));
        positionList.add(new Position(5,3));
        ship = createShip(positionList);
        teamA.add(ship);

        //1x5 T
        positionList = new ArrayList<>();
        positionList.add(new Position(5,5));
        positionList.add(new Position(5,6));
        positionList.add(new Position(5,7));
        positionList.add(new Position(6,6));
        positionList.add(new Position(7,6));
        ship = createShip(positionList);
        teamA.add(ship);

    }




    public void onCloseSetPositions(View v) {

        Log.d("onCloseSetPositions", "Clicked onCloseSetPositions");

        finish();
    }

}
