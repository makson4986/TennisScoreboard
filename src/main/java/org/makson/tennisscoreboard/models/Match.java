package org.makson.tennisscoreboard.models;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Match {
    private final Player playerOne;
    private final Player playerTwo;
    private final Score scorePlayerOne = new Score(40, 6, 1);
    private final Score scorePlayerTwo = new Score(0, 0, 0);
    @Setter
    private boolean isDeuce = false;
    @Setter
    private boolean isTieBreak = false;
    @Setter
    private boolean isFinished = false;


    public Match(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }
}
