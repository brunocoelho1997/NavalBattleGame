package com.example.brunocoelho.navalbattle.Game;

import com.example.brunocoelho.navalbattle.Profiles.Profile;
import com.example.brunocoelho.navalbattle.Game.Models.Team;

import java.io.Serializable;

public class Data implements Serializable{

    private Team teamA;
    private Team teamB;
    private Profile profileA;
    private Profile profileB;


    private boolean started;
    //if 1 then teamA, if 0 then teamB
    private boolean teamATurn;
    //if 1 then teamA, if 0 then teamB
    private boolean amITeamA;
    private boolean twoPlayer;


    public Data() {
        teamA = new Team();
        teamB = new Team();
        started = false;
    }

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isTeamATurn() {
        return teamATurn;
    }

    public void setTeamATurn(boolean teamATurn) {
        this.teamATurn = teamATurn;
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

    public Profile getProfileA() {
        return profileA;
    }

    public void setProfileA(Profile profileA) {
        this.profileA = profileA;
    }

    public Profile getProfileB() {
        return profileB;
    }

    public void setProfileB(Profile profileB) {
        this.profileB = profileB;
    }
}
