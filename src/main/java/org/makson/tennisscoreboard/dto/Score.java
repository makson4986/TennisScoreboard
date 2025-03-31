package org.makson.tennisscoreboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class Score {
    @Setter
    private int points;
    private int games;
    private int sets;

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
