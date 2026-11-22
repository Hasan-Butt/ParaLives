package com.paralives.db.service;

import com.paralives.db.HibernateUtil;
import com.paralives.db.model.SaveSlot;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class SaveService {

    // Saves game state to a specific slot (creates or updates)
    public SaveSlot save(Long playerId, int slotNumber, String gameStateJson, int puzzleIndex) {
        if (slotNumber < 1 || slotNumber > 3) {
            throw new IllegalArgumentException("Slot number must be between 1 and 3");
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query<SaveSlot> query = session.createQuery(
                    "FROM SaveSlot WHERE playerId = :playerId AND slotNumber = :slotNumber", SaveSlot.class);
            query.setParameter("playerId", playerId);
            query.setParameter("slotNumber", slotNumber);
            SaveSlot existingSlot = query.uniqueResult();

            if (existingSlot != null) {
                existingSlot.setGameStateJson(gameStateJson);
                existingSlot.setPuzzleIndex(puzzleIndex);
                session.merge(existingSlot);
                transaction.commit();
                return existingSlot;
            } else {
                SaveSlot newSlot = new SaveSlot(playerId, slotNumber, puzzleIndex, gameStateJson);
                session.persist(newSlot);
                transaction.commit();
                return newSlot;
            }
        } catch (RuntimeException exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    // Loads game state from a specific slot
    public Optional<SaveSlot> load(Long playerId, int slotNumber) {
        if (slotNumber < 1 || slotNumber > 3) {
            throw new IllegalArgumentException("Slot number must be between 1 and 3");
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<SaveSlot> query = session.createQuery(
                    "FROM SaveSlot WHERE playerId = :playerId AND slotNumber = :slotNumber", SaveSlot.class);
            query.setParameter("playerId", playerId);
            query.setParameter("slotNumber", slotNumber);
            return Optional.ofNullable(query.uniqueResult());
        }
    }

    // Lists all save slots for a player (max 3)
    public List<SaveSlot> listSlots(Long playerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<SaveSlot> query = session.createQuery(
                    "FROM SaveSlot WHERE playerId = :playerId ORDER BY slotNumber", SaveSlot.class);
            query.setParameter("playerId", playerId);
            return query.list();
        }
    }

    // Deletes a specific save slot
    public void deleteSlot(Long playerId, int slotNumber) {
        if (slotNumber < 1 || slotNumber > 3) {
            throw new IllegalArgumentException("Slot number must be between 1 and 3");
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<SaveSlot> query = session.createQuery(
                    "FROM SaveSlot WHERE playerId = :playerId AND slotNumber = :slotNumber", SaveSlot.class);
            query.setParameter("playerId", playerId);
            query.setParameter("slotNumber", slotNumber);
            SaveSlot slot = query.uniqueResult();

            if (slot != null) {
                session.remove(slot);
            }
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }
}
