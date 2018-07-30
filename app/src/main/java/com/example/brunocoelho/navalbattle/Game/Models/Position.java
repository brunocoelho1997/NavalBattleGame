package com.example.brunocoelho.navalbattle.Game.Models;

import java.io.Serializable;

public class Position implements Serializable{
    private int number, letter;

    public Position() {
        number = -1;
        letter = -1;
    }

    public Position(int number, int letter) {
        this.number = number;
        this.letter = letter;
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

    @Override
    public boolean equals(Object obj) {
        if(this.letter == ((Position)obj).getLetter())
        {
            if(this.number == ((Position)obj).getNumber())
                return true;
        }

        return false;
    }

    @Override
    public String toString() {

        /*
        Tem de ser lido como: linha x coluna.
         */
        return "[" + (number+1) + ";" +  (char)(letter+97)  + "] - " + number + ";" +letter  ;
    }
}
