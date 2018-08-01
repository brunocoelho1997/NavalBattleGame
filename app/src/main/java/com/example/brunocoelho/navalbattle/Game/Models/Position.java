package com.example.brunocoelho.navalbattle.Game.Models;

import com.example.brunocoelho.navalbattle.Game.Constants;

import java.io.Serializable;

public class Position implements Serializable{
    private int number, letter;
    private int color;
    private boolean destroyed;

    public Position() {
        number = -1;
        letter = -1;
    }

    public Position(int number, int letter) {
        this.number = number;
        this.letter = letter;
        this.color = Constants.FULL_SQUARE;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getLetter() {
        return letter;
    }

    public void setLetter(int letter) {
        this.letter = letter;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.letter == ((Position)obj).getLetter())
        {
            if(this.number == ((Position)obj).getNumber())
                return true;
        }

        return false;

    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    @Override
    public String toString() {

        /*
        Tem de ser lido como: linha x coluna.
         */
        return "[" + (number) + ";" +  (char)(letter+96)  + "] - " + number + ";" +letter  ;
    }
}
