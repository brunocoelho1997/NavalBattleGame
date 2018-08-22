package com.example.brunocoelho.navalbattle.Game.Models;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.brunocoelho.navalbattle.Game.Constants;
import com.example.brunocoelho.navalbattle.Profiles.History;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Profile implements Serializable{

    private String objectType;
    private String name;
    private boolean selected;
    private ArrayList<History> historyList;
    private String filePathPhoto;


    public Profile(String name) {
        this.objectType = Constants.CLASS_PROFILE; //className
        this.historyList = new ArrayList<>();

        this.name = name;
    }

    public void addHistorico(History h) {
        if(historyList.size() == 10)
            historyList.remove(9);
        historyList.add(0, h);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ArrayList<History> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(ArrayList<History> historyList) {
        this.historyList = historyList;
    }

    public boolean hasHistory() {
        return historyList.size() != 0;
    }
    public History getHistory(int i) {
        return historyList.get(i);
    }

    public String [] getTitles() {
        String [] strs = new String[historyList.size()];

        for(int i = 0; i < historyList.size(); i++) {
            History h = historyList.get(i);
            strs[i] = h.getProfileTeamA() + " - " + h.getProfileTeamB();
        }
        return strs;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Profile))
            return false;

        Profile p = (Profile) obj;

        if(!name.equals(p.name) || historyList.size() != p.historyList.size())
            return false;

        for (int i = 0; i < historyList.size(); i++) {
            if(!historyList.get(i).equals(p.historyList.get(i)))
                return false;
        }

        return true;
    }

    public String getFilePathPhoto() {
        return filePathPhoto;
    }

    public void setFilePathPhoto(String filePathPhoto) {
        this.filePathPhoto = filePathPhoto;
    }

    private Uri Uri() {
        return Uri.parse("file://" + filePathPhoto);
    }

    @SuppressWarnings("deprecation")
    public Bitmap getImage(Context context, int targetW, int targetH) throws FileNotFoundException {

        Log.d(TAG, "getImage: Uri().getPath() = " + Uri().getPath());


        InputStream imageStream = context.getContentResolver().openInputStream(Uri());

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(Uri().getPath(), bmOptions);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, bmOptions);


        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        Log.d(TAG, "getImage: targetW = " + targetW);
        Log.d(TAG, "getImage: targetH = " + targetH);
        Log.d(TAG, "getImage: photoW = " + photoW);
        Log.d(TAG, "getImage: photoH = " + photoH);

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        imageStream = context.getContentResolver().openInputStream(Uri());

        return BitmapFactory.decodeStream(imageStream, null, bmOptions);
    }

    @Override
    public String toString() {
        return "Profile{" +
                "objectType='" + objectType + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
