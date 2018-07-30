package com.example.brunocoelho.navalbattle.Game;

import android.util.Log;

import com.example.brunocoelho.navalbattle.Game.Models.Position;
import com.example.brunocoelho.navalbattle.Game.Models.Ship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NavalBattleGame implements Serializable{

    private  List<Ship> teamA;
    private  List<Ship> teamB;

    public NavalBattleGame() {
        teamA = new ArrayList<>();
        teamB = new ArrayList<>();
    }


    public Ship getShip(Position position)
    {
        for(Ship ship: teamA)
        {
            if(ship.getPositionList().contains(position))
                return ship;

        }
        for(Ship ship: teamB)
        {
            if(ship.getPositionList().contains(position))
                return ship;
        }
        return null;
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
