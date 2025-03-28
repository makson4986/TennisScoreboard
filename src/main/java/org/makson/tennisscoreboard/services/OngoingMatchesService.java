package org.makson.tennisscoreboard.services;

import org.makson.tennisscoreboard.models.Match;
import org.makson.tennisscoreboard.models.Player;
import org.makson.tennisscoreboard.repositories.PlayerRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {
    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();
    private final PlayerRepository playerRepository = PlayerRepository.getInstance();
    private final Map<UUID, Match> currentMatches = new ConcurrentHashMap<>();

    private OngoingMatchesService() {
    }

    public Match getMatch(UUID uuid) {
        return currentMatches.get(uuid);
    }

    public UUID createMatch(String playerOne, String playerTwo) {
        var player1 = playerRepository.findByName(playerOne);
        var player2 = playerRepository.findByName(playerTwo);

        if (player1.isEmpty() || player2.isEmpty()) {
            player1 = Optional.of(playerRepository.save(Player.builder().name(playerOne).build()));
            player2 = Optional.of(playerRepository.save(Player.builder().name(playerTwo).build()));
        }

        UUID uuid = UUID.randomUUID();
        Match newMatch = new Match(player1.get(), player2.get());
        currentMatches.put(uuid, newMatch);
        return uuid;
    }

    public static OngoingMatchesService getInstance() {
        return INSTANCE;
    }
}

