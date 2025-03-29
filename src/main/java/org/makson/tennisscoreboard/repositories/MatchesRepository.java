package org.makson.tennisscoreboard.repositories;

import jakarta.transaction.Transactional;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.makson.tennisscoreboard.models.Matches;
import org.makson.tennisscoreboard.utils.HibernateUtil;

import java.util.List;

public class MatchesRepository {
    private static final MatchesRepository INSTANCE = new MatchesRepository();

    private MatchesRepository() {
    }

    public void save(Matches match) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(match);
        session.getTransaction().commit();
    }

    public List<Matches> findAll() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        return session.createQuery("select m from Matches m", Matches.class).getResultList();
    }

    public static MatchesRepository getInstance() {
        return INSTANCE;
    }
}
