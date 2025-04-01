package org.makson.tennisscoreboard.services;

import org.makson.tennisscoreboard.dto.Match;
import org.makson.tennisscoreboard.dto.Point;
import org.makson.tennisscoreboard.dto.Score;

public class MatchScoreCalculationService {
    private static final MatchScoreCalculationService INSTANCE = new MatchScoreCalculationService();
    private final float COST_ONE_POINT = 15F;
    private final int DEFAULT_AMOUNT_GAMES = 6;
    private final int DEFAULT_AMOUNT_SETS = 2;
    private final int DEFAULT_MAX_POINTS_FOR_WIN_IN_TIEBREAK = 7;

    private MatchScoreCalculationService() {
    }

    public void calculateMatchScore(Match match, String currentPlayer) {
        Score scoreCurrentPlayer;
        Score scoreOpponent;

        if (currentPlayer.equals("playerOne")) {
            scoreCurrentPlayer = match.getScorePlayerOne();
            scoreOpponent = match.getScorePlayerTwo();
        } else {
            scoreCurrentPlayer = match.getScorePlayerTwo();
            scoreOpponent = match.getScorePlayerOne();
        }

        if (!match.isDeuce() && !match.isTieBreak()) {
            setPlayerPoints(match, scoreCurrentPlayer, scoreOpponent);
        } else if (match.isDeuce()) {
            calculateMatchScoreInDeuce(match, scoreCurrentPlayer, scoreOpponent);
        } else if (match.isTieBreak()) {
            calculateMatchScoreInTieBreak(match, scoreCurrentPlayer, scoreOpponent);
        }
    }

    private void setPlayerPoints(Match currentMatch, Score scoreCurrentPlayer, Score scoreOpponent) {
        if (!scoreCurrentPlayer.getPoints().equals(Point.WIN_POINT)) {
            int currentPoint = (int) Math.ceil(Integer.parseInt(scoreCurrentPlayer.getPoints().getPoint()) / COST_ONE_POINT);
            scoreCurrentPlayer.setPoints(Point.values()[currentPoint + 1]);
        }

        if (scoreCurrentPlayer.getPoints().equals(scoreOpponent.getPoints()) &&
                scoreCurrentPlayer.getPoints() == Point.FORTY) {
            currentMatch.setDeuce(true);
        }

        if (hasPointsAdvantage(scoreCurrentPlayer, scoreOpponent)) {
            setPlayerGames(currentMatch, scoreCurrentPlayer, scoreOpponent);
        }
    }

    private void setPlayerGames(Match currentMatch, Score scoreCurrentPlayer, Score scoreOpponent) {
        scoreCurrentPlayer.addGames(1);

        if (scoreCurrentPlayer.getGames() == scoreOpponent.getGames() &&
                scoreCurrentPlayer.getGames() == DEFAULT_AMOUNT_GAMES) {
            currentMatch.setTieBreak(true);
        }

        scoreCurrentPlayer.resetPoints();
        scoreOpponent.resetPoints();

        if (hasGamesAdvantage(scoreCurrentPlayer)) {
            setPlayerSets(currentMatch, scoreCurrentPlayer, scoreOpponent);
        }
    }

    private void setPlayerSets(Match currentMatch, Score scoreCurrentPlayer, Score scoreOpponent) {
        scoreCurrentPlayer.addSets(1);
        scoreCurrentPlayer.resetGames();
        scoreOpponent.resetGames();

        if (scoreCurrentPlayer.getSets() == DEFAULT_AMOUNT_SETS) {
            currentMatch.setFinished(true);
        }
    }

    private void calculateMatchScoreInDeuce(Match currentMatch, Score scoreCurrentPlayer, Score scoreOpponent) {
        if (hasDeuceAdvantage(scoreCurrentPlayer)) {
            scoreCurrentPlayer.resetPoints();
            scoreOpponent.resetPoints();
            currentMatch.setDeuce(false);
            setPlayerGames(currentMatch, scoreCurrentPlayer, scoreOpponent);
            return;
        } else if (!scoreOpponent.getPoints().equals(Point.ADVANTAGE)){
            scoreCurrentPlayer.setPoints(Point.ADVANTAGE);
        }

        scoreOpponent.setPoints(Point.FORTY);
    }

    private void calculateMatchScoreInTieBreak(Match currentMatch, Score scoreCurrentPlayer, Score scoreOpponent) {
        scoreCurrentPlayer.addTieBreaksPoints(1);

        if (hasTieBreakAdvantage(scoreCurrentPlayer, scoreOpponent)) {
            currentMatch.setTieBreak(false);

            scoreCurrentPlayer.resetPoints();
            scoreOpponent.resetPoints();

            scoreCurrentPlayer.resetTieBreaksPoints();
            scoreOpponent.resetTieBreaksPoints();

            setPlayerSets(currentMatch, scoreCurrentPlayer, scoreOpponent);
        }
    }

    private boolean hasPointsAdvantage(Score scoreCurrentPlayer, Score scoreOpponent) {
        return (scoreCurrentPlayer.getPoints().equals(Point.WIN_POINT)) && !scoreOpponent.getPoints().equals(Point.WIN_POINT);
    }

    private boolean hasGamesAdvantage(Score scoreCurrentPlayer) {
        return scoreCurrentPlayer.getGames() > DEFAULT_AMOUNT_GAMES;
    }

    private boolean hasDeuceAdvantage(Score scoreCurrentPlayer) {
        return scoreCurrentPlayer.getPoints().equals(Point.ADVANTAGE);
    }

    private boolean hasTieBreakAdvantage(Score scoreCurrentPlayer, Score scoreOpponent) {
        return (scoreCurrentPlayer.getTieBreaksPoints() >= DEFAULT_MAX_POINTS_FOR_WIN_IN_TIEBREAK) &&
                scoreCurrentPlayer.getTieBreaksPoints() - scoreOpponent.getTieBreaksPoints() >= 2;
    }

    public static MatchScoreCalculationService getInstance() {
        return INSTANCE;
    }
}
