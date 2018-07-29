package com.example.brunocoelho.navalbattle.Game.Activities;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.brunocoelho.navalbattle.Game.BattlefieldView;
import com.example.brunocoelho.navalbattle.R;

public class GameActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private BattlefieldView battlefieldView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        frameLayout = findViewById(R.id.battleField);
        battlefieldView = new BattlefieldView(this);
        frameLayout.addView(battlefieldView);

    }


}
