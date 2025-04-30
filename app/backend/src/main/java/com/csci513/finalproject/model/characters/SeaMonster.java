package com.csci513.finalproject.model.characters;

import com.csci513.finalproject.composite.OceanFeature;
import com.csci513.finalproject.strategy.MovementStrategy;
import com.csci513.finalproject.strategy.PatrolStrategy; // Use PatrolStrategy for basic movement
import com.csci513.finalproject.utils.Position;
import com.csci513.finalproject.model.map.OceanMap;

// Represents a Sea Monster.
// Implements OceanFeature to be part of the Composite pattern.
public class SeaMonster extends GameCharacter implements OceanFeature {

    private boolean active = true; // Part of OceanFeature
    private MovementStrategy movementStrategy;
    // Map dimensions fetched from singleton
    private int mapWidth = OceanMap.getInstance().getWidth();
    private int mapHeight = OceanMap.getInstance().getHeight();

    public SeaMonster(int x, int y) {
        super(x, y);
        // Give monsters a default patrol strategy
        this.movementStrategy = new PatrolStrategy();
    }

    @Override
    public void move() {
        if (!active) {
            System.out.println("SeaMonster is inactive, not moving.");
            return;
        }

        if (movementStrategy != null) {
            Position currentPosition = getPosition();
            Position nextPositionAttempt = movementStrategy.move(currentPosition);

            // Apply wrap-around boundary logic
            int nextX = (nextPositionAttempt.getX() + mapWidth) % mapWidth;
            int nextY = (nextPositionAttempt.getY() + mapHeight) % mapHeight;
            Position nextPosition = new Position(nextX, nextY);

            // Check for moving onto islands
            if (OceanMap.getInstance().isIsland(nextX, nextY)) {
                System.out.println("SeaMonster tried to move onto an island at [" + nextX + "," + nextY + "]");
                nextPosition = currentPosition; // Stay put if moving onto island
            }

            if (!currentPosition.equals(nextPosition)) {
                setPosition(nextPosition);
                 System.out.println("SeaMonster moved to [" + getPosition().getX() + "," + getPosition().getY() + "] using " + movementStrategy.getClass().getSimpleName());
            } else {
                 System.out.println("SeaMonster stayed at [" + getPosition().getX() + "," + getPosition().getY() + "] (Island or no move from strategy)");
            }
        } else {
             System.out.println("SeaMonster has no strategy!");
        }
    }

    // OceanFeature methods
    @Override
    public void activate() {
        this.active = true;
        System.out.println("SeaMonster at [" + getPosition().getX() + "," + getPosition().getY() + "] activated.");
    }

    @Override
    public void deactivate() {
        this.active = false;
        System.out.println("SeaMonster at [" + getPosition().getX() + "," + getPosition().getY() + "] deactivated.");
    }

    public boolean isActive() {
        return active;
    }
} 