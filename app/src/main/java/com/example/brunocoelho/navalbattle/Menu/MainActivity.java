package com.example.brunocoelho.navalbattle.Menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.brunocoelho.navalbattle.Game.NavalBattleGame;
import com.example.brunocoelho.navalbattle.R;

public class MainActivity extends AppCompatActivity {

    private NavalBattleGame navalBattleGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navalBattleGame = new NavalBattleGame();


    }

    public void onNewGame(View v) {
        Intent intent = new Intent(this, newGameActivity.class);

        intent.putExtra("navalBattleGame", navalBattleGame);


        startActivity(intent);

        Log.d("onNewGame", "Aderi onNewGame");
    }

    public void onProfiles(View v) {

        Intent intent = new Intent(this, ProfilesList.class);
        intent.putExtra("navalBattleGame", navalBattleGame);

        startActivity(intent);

        Log.d("onProfiles", "Aderi onProfiles");

    }
}
