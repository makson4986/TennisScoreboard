package org.makson.tennisscoreboard.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.makson.tennisscoreboard.services.OngoingMatchesService;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@WebServlet("/new-match")
public class NewMatchController extends BaseController {
    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher( "/new-match.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String namePlayerOne = req.getParameter("namePlayerOne");
        String namePlayerTwo = req.getParameter("namePlayerTwo");

        String errorMessage = checkNameCorrect(namePlayerOne)
                .orElse(checkNameCorrect(namePlayerTwo).orElse(""));

        if (!errorMessage.isEmpty()) {
            req.setAttribute("errorMessage", errorMessage);
            req.getRequestDispatcher("/new-match.jsp").forward(req, resp);
        }

        UUID uuid = ongoingMatchesService.createMatch(namePlayerOne, namePlayerTwo);
        resp.sendRedirect(req.getContextPath() + "/match-score?uuid=%s".formatted(uuid));
    }

    private Optional<String> checkNameCorrect(String name) {
        if (name.isBlank()) {
            return Optional.of("Name cannot be blank!");
        } else if (name.length() < 2 || name.length() > 16) {
            return Optional.of("The name length cannot be less than 2 characters or more than 16!");
        } else if (Pattern.compile("\\d").matcher(name).find()) {
            return Optional.of("The name cannot contain numbers!");
        }

        return Optional.empty();
    }
}
