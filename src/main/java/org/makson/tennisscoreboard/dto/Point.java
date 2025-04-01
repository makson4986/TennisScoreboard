package org.makson.tennisscoreboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Point {
    ZERO("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    WIN_POINT("WP"),
    ADVANTAGE("AD");

    private final String point;
}
