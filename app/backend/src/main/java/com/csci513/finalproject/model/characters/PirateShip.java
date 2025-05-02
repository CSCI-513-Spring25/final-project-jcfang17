package com.csci513.finalproject.model.characters;

import com.csci513.finalproject.observer.Observable;
import com.csci513.finalproject.observer.Observer;
import com.csci513.finalproject.strategy.ChaseStrategy;
import com.csci513.finalproject.strategy.MovementStrategy;
import com.csci513.finalproject.strategy.PatrolStrategy;
import com.csci513.finalproject.strategy.PredictiveChaseStrategy;
import com.csci513.finalproject.utils.Position;
import com.csci513.finalproject.model.map.OceanMap;
import com.csci513.finalproject.model.map.MapCell;

import java.util.Random;

// Abstract base class for different Pirate Ship types.
// Implements Observer to track Columbus.
public abstract class PirateShip extends GameCharacter implements Observer {

    private MovementStrategy movementStrategy;
    // Map dimensions fetched from singleton
    private int mapWidth = OceanMap.getInstance().getWidth();
    private int mapHeight = OceanMap.getInstance().getHeight();

    public PirateShip(int x, int y, MovementStrategy strategy) {
        super(x, y);
        this.movementStrategy = strategy;
    }

    // Use the assigned strategy to move
    @Override
    public void move() {
        if (movementStrategy != null) {
            Position currentPosition = getPosition();
            Position nextPositionAttempt = movementStrategy.move(currentPosition); // Get intended move from strategy

            // Apply wrap-around boundary logic
            int nextX = (nextPositionAttempt.getX() + mapWidth) % mapWidth;
            int nextY = (nextPositionAttempt.getY() + mapHeight) % mapHeight;
            Position nextPosition = new Position(nextX, nextY);

            // Check for moving onto islands
            if (OceanMap.getInstance().isIsland(nextX, nextY)) {
                 System.out.println(getClass().getSimpleName() + " tried to move onto an island at [" + nextX + "," + nextY + "]");
                 nextPosition = currentPosition; // Stay put if moving onto island
            } else {
                // Check if the destination cell is a strategy switcher
                MapCell destinationCell = OceanMap.getInstance().getCell(nextX, nextY);
                if (destinationCell != null && destinationCell.isStrategySwitcher()) {
                    System.out.println(getClass().getSimpleName() + " landed on a strategy switcher cell at [" + nextX + "," + nextY + "]!");
                    switchStrategy();
                }
            }

            // Set position if it actually changed
            if (!currentPosition.equals(nextPosition)) {
                setPosition(nextPosition);
                System.out.println(getClass().getSimpleName() + " moved to [" + getPosition().getX() + "," + getPosition().getY() + "] using " + movementStrategy.getClass().getSimpleName());
            } else {
                System.out.println(getClass().getSimpleName() + " stayed at [" + getPosition().getX() + "," + getPosition().getY() + "] (Island or no move from strategy)");
            }
        } else {
            System.out.println(getClass().getSimpleName() + " has no strategy!");
        }
    }

    // Helper method to switch between chase strategies
    private void switchStrategy() {
        if (this.movementStrategy instanceof ChaseStrategy && !(this.movementStrategy instanceof PredictiveChaseStrategy)) {
            // Switch from Standard Chase to Predictive Chase
            Position currentTarget = ((ChaseStrategy) this.movementStrategy).getTarget();
            this.movementStrategy = new PredictiveChaseStrategy(currentTarget);
            System.out.println(getClass().getSimpleName() + " switched to PredictiveChaseStrategy.");
        } else if (this.movementStrategy instanceof PredictiveChaseStrategy) {
            // Switch from Predictive Chase to Standard Chase
            Position currentTarget = ((PredictiveChaseStrategy) this.movementStrategy).getTarget();
            this.movementStrategy = new ChaseStrategy(currentTarget);
            System.out.println(getClass().getSimpleName() + " switched to ChaseStrategy.");
        } else {
            // If it's Patrol or some other strategy, do nothing
            System.out.println(getClass().getSimpleName() + " landed on switcher, but current strategy (" + this.movementStrategy.getClass().getSimpleName() + ") is not switchable.");
        }
    }

    public void setMovementStrategy(MovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    // Getter for testing purposes
    public MovementStrategy getMovementStrategy() {
        return this.movementStrategy;
    }

    // Observer method
    @Override
    public void update(Observable observable) {
        if (observable instanceof ColumbusShip) {
            ColumbusShip columbus = (ColumbusShip) observable;
            System.out.println(getClass().getSimpleName() + " at [" + getPosition().getX() + "," + getPosition().getY() +
                               "] observes Columbus at [" + columbus.getPosition().getX() + "," +
                               columbus.getPosition().getY() + "]");

             // Update the target for chase-type strategies
             if (this.movementStrategy instanceof ChaseStrategy) {
                  // Standard chase strategy
                  ((ChaseStrategy) this.movementStrategy).setTarget(columbus.getPosition());
             } else if (this.movementStrategy instanceof PredictiveChaseStrategy) {
                  // Predictive chase strategy
                  ((PredictiveChaseStrategy) this.movementStrategy).setTarget(columbus.getPosition());
             }
            
        }
    }
} 