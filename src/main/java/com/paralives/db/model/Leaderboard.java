package com.paralives.db.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "leaderboard")
public class Leaderboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "puzzle_number", nullable = false)
    private Integer puzzleNumber;

    @Column(name = "solve_time_ms", nullable = false)
    private Long solveTimeMs;

    @Column(name = "completed_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", insertable = false, updatable = false)
    private Player player;

    public Leaderboard() {
    }

    public Leaderboard(Long playerId, Integer puzzleNumber, Long solveTimeMs) {
        this.playerId = playerId;
        this.puzzleNumber = puzzleNumber;
        this.solveTimeMs = solveTimeMs;
    }

    public Long getId() {
        return id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Integer getPuzzleNumber() {
        return puzzleNumber;
    }

    public void setPuzzleNumber(Integer puzzleNumber) {
        this.puzzleNumber = puzzleNumber;
    }

    public Long getSolveTimeMs() {
        return solveTimeMs;
    }

    public void setSolveTimeMs(Long solveTimeMs) {
        this.solveTimeMs = solveTimeMs;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public Player getPlayer() {
        return player;
    }
}
