package com.example.brunocoelho.navalbattle.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.brunocoelho.navalbattle.Game.GameActivity;
import com.example.brunocoelho.navalbattle.Game.SetPositionsActivity;
import com.example.brunocoelho.navalbattle.R;

public class newGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

    }

    public void onOfflineGame(View v) {
        Intent intent = new Intent(this, SetPositionsActivity.class);
        startActivity(intent);

        Log.d("onOfflineGame", "Aderi onOfflineGame");

    }

    public void onOnlineGame(View v) {
//        Intent intent = new Intent(this, ListaPerfis.class);
//        startActivity(intent);

        Log.d("onOnlineGame", "Aderi onOnlineGame");

    }

}
