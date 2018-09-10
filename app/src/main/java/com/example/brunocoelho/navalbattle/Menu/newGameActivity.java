package com.example.brunocoelho.navalbattle.Menu;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.brunocoelho.navalbattle.Game.BattlefieldActivity;
import com.example.brunocoelho.navalbattle.Game.NavalBattleGame;
import com.example.brunocoelho.navalbattle.R;

public class newGameActivity extends AppCompatActivity {

    private NavalBattleGame navalBattleGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        navalBattleGame = (NavalBattleGame)getIntent().getSerializableExtra("navalBattleGame");

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/mastodontus.ttf");
        ((Button)findViewById(R.id.btOfflineGame)).setTypeface(font);
        ((Button)findViewById(R.id.btOnlineGame)).setTypeface(font);
    }

    public void onOfflineGame(View v) {

        navalBattleGame.setTwoPlayer(false);
        navalBattleGame.setAmITeamA(true);
        navalBattleGame.setProfileTeamA(navalBattleGame.getSelectedProfile());
        navalBattleGame.setProfileTeamB(navalBattleGame.generateAIProfile());

        Intent intent = new Intent(this, BattlefieldActivity.class);
        intent.putExtra("navalBattleGame", navalBattleGame);
        startActivity(intent);

        Log.d("onOfflineGame", "Aderi onOfflineGame");

    }

    public void onOnlineGame(View v) {

        navalBattleGame.setTwoPlayer(true);

        Intent intent = new Intent(this, ServerOrClientActivity.class);
        intent.putExtra("navalBattleGame", navalBattleGame);
        startActivity(intent);


        Log.d("onOnlineGame", "Aderi onOnlineGame");

    }

    public void onCloseNewGame(View v) {

        Log.d("onCloseNewGame", "Clicked onCloseNewGame");

        finish();
    }

}
