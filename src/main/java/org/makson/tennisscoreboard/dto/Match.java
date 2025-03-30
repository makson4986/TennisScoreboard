package org.makson.tennisscoreboard.dto;

import lombok.Getter;
import lombok.Setter;
import org.makson.tennisscoreboard.models.Player;

@Getter
public class Match {
    private final Player playerOne;
    private final Player playerTwo;
    private final Score scorePlayerOne = new Score(0, 0, 0);
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
