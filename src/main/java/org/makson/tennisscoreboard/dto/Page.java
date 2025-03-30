package org.makson.tennisscoreboard.dto;

public record Page(int offset, int limit, String filterByName) {
}
