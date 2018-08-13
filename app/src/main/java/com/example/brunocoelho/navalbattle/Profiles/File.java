package com.example.brunocoelho.navalbattle.Profiles;

import android.content.Context;
import android.util.Log;

import com.example.brunocoelho.navalbattle.Game.Models.Profile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static com.example.brunocoelho.navalbattle.Game.Constants.FILE_NAME;

public class File {

    public static void saveProfiles(Context context, ArrayList<Profile> profiles) {
        FileOutputStream fos = null;
        ObjectOutputStream os = null;

        try {
            fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(profiles.size());
            for (Profile p : profiles) {
                os.writeObject(p);
            }
            os.close();
            fos.close();

            Log.i("teste", "All profiles was saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Profile> loadProfiles(Context context) {
        ArrayList<Profile> profiles;

        try {
            FileInputStream fis = null;
            fis = context.openFileInput(FILE_NAME);
            ObjectInputStream is = new ObjectInputStream(fis);

            profiles = new ArrayList<>();
            int size = (int) is.readObject();

            for(int i = 0; i < size; i++) {
                profiles.add((Profile) is.readObject());
            }
            is.close();
            fis.close();
        } catch(Exception e) {
            profiles = new ArrayList<>();
        }

        return profiles;
    }
}
