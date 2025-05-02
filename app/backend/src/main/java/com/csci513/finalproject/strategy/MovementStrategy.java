package com.csci513.finalproject.strategy;

import com.csci513.finalproject.utils.Position;

// Strategy interface for defining movement algorithms.
public interface MovementStrategy {
    // Takes the current position and returns the next position (or modifies the passed position)
    Position move(Position currentPosition);
} 