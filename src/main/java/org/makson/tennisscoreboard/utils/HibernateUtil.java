package org.makson.tennisscoreboard.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = createSessionFactory();
    }

    private static SessionFactory createSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure();
        return configuration.buildSessionFactory();
    }
}

