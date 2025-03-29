package org.makson.tennisscoreboard.services;

import jakarta.transaction.Transactional;
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

    @Transactional
    public UUID createMatch(String playerOne, String playerTwo) {
        var player1 = playerRepository.findByName(playerOne);
        var player2 = playerRepository.findByName(playerTwo);

        if (player1.isEmpty()) {
            player1 = Optional.of(playerRepository.save(Player.builder().name(playerOne).build()));
        }

        if (player2.isEmpty()) {
            player2 = Optional.of(playerRepository.save(Player.builder().name(playerTwo).build()));
        }

        UUID uuid = UUID.randomUUID();
        Match newMatch = new Match(player1.get(), player2.get());
        currentMatches.put(uuid, newMatch);
        return uuid;
    }

    @Transactional
    public void deleteMatch(UUID uuid) {
        currentMatches.remove(uuid);
    }

    public static OngoingMatchesService getInstance() {
        return INSTANCE;
    }

//    public static void main(String[] args) {
//        Player player1 = Player.builder().name("player1").build();
//        PlayerRepository.getInstance().save(player1);
//    }
}

