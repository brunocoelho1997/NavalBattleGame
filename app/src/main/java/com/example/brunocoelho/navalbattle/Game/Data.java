package com.example.brunocoelho.navalbattle.Game;

import com.example.brunocoelho.navalbattle.Game.Models.Ship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Data implements Serializable{
    private List<Ship> teamA;
    private  List<Ship> teamB;

    public Data() {
        teamA = new ArrayList<>();
        teamB = new ArrayList<>();
    }

    public List<Ship> getTeamA() {
        return teamA;
    }

    public void setTeamA(List<Ship> teamA) {
        this.teamA = teamA;
    }

    public List<Ship> getTeamB() {
        return teamB;
    }

    public void setTeamB(List<Ship> teamB) {
        this.teamB = teamB;
    }
}
