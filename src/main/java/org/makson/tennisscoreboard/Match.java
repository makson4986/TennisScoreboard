package org.makson.tennisscoreboard;

import lombok.Getter;
import org.makson.tennisscoreboard.models.Player;

@Getter
public class Match {
    private final Player playerOne;
    private final Player playerTwo;
    private final Score scorePlayerOne = new Score();
    private final Score scorePlayerTwo = new Score();

    public Match(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }
}
