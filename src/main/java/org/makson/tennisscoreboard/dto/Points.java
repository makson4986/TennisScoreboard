package org.makson.tennisscoreboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Points {
    ONE_POINT(15),
    TWO_POINT(30),
    THREE_POINT(40),
    LAST_POINT(1000);

    private final int points;
}
