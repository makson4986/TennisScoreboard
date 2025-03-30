package org.makson.tennisscoreboard.services;

import org.makson.tennisscoreboard.dto.Match;
import org.makson.tennisscoreboard.dto.Points;
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
        if (scoreCurrentPlayer.getPoints() < Points.LAST_POINT.getPoints()) {
            int currentPoint = (int) Math.ceil(scoreCurrentPlayer.getPoints() / COST_ONE_POINT);
            scoreCurrentPlayer.setPoints(Points.values()[currentPoint].getPoints());
        }

        if (scoreCurrentPlayer.getPoints() == scoreOpponent.getPoints() &&
                scoreCurrentPlayer.getPoints() == Points.THREE_POINT.getPoints()) {
            currentMatch.setDeuce(true);
        }

        if (hasPointsAdvantage(scoreCurrentPlayer, scoreOpponent) && (scoreCurrentPlayer.getGames() <= DEFAULT_AMOUNT_GAMES)) {
            setPlayerGames(currentMatch, scoreCurrentPlayer, scoreOpponent);
        }
    }

    private void setPlayerGames(Match currentMatch, Score scoreCurrentPlayer, Score scoreOpponent) {
        scoreCurrentPlayer.addGames(1);
        scoreCurrentPlayer.resetPoints();

        if (scoreCurrentPlayer.getGames() == scoreOpponent.getGames() &&
                scoreCurrentPlayer.getGames() == DEFAULT_AMOUNT_GAMES) {
            currentMatch.setTieBreak(true);
            scoreCurrentPlayer.resetPoints();
            scoreOpponent.resetPoints();
        }

        if ((hasGamesAdvantage(scoreCurrentPlayer)) && (scoreCurrentPlayer.getSets() < DEFAULT_AMOUNT_SETS)) {
            setPlayerSets(currentMatch, scoreCurrentPlayer);
        }
    }

    private void setPlayerSets(Match currentMatch, Score scoreCurrentPlayer) {
        scoreCurrentPlayer.addSets(1);
        scoreCurrentPlayer.resetGames();

        if (scoreCurrentPlayer.getSets() == DEFAULT_AMOUNT_SETS) {
            currentMatch.setFinished(true);
        }
    }

    private void calculateMatchScoreInDeuce(Match currentMatch, Score scoreCurrentPlayer, Score scoreOpponent) {
        scoreCurrentPlayer.addPoints(1);

        if (hasDeuceAdvantage(scoreCurrentPlayer, scoreOpponent)) {
            scoreCurrentPlayer.setPoints(Points.LAST_POINT.getPoints());
            scoreOpponent.setPoints(Points.THREE_POINT.getPoints());
            currentMatch.setDeuce(false);
            setPlayerGames(currentMatch, scoreCurrentPlayer, scoreOpponent);
        }
    }

    private void calculateMatchScoreInTieBreak(Match currentMatch, Score scoreCurrentPlayer, Score scoreOpponent) {
        scoreCurrentPlayer.addPoints(1);

        if (hasTieBreakAdvantage(scoreCurrentPlayer, scoreOpponent)) {
            currentMatch.setTieBreak(false);
            scoreCurrentPlayer.resetPoints();
            scoreOpponent.resetPoints();
            setPlayerSets(currentMatch, scoreCurrentPlayer);
        }
    }

    private boolean hasPointsAdvantage(Score scoreCurrentPlayer, Score scoreOpponent) {
        return (scoreCurrentPlayer.getPoints() == Points.LAST_POINT.getPoints()) &&
                scoreOpponent.getPoints() != Points.LAST_POINT.getPoints();
    }

    private boolean hasGamesAdvantage(Score scoreCurrentPlayer) {
        return scoreCurrentPlayer.getGames() > DEFAULT_AMOUNT_GAMES;
    }

    private boolean hasDeuceAdvantage(Score scoreCurrentPlayer, Score scoreOpponent) {
        return scoreCurrentPlayer.getPoints() - scoreOpponent.getPoints() == 2;
    }

    private boolean hasTieBreakAdvantage(Score scoreCurrentPlayer, Score scoreOpponent) {
        return (scoreCurrentPlayer.getPoints() >= DEFAULT_MAX_POINTS_FOR_WIN_IN_TIEBREAK) &&
                scoreCurrentPlayer.getPoints() - scoreOpponent.getPoints() >= 2;
    }

    public static MatchScoreCalculationService getInstance() {
        return INSTANCE;
    }
}
