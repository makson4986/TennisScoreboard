package org.makson.tennisscoreboard.services;

public class FinishedMatchesPersistenceService {
    private static final FinishedMatchesPersistenceService INSTANCE = new FinishedMatchesPersistenceService();

    private FinishedMatchesPersistenceService() {
    }

    public static FinishedMatchesPersistenceService getInstance() {
        return INSTANCE;
    }
}
