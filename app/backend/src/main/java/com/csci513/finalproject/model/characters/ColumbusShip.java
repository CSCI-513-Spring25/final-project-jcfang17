package com.csci513.finalproject.model.characters;

import com.csci513.finalproject.observer.Observable;
import com.csci513.finalproject.observer.Observer;
import com.csci513.finalproject.utils.Position;
import com.csci513.finalproject.model.map.OceanMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Represents Christopher Columbus's ship (the player).
// Acts as the Observable for Pirates (Observers).
public class ColumbusShip extends GameCharacter implements Observable {

    private Set<Observer> observers;
    // Map dimensions fetched from singleton
    private int mapWidth = OceanMap.getInstance().getWidth();
    private int mapHeight = OceanMap.getInstance().getHeight();

    public ColumbusShip(int x, int y) {
        super(x, y);
        this.observers = new HashSet<>(); // Initialize the observers set
    }

    @Override
    public void move() {
        System.out.println("ColumbusShip.move() called without direction - doing nothing.");
    }

    // New move method accepting direction
    public void move(String direction) {
        Position currentPos = getPosition();
        int currentX = currentPos.getX();
        int currentY = currentPos.getY();
        int nextX = currentX;
        int nextY = currentY;

        switch (direction.toUpperCase()) {
            case "UP":
                nextY--;
                break;
            case "DOWN":
                nextY++;
                break;
            case "LEFT":
                nextX--;
                break;
            case "RIGHT":
                nextX++;
                break;
            default:
                System.out.println("Unknown move direction: " + direction);
                return; // Don't move or notify if direction is invalid
        }

        // Wrap-around boundary logic
        nextX = (nextX + mapWidth) % mapWidth;   // Modulo for horizontal wrap
        nextY = (nextY + mapHeight) % mapHeight; // Modulo for vertical wrap

        // Check for moving onto islands
        if (OceanMap.getInstance().isIsland(nextX, nextY)) { // Use OceanMap method
             System.out.println("Columbus tried to move onto an island at [" + nextX + "," + nextY + "]");
             return; // Stay put
        }

        // Update position if it changed
        if (nextX != currentX || nextY != currentY) {
            setPosition(new Position(nextX, nextY));
            System.out.println("Columbus moved " + direction + " to [" + getPosition().getX() + "," + getPosition().getY() + "]");
            // Notify observers only if the position actually changed
            notifyObservers();
        } else {
             // This case might happen if the move direction was unknown or blocked by an island
             System.out.println("Columbus stayed at [" + currentX + "," + currentY + "]");
        }
    }

    // Observable methods
    @Override
    public void registerObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("Observer registered: " + observer.getClass().getSimpleName());
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
        System.out.println("Observer removed: " + observer.getClass().getSimpleName());
    }

    @Override
    public void notifyObservers() {
        System.out.println("Columbus notifying " + observers.size() + " observers...");
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    // Method to clear all registered observers
    public void clearObservers() {
        System.out.println("Clearing all observers for ColumbusShip.");
        this.observers.clear();
    }
} 