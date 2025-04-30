package com.csci513.finalproject.factory;

import com.csci513.finalproject.model.characters.ChaserPirateShip;
import com.csci513.finalproject.model.characters.PatrolPirateShip;
import com.csci513.finalproject.model.characters.PirateShip;
import com.csci513.finalproject.strategy.ChaseStrategy;
import com.csci513.finalproject.strategy.PatrolStrategy;
import com.csci513.finalproject.strategy.PredictiveChaseStrategy;
import com.csci513.finalproject.utils.Position;

// Concrete factory implementing the factory method.
public class StandardPirateShipFactory extends PirateShipFactory {

    @Override
    public PirateShip createPirateShip(String type, Position position) {
        PirateShip pirateShip = null;

        // Determine which type of pirate ship to create
        if ("CHASER".equalsIgnoreCase(type)) {
            // Create a ChaserPirateShip, maybe with a default ChaseStrategy
            // Note: The target for ChaseStrategy likely needs to be set elsewhere (e.g., by GameManager or Observer update)
            pirateShip = new ChaserPirateShip(position.getX(), position.getY(), new ChaseStrategy(null)); // Target initially null
        } else if ("PREDICTIVE_CHASER".equalsIgnoreCase(type)) {
            // Create a ChaserPirateShip with PredictiveChaseStrategy
            pirateShip = new ChaserPirateShip(position.getX(), position.getY(), new PredictiveChaseStrategy(null)); // Target initially null
        } else if ("PATROL".equalsIgnoreCase(type)) {
            // Create a PatrolPirateShip, maybe with a default PatrolStrategy
            pirateShip = new PatrolPirateShip(position.getX(), position.getY(), new PatrolStrategy());
        }
        // Add more types if needed

        if (pirateShip == null) {
            throw new IllegalArgumentException("Unknown pirate ship type: " + type);
        }

        System.out.println("Created a " + type + " pirate ship.");
        return pirateShip;
    }
} 