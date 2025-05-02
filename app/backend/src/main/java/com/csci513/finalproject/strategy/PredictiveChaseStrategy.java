package com.csci513.finalproject.strategy;

import com.csci513.finalproject.utils.Position;

import java.util.Objects;

// Concrete strategy that chases a target, predicting the next move based on consecutive directions.
public class PredictiveChaseStrategy implements MovementStrategy {

    private Position targetPosition;
    private Position previousTargetPosition; // To calculate the last move direction
    private String lastDirection = null;
    private String secondLastDirection = null;
    private boolean consecutiveMove = false;

    public PredictiveChaseStrategy(Position initialTarget) {
        this.targetPosition = initialTarget;
        // Initial previous position can be the same, no direction calculated yet
        this.previousTargetPosition = initialTarget;
    }

    // Method to update the target's position and track movement history
    public void setTarget(Position newTarget) {
        if (newTarget == null) {
            // If target becomes null (e.g., Columbus disappears?), reset history?
            this.targetPosition = null;
            this.previousTargetPosition = null;
            this.lastDirection = null;
            this.secondLastDirection = null;
            this.consecutiveMove = false;
            return;
        }

        // Only update history if the target actually moved
        if (this.targetPosition != null && !this.targetPosition.equals(newTarget)) {
            // Calculate direction of the last move
            int dx = newTarget.getX() - this.targetPosition.getX();
            int dy = newTarget.getY() - this.targetPosition.getY();
            String currentDirection = calculateDirection(dx, dy);

            // Update history
            this.secondLastDirection = this.lastDirection;
            this.lastDirection = currentDirection;

            // Check for consecutive moves
            this.consecutiveMove = this.lastDirection != null && this.lastDirection.equals(this.secondLastDirection);

            if (this.consecutiveMove) {
                System.out.println("PredictiveChaseStrategy: Detected consecutive move: " + this.lastDirection);
            }
        } else if (this.targetPosition == null) {
             // First time setting the target, clear history just in case
             this.lastDirection = null;
             this.secondLastDirection = null;
             this.consecutiveMove = false;
        }


        // Update positions for the next calculation
        this.previousTargetPosition = this.targetPosition; // Old target becomes previous
        this.targetPosition = newTarget;             // Store the new target position
        System.out.println("PredictiveChaseStrategy target updated to: " + this.targetPosition + ", Last Dir: " + lastDirection + ", Prev Last Dir: " + secondLastDirection);
    }

    // Getter for the target position
    public Position getTarget() {
        return targetPosition;
    }

    // Helper to determine direction string from dx, dy
    private String calculateDirection(int dx, int dy) {
        if (Math.abs(dx) >= Math.abs(dy)) { // Prioritize horizontal
            if (dx > 0) return "RIGHT";
            if (dx < 0) return "LEFT";
        }
        // If dx is 0 or dy is larger magnitude
        if (dy > 0) return "DOWN";
        if (dy < 0) return "UP";
        return null; // No movement
    }

    @Override
    public Position move(Position currentPosition) {
        if (targetPosition == null) {
            System.out.println("PredictiveChaseStrategy: No target set, staying put.");
            return currentPosition;
        }

        int currentX = currentPosition.getX();
        int currentY = currentPosition.getY();
        int targetX = targetPosition.getX();
        int targetY = targetPosition.getY();

        int chaseDx = targetX - currentX;
        int chaseDy = targetY - currentY;

        // Calculate standard chase step (move 1 unit towards target, non-diagonal)
        int stdStepX = 0;
        int stdStepY = 0;
        if (Math.abs(chaseDx) >= Math.abs(chaseDy)) {
            if (chaseDx > 0) stdStepX = 1;
            else if (chaseDx < 0) stdStepX = -1;
            else if (chaseDy > 0) stdStepY = 1; // Move vertically if horizontally aligned
            else if (chaseDy < 0) stdStepY = -1;
        } else {
            if (chaseDy > 0) stdStepY = 1;
            else if (chaseDy < 0) stdStepY = -1;
            else if (chaseDx > 0) stdStepX = 1; // Move horizontally if vertically aligned
            else if (chaseDx < 0) stdStepX = -1;
        }

        // Initialize final step with standard chase step
        int finalStepX = stdStepX;
        int finalStepY = stdStepY;

        // Apply prediction if applicable
        if (consecutiveMove && lastDirection != null) {
            System.out.println("PredictiveChaseStrategy: Applying prediction for direction " + lastDirection);
            int predStepX = 0;
            int predStepY = 0;
            switch (lastDirection) {
                case "UP":    predStepY = -1; break;
                case "DOWN":  predStepY = 1;  break;
                case "LEFT":  predStepX = -1; break;
                case "RIGHT": predStepX = 1;  break;
            }

            // Combine standard chase step and predicted step
            // If standard step and predicted step are different axes, result is diagonal
            // If they are the same axis, the standard step takes precedence (already moving that way)
            // Example: std is LEFT (-1, 0), pred is UP (0, -1) -> final is (-1, -1)
            // Example: std is LEFT (-1, 0), pred is LEFT (-1, 0) -> final is (-1, 0)
            if (predStepX != 0 && stdStepX == 0) finalStepX = predStepX;
            if (predStepY != 0 && stdStepY == 0) finalStepY = predStepY;

             // Ensure we don't move more than 1 unit diagonally total (e.g., cap components)
             // This logic might need refinement based on desired speed/behavior
             finalStepX = Integer.signum(finalStepX); // Cap to -1, 0, 1
             finalStepY = Integer.signum(finalStepY); // Cap to -1, 0, 1
        }

        int nextX = currentX + finalStepX;
        int nextY = currentY + finalStepY;

        System.out.println("PredictiveChaseStrategy: Moving from [" + currentX + "," + currentY +
                           "] towards [" + targetX + "," + targetY + "] -> Step (" + finalStepX + "," + finalStepY + ") -> Next [" + nextX + "," + nextY + "]");

        return new Position(nextX, nextY);
    }
} 