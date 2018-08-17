package com.example.brunocoelho.navalbattle.Profiles;

import com.example.brunocoelho.navalbattle.Game.Models.Profile;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class History {
    private Profile teamB;
    private Profile teamA;
    private Calendar date;
    private Result winner; // true - jogadorA - false - jogadorB;

    public History(Profile teamB, Profile teamA) {
        this.teamB = teamB;
        this.teamA = teamA;
    }
    public History() {
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

        return teamA.equals(h.teamA) && teamB.equals(h.teamB) && date.equals(h.date) && winner == h.winner;
    }

    @Override
    public String toString() {
        return "Team A: " + teamA+ " Team B: " + teamB + " Vencedor: " + winner;
    }

    public Profile getTeamB() {
        return teamB;
    }

    public void setTeamB(Profile teamB) {
        this.teamB = teamB;
    }

    public Profile getTeamA() {
        return teamA;
    }

    public void setTeamA(Profile teamA) {
        this.teamA = teamA;
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
