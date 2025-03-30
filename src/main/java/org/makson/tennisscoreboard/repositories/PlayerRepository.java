package org.makson.tennisscoreboard.repositories;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.makson.tennisscoreboard.exceptions.DataBaseException;
import org.makson.tennisscoreboard.models.Player;
import org.makson.tennisscoreboard.utils.HibernateUtil;

import java.util.Optional;

public class PlayerRepository {
    private static final PlayerRepository INSTANCE = new PlayerRepository();

    private PlayerRepository() {
    }

    public Player save(Player player) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(player);
            session.getTransaction().commit();
            return player;
        } catch (HibernateException e) {
            throw new DataBaseException();
        }
    }

    public Optional<Player> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select m from Player m where m.name=:name", Player.class)
                    .setParameter("name", name).uniqueResultOptional();
        } catch (HibernateException e) {
            throw new DataBaseException();
        }
    }

    public static PlayerRepository getInstance() {
        return INSTANCE;
    }
}
