package com.paralives.db.service;

import com.paralives.db.HibernateUtil;
import com.paralives.db.model.Leaderboard;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class LeaderboardService {

    // Submits a speedrun score for a puzzle
    public Leaderboard submitScore(Long playerId, int puzzleNumber, long solveTimeMs) {
        if (puzzleNumber < 1 || puzzleNumber > 6) {
            throw new IllegalArgumentException("Puzzle number must be between 1 and 6");
        }

        Leaderboard entry = new Leaderboard(playerId, puzzleNumber, solveTimeMs);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(entry);
            transaction.commit();
            return entry;
        } catch (RuntimeException exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    // Returns top 10 scores for a specific puzzle
    public List<Leaderboard> getTopTen(int puzzleNumber) {
        if (puzzleNumber < 1 || puzzleNumber > 6) {
            throw new IllegalArgumentException("Puzzle number must be between 1 and 6");
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Leaderboard> query = session.createQuery(
                    "FROM Leaderboard WHERE puzzleNumber = :puzzleNumber ORDER BY solveTimeMs ASC",
                    Leaderboard.class);
            query.setParameter("puzzleNumber", puzzleNumber);
            query.setMaxResults(10);
            return query.list();
        }
    }

    // Returns player's best time for a specific puzzle
    public Optional<Long> getPlayerBestTime(Long playerId, int puzzleNumber) {
        if (puzzleNumber < 1 || puzzleNumber > 6) {
            throw new IllegalArgumentException("Puzzle number must be between 1 and 6");
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT MIN(l.solveTimeMs) FROM Leaderboard l WHERE l.playerId = :playerId AND l.puzzleNumber = :puzzleNumber",
                    Long.class);
            query.setParameter("playerId", playerId);
            query.setParameter("puzzleNumber", puzzleNumber);
            return Optional.ofNullable(query.uniqueResult());
        }
    }

    // Returns all scores for a specific player
    public List<Leaderboard> getPlayerScores(Long playerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Leaderboard> query = session.createQuery(
                    "FROM Leaderboard WHERE playerId = :playerId ORDER BY completedAt DESC",
                    Leaderboard.class);
            query.setParameter("playerId", playerId);
            return query.list();
        }
    }
}
