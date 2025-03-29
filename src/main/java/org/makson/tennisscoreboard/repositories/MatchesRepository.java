package org.makson.tennisscoreboard.repositories;

import jakarta.transaction.Transactional;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.criteria.JpaCriteriaQuery;
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

    public List<Matches> findAllPaginated(int offset, int limit) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        return session.createQuery("select m from Matches m", Matches.class).setFirstResult(offset).setMaxResults(limit).list();
    }

    public List<Matches> findAllPaginatedByFilter(int offset, int limit, String filterByName) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        return session.createQuery("""
                SELECT m
                FROM Matches m
                WHERE m.player1.name LIKE :name OR m.player2.name LIKE :name
                """, Matches.class).setParameter("name", "%" + filterByName + "%").list();
    }

    public Long getAmountRows() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        return session.createQuery("select count(*) from Matches", Long.class).getSingleResult();
    }

    public static MatchesRepository getInstance() {
        return INSTANCE;
    }
}
