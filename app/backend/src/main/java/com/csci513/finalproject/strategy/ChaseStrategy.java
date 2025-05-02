package com.csci513.finalproject.strategy;

import com.csci513.finalproject.utils.Position;

// Concrete strategy for chasing a target.
public class ChaseStrategy implements MovementStrategy {

    private Position targetPosition;

    public ChaseStrategy(Position initialTarget) {
        this.targetPosition = initialTarget;
    }

    public void setTarget(Position targetPosition) {
        this.targetPosition = targetPosition;
        System.out.println("ChaseStrategy target updated to: " + targetPosition);
    }

    // Getter for the target position
    public Position getTarget() {
        return targetPosition;
    }

    @Override
    public Position move(Position currentPosition) {
        if (targetPosition == null) {
            System.out.println("ChaseStrategy: No target set, staying put.");
            return currentPosition; // Stay put if no target
        }

        int currentX = currentPosition.getX();
        int currentY = currentPosition.getY();
        int targetX = targetPosition.getX();
        int targetY = targetPosition.getY();

        int nextX = currentX;
        int nextY = currentY;

        int dx = targetX - currentX;
        int dy = targetY - currentY;

        // Prioritize moving horizontally if distance is greater or equal,
        // otherwise move vertically. Avoid diagonal moves.
        if (Math.abs(dx) >= Math.abs(dy)) {
            // Move horizontally if dx is not 0
            if (dx > 0) {
                nextX++;
            } else if (dx < 0) {
                nextX--;
            }
            // If dx is 0, try moving vertically
            else if (dy > 0) {
                nextY++;
            } else if (dy < 0) {
                nextY--;
            }
        } else {
             // Move vertically if dy is not 0
             if (dy > 0) {
                nextY++;
            } else if (dy < 0) {
                nextY--;
            }
             // If dy is 0, try moving horizontally
             else if (dx > 0) {
                 nextX++;
             } else if (dx < 0) {
                 nextX--;
             }
        }

        System.out.println("ChaseStrategy: Moving from [" + currentX + "," + currentY +
                           "] towards [" + targetX + "," + targetY + "] -> [" + nextX + "," + nextY + "]");

        // Return the calculated next position
        // The actual update of the character's position should happen in the character's move() method
        return new Position(nextX, nextY);
    }
} 