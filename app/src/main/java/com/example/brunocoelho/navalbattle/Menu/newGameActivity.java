package com.example.brunocoelho.navalbattle.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.brunocoelho.navalbattle.Game.Activities.SetPositionsActivity;
import com.example.brunocoelho.navalbattle.Game.NavalBattleGame;
import com.example.brunocoelho.navalbattle.R;

public class newGameActivity extends AppCompatActivity {

    private NavalBattleGame navalBattleGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        navalBattleGame = new NavalBattleGame();

    }

    public void onOfflineGame(View v) {
        Intent intent = new Intent(this, SetPositionsActivity.class);
        intent.putExtra("navalBattleGame", navalBattleGame);
        startActivity(intent);

        Log.d("onOfflineGame", "Aderi onOfflineGame");

    }

    public void onOnlineGame(View v) {
//        Intent intent = new Intent(this, ListaPerfis.class);
//        startActivity(intent);

        Log.d("onOnlineGame", "Aderi onOnlineGame");

    }

}
