package com.example.brunocoelho.navalbattle.Profiles;

import com.example.brunocoelho.navalbattle.Game.Models.Profile;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class History implements Serializable{
    private Profile profileTeamB;
    private Profile profileTeamA;
    private Calendar date;
    private Result winner; // true - jogadorA - false - jogadorB;

    public History(Profile teamA, Profile teamB) {
        this.profileTeamB = teamB;
        this.profileTeamA = teamA;
        winner = Result.NotDefined;
        date = GregorianCalendar.getInstance();
    }

    public String getDate() {
        return String.format("%02d/%02d/%04d",
                date.get(GregorianCalendar.DAY_OF_MONTH),
                date.get(GregorianCalendar.MONTH) + 1,
                date.get(GregorianCalendar.YEAR));
    }
    public String getHour() {
        return String.format("%02d:%02d",
                date.get(GregorianCalendar.HOUR),
                date.get(GregorianCalendar.MINUTE));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof History))
            return false;

        History h = (History) obj;

        return profileTeamA.equals(h.profileTeamA) && profileTeamB.equals(h.profileTeamB) && date.equals(h.date) && winner == h.winner;
    }

    @Override
    public String toString() {
        return "Team A: " + profileTeamA + " Team B: " + profileTeamB + " Vencedor: " + winner;
    }

    public Profile getProfileTeamB() {
        return profileTeamB;
    }

    public void setProfileTeamB(Profile profileTeamB) {
        this.profileTeamB = profileTeamB;
    }

    public Profile getProfileTeamA() {
        return profileTeamA;
    }

    public void setProfileTeamA(Profile profileTeamA) {
        this.profileTeamA = profileTeamA;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Result getWinner() {
        return winner;
    }

    public void setWinner(Result winner) {
        this.winner = winner;
    }
}
