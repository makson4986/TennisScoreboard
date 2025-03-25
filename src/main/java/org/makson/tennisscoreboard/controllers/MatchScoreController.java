package org.makson.tennisscoreboard.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.makson.tennisscoreboard.Match;
import org.makson.tennisscoreboard.services.OngoingMatchesService;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreController extends HttpServlet {
    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameter("uuid");
        Match currenctMatch = ongoingMatchesService.getMatch(UUID.fromString(uuid));
        req.setAttribute("currentMatch", currenctMatch);

        req.getRequestDispatcher(req.getContextPath() + "/match-score.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameter("uuid");
        Match currenctMatch = ongoingMatchesService.getMatch(UUID.fromString(uuid));

        req.setAttribute("currentMatch", currenctMatch);
        req.setAttribute("uuid", uuid);

        resp.sendRedirect(req.getContextPath() + "/match-score?uuid=%s".formatted(uuid));
    }
}
