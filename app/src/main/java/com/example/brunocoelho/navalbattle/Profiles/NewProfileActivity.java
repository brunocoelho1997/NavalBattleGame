package com.example.brunocoelho.navalbattle.Profiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brunocoelho.navalbattle.R;

public class NewProfileActivity extends Activity {


    private String name;

    private String stringAux;

    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);
    }

    public void onCreateProfile(View v) {
        if(validations()) {
            Profile p = new Profile(name);
            p.setFilePathPhoto(filePath);
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

        startActivityForResult(intent, 1);
        Log.d("onTakePhoto", "Aderi onTakePhoto");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                if(data.hasExtra("filePath")) {

                    Log.d("onTakePhoto", (String) data.getSerializableExtra("filePath"));

                    filePath = (String) data.getSerializableExtra("filePath");

                    java.io.File imgFile = new  java.io.File(filePath);

                    if(imgFile.exists()){

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        ImageView myImage = (ImageView) findViewById(R.id.ivPhoto);

                        myImage.setImageBitmap(myBitmap);


                    }
                }
            }
        }
    }

}
