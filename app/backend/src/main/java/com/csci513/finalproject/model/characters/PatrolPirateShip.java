package com.csci513.finalproject.model.characters;

import com.csci513.finalproject.strategy.MovementStrategy;


public class PatrolPirateShip extends PirateShip {
    public PatrolPirateShip(int x, int y, MovementStrategy strategy) {
        super(x, y, strategy);
    }

} 