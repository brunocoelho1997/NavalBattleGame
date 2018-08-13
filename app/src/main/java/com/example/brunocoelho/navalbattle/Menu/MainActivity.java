package com.example.brunocoelho.navalbattle.Menu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.brunocoelho.navalbattle.Game.NavalBattleGame;
import com.example.brunocoelho.navalbattle.Profiles.File;
import com.example.brunocoelho.navalbattle.Profiles.ProfilesListActivity;
import com.example.brunocoelho.navalbattle.R;

public class MainActivity extends AppCompatActivity {

    private NavalBattleGame navalBattleGame;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getBaseContext();

        navalBattleGame = new NavalBattleGame();


    }

    public void onNewGame(View v) {
        Intent intent = new Intent(this, newGameActivity.class);
        intent.putExtra("navalBattleGame", navalBattleGame);
        startActivity(intent);
        Log.d("onNewGame", "Aderi onNewGame");
        Log.d("onNewGame", "Selected Profile: " + File.loadSelectedProfile(context));

    }

    public void onProfiles(View v) {

        Intent intent = new Intent(this, ProfilesListActivity.class);
        intent.putExtra("navalBattleGame", navalBattleGame);

        startActivity(intent);

        Log.d("onProfiles", "Aderi onProfiles");

    }
}
