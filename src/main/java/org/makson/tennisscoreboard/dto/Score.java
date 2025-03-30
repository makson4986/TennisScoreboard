package org.makson.tennisscoreboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Score {
    @Setter
    private int points;
    private int games;
    private int sets;

    public Score() {
    }

    public Score(int points, int games, int sets) {
        this.points = points;
        this.games = games;
        this.sets = sets;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void addGames(int games) {
        this.games += games;
    }

    public void addSets(int sets) {
        this.sets += sets;
    }

    public void resetPoints() {
        points = 0;
    }

    public void resetGames() {
        games = 0;
    }
}
