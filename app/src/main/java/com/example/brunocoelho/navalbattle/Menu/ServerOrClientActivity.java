package com.example.brunocoelho.navalbattle.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.example.brunocoelho.navalbattle.Game.BattlefieldActivity;
import com.example.brunocoelho.navalbattle.Game.Constants;
import com.example.brunocoelho.navalbattle.Game.NavalBattleGame;
import com.example.brunocoelho.navalbattle.R;

public class ServerOrClientActivity extends Activity {

    NavalBattleGame navalBattleGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_or_client);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            navalBattleGame = (NavalBattleGame) savedInstanceState.getSerializable("restoredNavalBattleGame");
        } else {
            // when is new game...
            navalBattleGame = (NavalBattleGame)getIntent().getSerializableExtra("navalBattleGame");
            navalBattleGame.setTeamsPositions();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putSerializable("restoredNavalBattleGame", navalBattleGame);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onCloseServerOrClient(View v) {

        Log.d("onCloseNewGame", "Clicked onCloseNewGame");

        finish();
    }
    public void onServer(View v) {

        Log.d("onServer", "Clicked onServer");
        Intent intent=new Intent(this, BattlefieldActivity.class);
        intent.putExtra("mode", Constants.SERVER);
        startActivity(intent);


        finish();
    }
    public void onClient(View v) {

        Log.d("onServer", "Clicked onServer");
        Intent intent=new Intent(this, BattlefieldActivity.class);
        intent.putExtra("mode", Constants.CLIENT);
        startActivity(intent);
    }


}
