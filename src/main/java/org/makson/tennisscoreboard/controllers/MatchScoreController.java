package org.makson.tennisscoreboard.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.makson.tennisscoreboard.dto.Match;
import org.makson.tennisscoreboard.exceptions.BadRequestException;
import org.makson.tennisscoreboard.exceptions.DataNotFoundException;
import org.makson.tennisscoreboard.services.FinishedMatchesPersistenceService;
import org.makson.tennisscoreboard.services.MatchScoreCalculationService;
import org.makson.tennisscoreboard.services.OngoingMatchesService;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreController extends BaseController {
    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();
    private final MatchScoreCalculationService matchScoreCalculationService = MatchScoreCalculationService.getInstance();
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService = FinishedMatchesPersistenceService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID uuid = tryParseUuidFromString(req.getParameter("uuid"));
        Match currentMatch;

        if (ongoingMatchesService.isMatchExist(uuid)) {
            currentMatch = ongoingMatchesService.getMatch(uuid);
        } else {
            throw new DataNotFoundException("The match does not exist or has ended");
        }

        setMatchAttribute(req, currentMatch, uuid);

        req.getRequestDispatcher("/match-score.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID uuid = tryParseUuidFromString(req.getParameter("uuid"));
        String currentPlayer = req.getParameter("currentPlayer");
        Match currentMatch;

        if (ongoingMatchesService.isMatchExist(uuid)) {
            currentMatch = ongoingMatchesService.getMatch(uuid);
        } else {
            throw new DataNotFoundException("The match does not exist or has ended");
        }

        matchScoreCalculationService.calculateMatchScore(currentMatch, currentPlayer);

        setMatchAttribute(req, currentMatch, uuid);

        if (currentMatch.isFinished()) {
            ongoingMatchesService.deleteMatch(uuid);
            finishedMatchesPersistenceService.saveMatch(currentMatch);
            req.getRequestDispatcher("/finished-match.jsp").forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/match-score?uuid=%s".formatted(uuid));
        }


    }

    private void setMatchAttribute(HttpServletRequest req, Match match, UUID uuid) {
        req.setAttribute("currentMatch", match);
        req.setAttribute("uuid", uuid);
    }

    private UUID tryParseUuidFromString(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("UUID is not correct!");
        } catch (NullPointerException e) {
            throw new BadRequestException("UUID is missing!");
        }
    }
}
