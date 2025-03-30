package org.makson.tennisscoreboard.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class BaseController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            super.service(req, resp);
        } catch (Exception exception) {
            String errorMessage = exception.getMessage();
            req.setAttribute("errorMessage", errorMessage);

            req.getRequestDispatcher( req.getContextPath() + "error-pages.jsp").forward(req, resp);
        }
    }
}
