package com.example.brunocoelho.navalbattle.Profiles;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static com.example.brunocoelho.navalbattle.Game.Constants.FILE_NAME;

public class File {

    private java.io.File file;

    public static void saveProfiles(Context context, ArrayList<Profile> profiles) {

        String filePathString = context.getFilesDir().getPath().toString() + "/" + FILE_NAME;
        java.io.File filePath = new java.io.File(filePathString);

        FileOutputStream fos = null;
        ObjectOutputStream os = null;

        try {
            fos = new FileOutputStream(filePath);
            os = new ObjectOutputStream(fos);
            os.writeObject(profiles.size());
            for (Profile p : profiles) {
                os.writeObject(p);
            }
            os.close();
            fos.close();

            Log.i("File", "All profiles was saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Profile> loadProfiles(Context context) {
        ArrayList<Profile> profiles;

        String filePathString = context.getFilesDir().getPath().toString() + "/" + FILE_NAME;
        java.io.File filePath = new java.io.File(filePathString);


        try {
            FileInputStream fis = null;
            fis = new FileInputStream(filePath);
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

    public static Profile loadSelectedProfile(Context context)
    {
        Profile profile = null;

        String filePathString = context.getFilesDir().getPath().toString() + "/" + FILE_NAME;
        java.io.File filePath = new java.io.File(filePathString);

        try {
            FileInputStream fis = null;
            fis = new FileInputStream(filePath);
            ObjectInputStream is = new ObjectInputStream(fis);

            int size = (int) is.readObject();

            for(int i = 0; i < size; i++) {
                Profile profileAux = (Profile) is.readObject();
                if(profileAux.isSelected())
                {
                    profile = profileAux;
                    break;
                }
            }
            is.close();
            fis.close();
        } catch(Exception e) {
            Log.d("getSelectedProfile", "Error loading selected profile. Error: " + e);

            return null;
        }

        return profile;
    }

}
