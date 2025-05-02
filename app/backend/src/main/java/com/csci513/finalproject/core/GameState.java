package com.csci513.finalproject.core;


public class GameState {

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