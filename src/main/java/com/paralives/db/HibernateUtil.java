package com.paralives.db;

import com.paralives.db.model.Player;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(Player.class);
        configuration.addAnnotatedClass(com.paralives.db.model.SaveSlot.class);
        configuration.addAnnotatedClass(com.paralives.db.model.Achievement.class);
        configuration.addAnnotatedClass(com.paralives.db.model.Leaderboard.class);
        configuration.addAnnotatedClass(com.paralives.db.model.Admin.class);

        return configuration.buildSessionFactory(
                new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build());
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        SESSION_FACTORY.close();
    }
}
