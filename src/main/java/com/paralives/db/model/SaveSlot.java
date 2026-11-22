package com.paralives.db.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "save_slots")
public class SaveSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "slot_number", nullable = false)
    private Integer slotNumber;

    @Column(name = "puzzle_index", nullable = false)
    private Integer puzzleIndex = 0;

    @Column(name = "game_state_json", columnDefinition = "TEXT")
    private String gameStateJson;

    @Column(name = "save_timestamp", nullable = false, insertable = false, updatable = false)
    private LocalDateTime saveTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", insertable = false, updatable = false)
    private Player player;

    public SaveSlot() {
    }

    public SaveSlot(Long playerId, Integer slotNumber, Integer puzzleIndex, String gameStateJson) {
        this.playerId = playerId;
        this.slotNumber = slotNumber;
        this.puzzleIndex = puzzleIndex;
        this.gameStateJson = gameStateJson;
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

    public Integer getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(Integer slotNumber) {
        this.slotNumber = slotNumber;
    }

    public Integer getPuzzleIndex() {
        return puzzleIndex;
    }

    public void setPuzzleIndex(Integer puzzleIndex) {
        this.puzzleIndex = puzzleIndex;
    }

    public String getGameStateJson() {
        return gameStateJson;
    }

    public void setGameStateJson(String gameStateJson) {
        this.gameStateJson = gameStateJson;
    }

    public LocalDateTime getSaveTimestamp() {
        return saveTimestamp;
    }

    public Player getPlayer() {
        return player;
    }
}
