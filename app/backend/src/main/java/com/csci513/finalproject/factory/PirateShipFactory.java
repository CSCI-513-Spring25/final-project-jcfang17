package com.csci513.finalproject.factory;

import com.csci513.finalproject.model.characters.PirateShip;
import com.csci513.finalproject.utils.Position;

// Abstract Factory for creating PirateShip objects (Factory Method pattern).
public abstract class PirateShipFactory {

    // The abstract factory method.
    // Subclasses will implement this to create specific pirate ship types.
    public abstract PirateShip createPirateShip(String type, Position position);

    // Can include other common methods shared by all factories
    public void prepareShip(PirateShip ship) {
        System.out.println("Preparing ship: " + ship.getClass().getSimpleName() + " at " + ship.getPosition());
        // Add common preparation steps
    }

    // Example of a method that uses the factory method
    public PirateShip orderPirateShip(String type, Position position) {
        PirateShip pirateShip = createPirateShip(type, position);
        prepareShip(pirateShip);
        // You might register the observer here, or assign a default strategy etc.
        return pirateShip;
    }
} 