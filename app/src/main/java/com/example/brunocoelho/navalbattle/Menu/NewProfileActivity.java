package com.example.brunocoelho.navalbattle.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.brunocoelho.navalbattle.Game.Models.Profile;
import com.example.brunocoelho.navalbattle.R;
import com.example.brunocoelho.navalbattle.TakePhotoActivity;

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

    public void onTakePhoto(View v) {
        Intent intent = new Intent(this, TakePhotoActivity.class);
        startActivity(intent);
        Log.d("onTakePhoto", "Aderi onTakePhoto");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                if(data.hasExtra("pathName")) {
                    Log.d("onActivityResult", "pathName:" + (String)data.getSerializableExtra("pathName"));
                    Log.d("onActivityResult", "aqui:" + (String)data.getSerializableExtra("aqui"));

                }
            }
        }
    }

}
