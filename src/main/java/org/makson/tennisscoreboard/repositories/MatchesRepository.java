package org.makson.tennisscoreboard.repositories;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.makson.tennisscoreboard.exceptions.DataBaseException;
import org.makson.tennisscoreboard.models.Matches;
import org.makson.tennisscoreboard.utils.HibernateUtil;

import java.util.List;

public class MatchesRepository {
    private static final MatchesRepository INSTANCE = new MatchesRepository();

    private MatchesRepository() {
    }

    public void save(Matches match) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(match);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new DataBaseException();
        }
    }

    public List<Matches> findAllPaginated(int offset, int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select m from Matches m", Matches.class).setFirstResult(offset).setMaxResults(limit).list();
        } catch (HibernateException e) {
            throw new DataBaseException();
        }
    }

    public List<Matches> findAllPaginatedByFilter(int offset, int limit, String filterByName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                            SELECT m
                            FROM Matches m
                            WHERE m.player1.name LIKE :name OR m.player2.name LIKE :name
                            """, Matches.class).setParameter("name", "%" + filterByName + "%")
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .list();
        } catch (HibernateException e) {
            throw new DataBaseException();
        }
    }

    public Long getAmountRows() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select count(*) from Matches", Long.class).getSingleResult();
        } catch (HibernateException e) {
            throw new DataBaseException();
        }
    }

    public static MatchesRepository getInstance() {
        return INSTANCE;
    }
}
