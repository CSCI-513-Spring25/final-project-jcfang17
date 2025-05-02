package com.csci513.finalproject.strategy;

import com.csci513.finalproject.utils.Position;
import java.util.Random;

// Concrete strategy for patrolling (e.g., random movement).
public class PatrolStrategy implements MovementStrategy {

    private Random random = new Random();

    @Override
    public Position move(Position currentPosition) {
        int currentX = currentPosition.getX();
        int currentY = currentPosition.getY();

        // Simple random move: +/- 1 in x or y direction, or stay put
        int moveType = random.nextInt(5); // 0: stay, 1: +x, 2: -x, 3: +y, 4: -y

        int nextX = currentX;
        int nextY = currentY;

        switch (moveType) {
            case 1: nextX++; break;
            case 2: nextX--; break;
            case 3: nextY++; break;
            case 4: nextY--; break;
            default: // case 0: stay put
                break;
        }


        System.out.println("PatrolStrategy: Moving randomly from [" + currentX + "," + currentY +
                           "] -> [" + nextX + "," + nextY + "]");

        return new Position(nextX, nextY);
    }
}
