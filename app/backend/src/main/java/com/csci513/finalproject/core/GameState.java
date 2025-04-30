package com.csci513.finalproject.core;

// Placeholder for overall game state.
public class GameState {
    // TODO: Add properties like game status (running, paused, win, lose),
    // player score, current turn, list of active characters, etc.

    private boolean isGameOver = false;
    private String statusMessage = "Game Started";

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
} 