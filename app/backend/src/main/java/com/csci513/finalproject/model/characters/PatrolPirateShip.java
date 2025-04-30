package com.csci513.finalproject.model.characters;

import com.csci513.finalproject.strategy.MovementStrategy;

// Placeholder for Patrol Pirate Ship.
public class PatrolPirateShip extends PirateShip {
    public PatrolPirateShip(int x, int y, MovementStrategy strategy) {
        super(x, y, strategy);
    }

    // Inherits move() from PirateShip which uses the strategy
    // Add specific patrol behavior if needed
} 