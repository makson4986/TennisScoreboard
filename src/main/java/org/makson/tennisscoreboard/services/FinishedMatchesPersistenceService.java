package org.makson.tennisscoreboard.services;

import org.makson.tennisscoreboard.models.Match;
import org.makson.tennisscoreboard.models.Matches;
import org.makson.tennisscoreboard.models.Player;
import org.makson.tennisscoreboard.repositories.MatchesRepository;

public class FinishedMatchesPersistenceService {
    private static final FinishedMatchesPersistenceService INSTANCE = new FinishedMatchesPersistenceService();
    private final MatchesRepository matchesRepository = MatchesRepository.getInstance();

    private FinishedMatchesPersistenceService() {
    }

    public void saveMatch(Match match) {
        Matches matches = Matches.builder()
                .player1(match.getPlayerOne())
                .player2(match.getPlayerTwo())
                .winner(defineWinner(match))
                .build();

        matchesRepository.save(matches);
    }

    private Player defineWinner(Match match) {
        if (match.getScorePlayerOne().getSets() == 2) {
            return match.getPlayerOne();
        }

        return match.getPlayerTwo();
    }

    public static FinishedMatchesPersistenceService getInstance() {
        return INSTANCE;
    }
}
