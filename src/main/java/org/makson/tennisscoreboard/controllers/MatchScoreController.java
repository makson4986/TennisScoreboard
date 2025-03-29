package org.makson.tennisscoreboard.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.makson.tennisscoreboard.models.Match;
import org.makson.tennisscoreboard.services.FinishedMatchesPersistenceService;
import org.makson.tennisscoreboard.services.MatchScoreCalculationService;
import org.makson.tennisscoreboard.services.OngoingMatchesService;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreController extends HttpServlet {
    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();
    private final MatchScoreCalculationService matchScoreCalculationService = MatchScoreCalculationService.getInstance();
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService = FinishedMatchesPersistenceService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameter("uuid");
        Match currenctMatch = ongoingMatchesService.getMatch(UUID.fromString(uuid));
        req.setAttribute("currentMatch", currenctMatch);
        req.setAttribute("uuid", uuid);

        req.getRequestDispatcher(req.getContextPath() + "/match-score.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID uuid = UUID.fromString(req.getParameter("uuid"));
        String currentPlayer = req.getParameter("currentPlayer");
        Match currenctMatch = ongoingMatchesService.getMatch(uuid);

        matchScoreCalculationService.calculateMatchScore(currenctMatch, currentPlayer);

        req.setAttribute("currentMatch", currenctMatch);
        req.setAttribute("uuid", uuid);

        if (currenctMatch.isFinished()) {
            ongoingMatchesService.deleteMatch(uuid);
            finishedMatchesPersistenceService.saveMatch(currenctMatch);
            req.getRequestDispatcher(req.getContextPath() + "/match-score.jsp").forward(req, resp);
        }

        resp.sendRedirect(req.getContextPath() + "/match-score?uuid=%s".formatted(uuid));
    }
}
