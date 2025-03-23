package org.makson.tennisscoreboard.repositories;

import jakarta.transaction.Transactional;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.makson.tennisscoreboard.models.Matches;
import org.makson.tennisscoreboard.utils.HibernateUtil;

import java.util.List;

public class MatchesRepository {
    @Transactional
    public void save(Matches match) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.persist(match);
    }

    @Transactional
    public List<Matches> findAll() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        return session.createQuery("select m from Matches m", Matches.class).getResultList();
    }
}
