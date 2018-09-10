package com.example.brunocoelho.navalbattle.Menu;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/mastodontus.ttf");
        ((TextView)findViewById(R.id.btServer)).setTypeface(font);
        ((TextView)findViewById(R.id.btClient)).setTypeface(font);

        navalBattleGame = (NavalBattleGame)getIntent().getSerializableExtra("navalBattleGame");

    }

    public void onCloseServerOrClient(View v) {

        Log.d("onCloseNewGame", "Clicked onCloseNewGame");

        finish();
    }
    public void onServer(View v) {

        Log.d("onServer", "Clicked onServer");
        navalBattleGame.setAmITeamA(true);
        navalBattleGame.setProfileTeamA(navalBattleGame.getSelectedProfile());


        Intent intent=new Intent(this, BattlefieldActivity.class);
        intent.putExtra("navalBattleGame", navalBattleGame);
        intent.putExtra("mode", Constants.SERVER);
        startActivity(intent);


    }
    public void onClient(View v) {

        Log.d("onServer", "Clicked onServer");
        navalBattleGame.setAmITeamA(false);
        navalBattleGame.setProfileTeamB(navalBattleGame.getSelectedProfile());

        Intent intent=new Intent(this, BattlefieldActivity.class);
        intent.putExtra("navalBattleGame", navalBattleGame);

        intent.putExtra("mode", Constants.CLIENT);
        startActivity(intent);
    }


}
