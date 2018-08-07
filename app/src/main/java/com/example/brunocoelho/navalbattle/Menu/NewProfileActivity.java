package com.example.brunocoelho.navalbattle.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.brunocoelho.navalbattle.Game.Models.Profile;
import com.example.brunocoelho.navalbattle.R;

public class NewProfileActivity extends Activity {


    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);
    }

    public void onCreateProfile(View v) {
        if(validations()) {
            Profile p = new Profile(name);
            Intent intent = new Intent();
            intent.putExtra("NewProfile", p);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private boolean validations() {
        EditText ed = (EditText) findViewById(R.id.eName);

        name = ed.getText().toString();
        if(name.trim().isEmpty()) {
            ed.requestFocus();
            ((TextView)findViewById(R.id.tvRequired)).setVisibility(TextView.VISIBLE);
            return false;
        }
        return true;
    }

}
