package com.example.brunocoelho.navalbattle.Game.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.brunocoelho.navalbattle.Game.Models.Position;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.Ship;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipFive;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipOne;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipThree;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipTwo;
import com.example.brunocoelho.navalbattle.Game.NavalBattleGame;
import com.example.brunocoelho.navalbattle.Game.BattlefieldView;
import com.example.brunocoelho.navalbattle.R;

import java.util.ArrayList;
import java.util.List;

public class BattlefieldActivity extends Activity {

    private FrameLayout frameLayout;
    private BattlefieldView battlefieldView;
    private NavalBattleGame navalBattleGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battlefield);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            navalBattleGame = (NavalBattleGame) savedInstanceState.getSerializable("restoredNavalBattleGame");
        } else {
            // when is new game...
            navalBattleGame = (NavalBattleGame)getIntent().getSerializableExtra("navalBattleGame");
            navalBattleGame.setTeamsPositions();
        }

        View view = this.getWindow().getDecorView();

        view.setBackgroundColor(Color.parseColor("#FFDB8E"));
//        view.setBackgroundResource(R.drawable.background);




        frameLayout = findViewById(R.id.positionsField);
        battlefieldView = new BattlefieldView(this, navalBattleGame);
        frameLayout.addView(battlefieldView);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putSerializable("restoredNavalBattleGame", navalBattleGame);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    public void onStartGame(View v) {
        navalBattleGame.startGame();

        //close panels of choose positions
        LinearLayout linearLayoutChoosePanel = findViewById(R.id.choosePanel);
        linearLayoutChoosePanel.setVisibility(View.GONE);
        Button buttonStartGame = findViewById(R.id.btStartGame);
        buttonStartGame.setVisibility(View.GONE);

        //open panel of players
        LinearLayout linearLayoutPlayerPanel = findViewById(R.id.playersPanel);
        linearLayoutPlayerPanel.setVisibility(View.VISIBLE);

        //random - 0 or 1
        navalBattleGame.setTeamATurn(Math.random() < 0.5);

        navalBattleGame.setAIPositions();

        battlefieldView.invalidate();
        Log.d("onStartGame", "Game Started. PlayerA playing:" + navalBattleGame.isTeamATurn());

    }


    public void onCloseSetPositions(View v) {

        Log.d("onCloseSetPositions", "Clicked onCloseSetPositions");

        finish();
    }

}