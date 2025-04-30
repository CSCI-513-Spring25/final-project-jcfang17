package com.csci513.finalproject.api;

import com.csci513.finalproject.core.GameManager;
import com.csci513.finalproject.core.GameState;

public class GameController {

    private GameManager gameManager; // Needs to be instantiated or injected

    public GameController(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    // Example endpoint to get the current game state
    // @GetMapping("/state") // Example Spring annotation
    public GameState getGameState() {
        System.out.println("API: Received request for game state.");
        // In a real setup, this would return a serialized GameState object
        // For now, just return the reference (won't work over HTTP)
        return gameManager.getGameState();
    }

    // Example endpoint to handle player action (e.g., move)
    // @PostMapping("/action/move") // Example Spring annotation
    public void handleMoveAction(/* @RequestBody MoveRequest moveRequest */) {
        System.out.println("API: Received request for move action.");
        // Previous TODOs removed as WebServer handles this directly
    }

    // TODO: Add endpoints for starting game, getting map data, etc.
}

// Example request body class (if needed)
// class MoveRequest {
//     String direction; // e.g., "UP", "DOWN", "LEFT", "RIGHT"
// } 