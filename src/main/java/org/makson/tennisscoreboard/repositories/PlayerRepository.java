package org.makson.tennisscoreboard.repositories;

import jakarta.transaction.Transactional;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.makson.tennisscoreboard.models.Player;
import org.makson.tennisscoreboard.utils.HibernateUtil;

import java.util.Optional;

public class PlayerRepository {
    private static final PlayerRepository INSTANCE = new PlayerRepository();

    private PlayerRepository() {
    }

    public Player save(Player player) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(player);
        session.getTransaction().commit();
        return player;
    }

    @Transactional
    public Optional<Player> findByName(String name) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        return session.createQuery("select m from Player m where m.name=:name", Player.class)
                .setParameter("name", name).uniqueResultOptional();
    }

    public static PlayerRepository getInstance() {
        return INSTANCE;
    }
}
