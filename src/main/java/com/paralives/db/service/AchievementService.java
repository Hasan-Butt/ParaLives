package com.paralives.db.service;

import com.paralives.db.HibernateUtil;
import com.paralives.db.model.Achievement;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class AchievementService {

    // Unlocks an achievement for a player
    public Achievement unlock(Long playerId, String achievementCode) {
        if (isUnlocked(playerId, achievementCode)) {
            return null;
        }

        Achievement achievement = new Achievement(playerId, achievementCode);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(achievement);
            transaction.commit();
            return achievement;
        } catch (RuntimeException exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    // Checks if player has unlocked a specific achievement
    public boolean isUnlocked(Long playerId, String achievementCode) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(a) FROM Achievement a WHERE a.playerId = :playerId AND a.achievementCode = :code",
                    Long.class);
            query.setParameter("playerId", playerId);
            query.setParameter("code", achievementCode);
            return query.uniqueResult() > 0;
        }
    }

    // Returns all unlocked achievements for a player
    public List<Achievement> getUnlockedAchievements(Long playerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Achievement> query = session.createQuery(
                    "FROM Achievement WHERE playerId = :playerId ORDER BY unlockedAt DESC", Achievement.class);
            query.setParameter("playerId", playerId);
            return query.list();
        }
    }

    // Returns count of unlocked achievements for a player
    public long getUnlockedCount(Long playerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(a) FROM Achievement a WHERE a.playerId = :playerId", Long.class);
            query.setParameter("playerId", playerId);
            return query.uniqueResult();
        }
    }
}
