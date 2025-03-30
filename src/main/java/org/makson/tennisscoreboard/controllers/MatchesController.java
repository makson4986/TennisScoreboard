package org.makson.tennisscoreboard.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.makson.tennisscoreboard.dto.Page;
import org.makson.tennisscoreboard.exceptions.BadRequestException;
import org.makson.tennisscoreboard.models.Matches;
import org.makson.tennisscoreboard.services.FinishedMatchesPersistenceService;

import java.io.IOException;
import java.util.List;

@WebServlet("/matches")
public class MatchesController extends BaseController {
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService = FinishedMatchesPersistenceService.getInstance();
    private final int MAX_MATCHES_PER_PAGES = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filterByName = req.getParameter("filterByName");
        String pageParameter = req.getParameter("page");

        int maxPages = finishedMatchesPersistenceService.getAmountMaxPages(MAX_MATCHES_PER_PAGES);
        int pageNumber = tryParsePageToInt(pageParameter, maxPages);
        int offset = (pageNumber - 1) * MAX_MATCHES_PER_PAGES;

        List<Matches> finishedMatches = finishedMatchesPersistenceService.getFinishedMatchesPaginated(new Page(offset, maxPages, filterByName));


        req.setAttribute("finishedMatches", finishedMatches);
        req.setAttribute("currentPageNumber", pageNumber);
        req.setAttribute("maxPages", maxPages);

        req.getRequestDispatcher("/matches.jsp").forward(req, resp);
    }

    private int tryParsePageToInt(String page, int maxPages) {
        int pageNumber;

        if (page == null) {
            return 1;
        }

        try {
            pageNumber = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid page number");
        }

        if (pageNumber < 1 || pageNumber > maxPages) {
            throw new BadRequestException("Page number cannot be less than 1 or greater than the maximum");
        }

        return pageNumber;
    }
}
