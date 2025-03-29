package org.makson.tennisscoreboard.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.makson.tennisscoreboard.models.Matches;
import org.makson.tennisscoreboard.services.FinishedMatchesPersistenceService;

import java.io.IOException;
import java.util.List;

@WebServlet("/matches")
public class MatchesController extends HttpServlet {
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService = FinishedMatchesPersistenceService.getInstance();
    private final int MAX_MATCHES_PER_PAGES = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filterByName = req.getParameter("filterByName");

        int pageNumber = Integer.parseInt(req.getParameter("page"));
        int offset = (pageNumber - 1) * MAX_MATCHES_PER_PAGES;
        int maxPages = finishedMatchesPersistenceService.getAmountMaxPages(MAX_MATCHES_PER_PAGES);
        List<Matches> finishedMatches;

        if (filterByName == null) {
            finishedMatches = finishedMatchesPersistenceService.getFinishedMatchesPaginated(offset, MAX_MATCHES_PER_PAGES);
        } else {
            finishedMatches = finishedMatchesPersistenceService.getFinishedMatchesPaginated(offset, MAX_MATCHES_PER_PAGES, filterByName);
        }

        req.setAttribute("finishedMatches", finishedMatches);
        req.setAttribute("currentPageNumber", pageNumber);
        req.setAttribute("maxPages", maxPages);

        req.getRequestDispatcher(req.getContextPath() + "/matches.jsp").forward(req, resp);
    }
}
