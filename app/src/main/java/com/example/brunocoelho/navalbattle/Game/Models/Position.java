package com.example.brunocoelho.navalbattle.Game.Models;

import com.example.brunocoelho.navalbattle.Game.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Position implements Serializable{
    private int number, letter;
    private int color;
    private String objectType;

    public Position() {
        number = -1;
        letter = -1;
    }

    public Position(int number, int letter) {
        this.number = number;
        this.letter = letter;
        this.color = Constants.FULL_SQUARE;
        this.objectType = Constants.CLASS_POSITION; //className

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

    @Override
    public String toString() {

        /*
        Tem de ser lido como: linha x coluna.
         */
        return "[" + (number) + ";" +  (char)(letter+96)  + "] - " + number + ";" +letter;
    }

    public boolean isAdjacent(Position onUpPosition) {

        List<Position> adjacentPositions = new ArrayList<>();

        adjacentPositions.add(new Position(this.getNumber()-1, this.getLetter()));
        adjacentPositions.add(new Position(this.getNumber()-1, this.getLetter()+1));
        adjacentPositions.add(new Position(this.getNumber()-1, this.getLetter()-1));
        adjacentPositions.add(new Position(this.getNumber(), this.getLetter()+1));
        adjacentPositions.add(new Position(this.getNumber(), this.getLetter()-1));
        adjacentPositions.add(new Position(this.getNumber()+1, this.getLetter()+1));
        adjacentPositions.add(new Position(this.getNumber()+1, this.getLetter()));
        adjacentPositions.add(new Position(this.getNumber()+1, this.getLetter()-1));

        if(adjacentPositions.contains(onUpPosition))
            return true;

        return false;
    }
}
