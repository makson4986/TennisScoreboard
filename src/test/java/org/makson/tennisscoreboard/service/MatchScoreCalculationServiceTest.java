package org.makson.tennisscoreboard.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.makson.tennisscoreboard.dto.Match;
import org.makson.tennisscoreboard.dto.Point;
import org.makson.tennisscoreboard.models.Player;
import org.makson.tennisscoreboard.services.MatchScoreCalculationService;

class MatchScoreCalculationServiceTest {
    private MatchScoreCalculationService matchScoreCalculationService;
    private Match match;
    private final String PLAYER1 = "playerOne";

    @BeforeEach
    void initScore() {
        match = new Match(
                Player.builder().name("test1").build(),
                Player.builder().name("test2").build()
        );

        matchScoreCalculationService = MatchScoreCalculationService.getInstance();
    }

    @Test
    @DisplayName("If player 1 wins a point at 40-40, the game does not end")
    void gameNotEndIfPlayerOneWinPointInDeuce() {
        match.getScorePlayerOne().setPoints(Point.FORTY);
        match.getScorePlayerTwo().setPoints(Point.FORTY);
        match.setDeuce(true);

        matchScoreCalculationService.calculateMatchScore(match, PLAYER1);

        Assertions.assertEquals(0, match.getScorePlayerOne().getGames());
    }

    @Test
    @DisplayName("If player 1 wins a point when the score is 40-0, then he also wins the game")
    void winGameIfPlayerOneWinPointInScore40To0() {
        match.getScorePlayerOne().setPoints(Point.FORTY);
        match.getScorePlayerTwo().setPoints(Point.ZERO);

        matchScoreCalculationService.calculateMatchScore(match, PLAYER1);

        Assertions.assertEquals(1, match.getScorePlayerOne().getGames());
    }

    @Test
    @DisplayName("If player 1 wins the game with the score 6-5 then a tie-break begins")
    void beginsTieBreakIfPlayerOneWinGameInScore6To5() {
        match.getScorePlayerOne().setPoints(Point.FORTY);
        match.getScorePlayerTwo().setPoints(Point.ZERO);

        match.getScorePlayerOne().addGames(5);
        match.getScorePlayerTwo().addGames(6);

        matchScoreCalculationService.calculateMatchScore(match, PLAYER1);

        Assertions.assertTrue(match.isTieBreak());
    }

    @Test
    @DisplayName("If player 1 does not win the tie-break at 9-8")
    void playerOneNotWinInTieBreakInScore9To8() {
        match.getScorePlayerOne().addTieBreaksPoints(8);
        match.getScorePlayerTwo().addTieBreaksPoints(8);

        match.getScorePlayerOne().addGames(6);
        match.getScorePlayerTwo().addGames(6);
        match.setTieBreak(true);

        matchScoreCalculationService.calculateMatchScore(match, PLAYER1);

        Assertions.assertTrue(match.isTieBreak());
    }
    @Test
    @DisplayName("Player 1 wins a match if he wins a set with the score 1-1 in the set")
    void playerOneWinMatchIfWinSetInScore1To1() {
        match.getScorePlayerOne().setPoints(Point.FORTY);
        match.getScorePlayerTwo().setPoints(Point.FIFTEEN);

        match.getScorePlayerOne().addGames(6);
        match.getScorePlayerTwo().addGames(2);

        match.getScorePlayerOne().addSets(1);
        match.getScorePlayerTwo().addSets(1);

        matchScoreCalculationService.calculateMatchScore(match, PLAYER1);

        Assertions.assertTrue(match.isFinished());
    }
}
