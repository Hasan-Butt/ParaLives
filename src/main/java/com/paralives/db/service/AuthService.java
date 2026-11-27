package com.paralives.db.service;

import com.paralives.db.HibernateUtil;
import com.paralives.db.model.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthService {

    // Registers a new player with hashed password
    public Player register(String name, String email, String password) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        Player player = new Player(name, email, passwordHash);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(player);
            transaction.commit();
            return player;
        } catch (RuntimeException exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    // Validates credentials and returns player if successful
    public Optional<Player> login(String email, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Player> query = session.createQuery(
                    "FROM Player WHERE email = :email AND blocked = false", Player.class);
            query.setParameter("email", email);
            Player player = query.uniqueResult();

            if (player != null && player.getPasswordHash() != null &&
                    BCrypt.checkpw(password, player.getPasswordHash())) {
                SessionContext.getInstance().setCurrentPlayer(player);
                return Optional.of(player);
            }
            return Optional.empty();
        }
    }

    // Creates a guest player without password
    public Player createGuest(String name) {
        Player guest = new Player(name, "guest_" + System.currentTimeMillis() + "@paralives.local");
        guest.setIsGuest(true);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(guest);
            transaction.commit();
            SessionContext.getInstance().setCurrentPlayer(guest);
            return guest;
        } catch (RuntimeException exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    // Clears the current session
    public void logout() {
        SessionContext.getInstance().clearSession();
    }

    // Validates email format
    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    // Validates password strength
    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }

    // Checks if email is already registered
    public boolean emailExists(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(p) FROM Player p WHERE p.email = :email", Long.class);
            query.setParameter("email", email);
            return query.uniqueResult() > 0;
        }
    }
}
