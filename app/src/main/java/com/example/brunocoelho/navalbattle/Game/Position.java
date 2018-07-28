package com.example.brunocoelho.navalbattle.Game;

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

    @Override
    public String toString() {

        /*
        Tem de ser lido como: linha x coluna.
         */
        return "[" + (letter+1) + ";" +  (char)(number+97)  + "] - " + letter + ";" +number  ;
    }
}
