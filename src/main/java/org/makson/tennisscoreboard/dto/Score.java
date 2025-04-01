package org.makson.tennisscoreboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Score {
    @Setter
    private Point points = Point.ZERO;
    private int games;
    private int sets;
    private int tieBreaksPoints;

    public void addGames(int games) {
        this.games += games;
    }

    public void addSets(int sets) {
        this.sets += sets;
    }

    public void addTieBreaksPoints(int tieBreaksPoints) {
        this.tieBreaksPoints += tieBreaksPoints;
    }

    public void resetPoints() {
        points = Point.ZERO;
    }

    public void resetGames() {
        games = 0;
    }

    public void resetTieBreaksPoints() {
        tieBreaksPoints = 0;
    }

}
