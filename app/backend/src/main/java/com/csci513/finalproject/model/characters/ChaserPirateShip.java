package com.csci513.finalproject.model.characters;

import com.csci513.finalproject.strategy.MovementStrategy;

// Placeholder for Chaser Pirate Ship.
public class ChaserPirateShip extends PirateShip {
    public ChaserPirateShip(int x, int y, MovementStrategy strategy) {
        super(x, y, strategy);
    }

    // Inherits move() from PirateShip which uses the strategy
    // Add specific chaser behavior if needed
} 