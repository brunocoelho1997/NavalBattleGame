package com.example.brunocoelho.navalbattle.Menu;

import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.example.brunocoelho.navalbattle.R;

public class CreditsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/mastodontus.ttf");
        ((TextView)findViewById(R.id.tvCredits1)).setTypeface(font);
        ((TextView)findViewById(R.id.tvCredits2)).setTypeface(font);
        ((TextView)findViewById(R.id.tvCredits3)).setTypeface(font);
        ((TextView)findViewById(R.id.tvCredits4)).setTypeface(font);
    }

}
