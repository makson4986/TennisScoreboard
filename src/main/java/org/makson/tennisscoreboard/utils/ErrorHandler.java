package org.makson.tennisscoreboard.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorHandler {
    public static void handleError(HttpServletRequest req, Throwable exception) {
        String errorMessage = exception.getMessage();

        req.setAttribute("errorMessage", errorMessage);
    }
}
