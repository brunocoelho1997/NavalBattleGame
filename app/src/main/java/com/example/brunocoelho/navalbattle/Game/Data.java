package com.example.brunocoelho.navalbattle.Game;

import com.example.brunocoelho.navalbattle.Game.Models.Ships.Ship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Data implements Serializable{
    private List<Ship> teamA;
    private  List<Ship> teamB;

    private boolean started;
    //if 1 then teamA, if 0 then teamB
    private boolean playerATurn;
    //if 1 then teamA, if 0 then teamB
    private boolean amITeamA;
    private boolean twoPlayer;

    public Data() {
        teamA = new ArrayList<>();
        teamB = new ArrayList<>();
        started = false;
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

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isPlayerATurn() {
        return playerATurn;
    }

    public void setPlayerATurn(boolean playerATurn) {
        this.playerATurn = playerATurn;
    }

    public boolean isTwoPlayer() {
        return twoPlayer;
    }

    public void setTwoPlayer(boolean twoPlayer) {
        this.twoPlayer = twoPlayer;
    }

    public boolean isAmITeamA() {
        return amITeamA;
    }

    public void setAmITeamA(boolean amITeamA) {
        this.amITeamA = amITeamA;
    }
}
