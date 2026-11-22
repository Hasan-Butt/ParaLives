package com.paralives.db.service;

import com.paralives.db.HibernateUtil;
import com.paralives.db.model.Admin;
import com.paralives.db.model.Leaderboard;
import com.paralives.db.model.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AdminService {

    // Authenticates an admin user
    public Optional<Admin> loginAdmin(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Admin> query = session.createQuery(
                    "FROM Admin WHERE username = :username", Admin.class);
            query.setParameter("username", username);
            Admin admin = query.uniqueResult();

            if (admin != null && BCrypt.checkpw(password, admin.getPasswordHash())) {
                return Optional.of(admin);
            }
            return Optional.empty();
        }
    }

    // Blocks a player account
    public void blockPlayer(Long playerId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Player player = session.find(Player.class, playerId);
            if (player != null) {
                player.setBlocked(true);
                session.merge(player);
            }
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    // Unblocks a player account
    public void unblockPlayer(Long playerId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Player player = session.find(Player.class, playerId);
            if (player != null) {
                player.setBlocked(false);
                session.merge(player);
            }
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    // Deletes a player account and all related data
    public void deletePlayer(Long playerId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Player player = session.find(Player.class, playerId);
            if (player != null) {
                session.remove(player);
            }
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    // Deletes a specific leaderboard entry
    public void deleteLeaderboardEntry(Long entryId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Leaderboard entry = session.find(Leaderboard.class, entryId);
            if (entry != null) {
                session.remove(entry);
            }
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    // Returns system statistics
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> totalPlayersQuery = session.createQuery(
                    "SELECT COUNT(p) FROM Player p WHERE p.isGuest = false", Long.class);
            stats.put("totalPlayers", totalPlayersQuery.uniqueResult());

            Query<Long> activePlayersQuery = session.createQuery(
                    "SELECT COUNT(p) FROM Player p WHERE p.blocked = false AND p.isGuest = false", Long.class);
            stats.put("activePlayers", activePlayersQuery.uniqueResult());

            Query<Long> blockedPlayersQuery = session.createQuery(
                    "SELECT COUNT(p) FROM Player p WHERE p.blocked = true", Long.class);
            stats.put("blockedPlayers", blockedPlayersQuery.uniqueResult());

            Query<Long> totalScoresQuery = session.createQuery(
                    "SELECT COUNT(l) FROM Leaderboard l", Long.class);
            stats.put("totalScores", totalScoresQuery.uniqueResult());

            Query<Double> avgSolveTimeQuery = session.createQuery(
                    "SELECT AVG(l.solveTimeMs) FROM Leaderboard l", Double.class);
            stats.put("avgSolveTimeMs", avgSolveTimeQuery.uniqueResult());

            for (int puzzle = 1; puzzle <= 6; puzzle++) {
                Query<Long> puzzleAttemptsQuery = session.createQuery(
                        "SELECT COUNT(l) FROM Leaderboard l WHERE l.puzzleNumber = :puzzleNumber", Long.class);
                puzzleAttemptsQuery.setParameter("puzzleNumber", puzzle);
                stats.put("puzzle" + puzzle + "Attempts", puzzleAttemptsQuery.uniqueResult());
            }

            return stats;
        }
    }

    // Creates a new admin account
    public Admin createAdmin(String username, String password) {
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        Admin admin = new Admin(username, passwordHash);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(admin);
            transaction.commit();
            return admin;
        } catch (RuntimeException exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }
}
