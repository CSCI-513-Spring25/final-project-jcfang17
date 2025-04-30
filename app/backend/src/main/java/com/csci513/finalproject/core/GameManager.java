package com.csci513.finalproject.core;

import com.csci513.finalproject.composite.FeatureGroup;
import com.csci513.finalproject.factory.PirateShipFactory;
import com.csci513.finalproject.factory.StandardPirateShipFactory;
import com.csci513.finalproject.model.characters.ColumbusShip;
import com.csci513.finalproject.model.characters.PirateShip;
import com.csci513.finalproject.model.characters.SeaMonster;
import com.csci513.finalproject.model.map.OceanMap;
import com.csci513.finalproject.utils.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

// Manages the game state and updates.
public class GameManager {

    private OceanMap oceanMap;
    private GameState gameState;
    private ColumbusShip columbusShip;
    private List<PirateShip> pirateShips;
    private List<SeaMonster> seaMonsters;
    private FeatureGroup monsterZone; // Optional: For Composite Pattern demo
    private Random random = new Random();
    private final int mapWidth = 20; // Define map size constants
    private final int mapHeight = 20;

    public GameManager() {
        // Initialize map (using Singleton)
        // Make map slightly larger as per requirements
        this.oceanMap = OceanMap.getInstance(mapWidth, mapHeight); // Use constants
        initializeGame(); // Call the new initialization method
    }

    // Method to initialize or reset the game state
    private synchronized void initializeGame() {
        System.out.println("Initializing/Resetting game...");
        // Re-fetch or ensure map instance exists
        // If map structure could change (e.g., islands placed randomly), might need more complex reset
        this.oceanMap = OceanMap.getInstance(mapWidth, mapHeight);
        // Reset game state flags and message
        this.gameState = new GameState(); // Create a new GameState object
        // Clear existing character lists
        this.pirateShips = new ArrayList<>();
        this.seaMonsters = new ArrayList<>();
        this.monsterZone = new FeatureGroup("Shallow Monsters"); // Recreate Composite group
        this.columbusShip = null; // Ensure old subject is cleared if necessary

        // Initialize characters and observers
        initializeCharacters();
        setupObservers();

        gameState.setStatusMessage("Game ready. Use controls to move Columbus.");
        System.out.println("Game initialization complete.");
    }


    private void initializeCharacters() {
        Set<Position> occupiedPositions = new HashSet<>();

        // Place Columbus
        // Ensure player starts at a valid position (not on potential future islands)
        Position columbusStart = new Position(0, 0);
        // TODO: Verify (0,0) is always ocean after map generation
        this.columbusShip = new ColumbusShip(columbusStart.getX(), columbusStart.getY());
        occupiedPositions.add(columbusStart);

        // Place Pirates using Factory
        PirateShipFactory factory = new StandardPirateShipFactory();
        addPirate("PATROL", factory, occupiedPositions);
        addPirate("PREDICTIVE_CHASER", factory, occupiedPositions);
        // TODO: Add more pirates/monsters, potentially from a config

        // Place Sea Monsters (Increased number)
        addSeaMonster(occupiedPositions);
        addSeaMonster(occupiedPositions);
        addSeaMonster(occupiedPositions);
        addSeaMonster(occupiedPositions); // Now 4 monsters

        // Add monsters to the composite group
        for(SeaMonster monster : this.seaMonsters) {
            this.monsterZone.addFeature(monster);
        }
        // this.monsterZone.activate(); // Example: Activate group (monsters start active by default now)
    }

    // Helper to add a pirate at a unique random position
    private void addPirate(String type, PirateShipFactory factory, Set<Position> occupied) {
        Position pos;
        do {
            pos = getRandomValidPosition(occupied);
        } while (occupied.contains(pos)); // Loop should be redundant if getRandomValidPosition works
        occupied.add(pos);
        PirateShip newPirate = factory.orderPirateShip(type, pos);
        this.pirateShips.add(newPirate);
        // Re-register observer if observers are cleared during reset
        if (this.columbusShip != null) {
             this.columbusShip.registerObserver(newPirate);
        }
        System.out.println("Placed " + type + " pirate at " + pos);
    }

    // Helper to add a sea monster at a unique random position
    private void addSeaMonster(Set<Position> occupied) {
         Position pos;
         do {
             pos = getRandomValidPosition(occupied);
         } while (occupied.contains(pos)); // Loop should be redundant if getRandomValidPosition works
         occupied.add(pos);
         SeaMonster monster = new SeaMonster(pos.getX(), pos.getY());
         this.seaMonsters.add(monster);
         System.out.println("Placed SeaMonster at " + pos);
    }

    // Helper to get a random valid position (not occupied, not island)
    private Position getRandomValidPosition(Set<Position> occupied) {
        Position pos;
        boolean positionIsValid;
        do {
             int x = random.nextInt(oceanMap.getWidth());
             int y = random.nextInt(oceanMap.getHeight());
             pos = new Position(x, y);
             // Check if occupied or if it's an island
             boolean isIsland = oceanMap.isIsland(x, y); // Use the OceanMap method
             positionIsValid = !occupied.contains(pos) && !isIsland;
        } while (!positionIsValid);
        return pos;
    }

    // Remove old getRandomPosition() - replaced by getRandomValidPosition
    /*
    private Position getRandomPosition() {
        int x = random.nextInt(oceanMap.getWidth());
        int y = random.nextInt(oceanMap.getHeight());
        return new Position(x, y);
    }
    */

    private void setupObservers() {
        // Ensure Columbus exists before registering observers
        if (this.columbusShip == null) {
            System.err.println("Attempted to setup observers before ColumbusShip was initialized!");
            return;
        }
        // Clear existing observers from Columbus first, in case of reset
        this.columbusShip.clearObservers(); // Need to add clearObservers() method to ColumbusShip

        // Register pirates to observe Columbus
        System.out.println("Setting up observers...");
        for (PirateShip pirate : pirateShips) {
            this.columbusShip.registerObserver(pirate);
        }
        System.out.println("Observers set up for " + pirateShips.size() + " pirates.");
    }

    // Method called by the API controller when a move request comes in
    public synchronized void processPlayerMove(String direction) {
        if (gameState.isGameOver()) {
            System.out.println("GameManager: Game is over, ignoring move.");
            return;
        }

        System.out.println("GameManager: Processing player move: " + direction);
        // 1. Update Columbus position based on direction
        columbusShip.move(direction); // Modify ColumbusShip.move to accept direction

        // 2. Move pirates (and monsters later)
        moveNPCs();

        // 3. Check win/lose conditions
        updateGameStatus();

        // Columbus notifies observers AFTER moving (and potentially being caught)
        // Note: ColumbusShip already calls notifyObservers in its move method - verify this is desired timing
        // If move was invalid (e.g., out of bounds), notification might not be needed or desired here
        // Notification is currently handled within ColumbusShip.move()
    }

    // Helper to move all non-player characters
    private void moveNPCs() {
        for (PirateShip pirate : pirateShips) {
            pirate.move(); // Pirates move based on their strategy
        }
        // Move monsters
        for (SeaMonster monster : seaMonsters) {
             monster.move();
        }
    }


    private void updateGameStatus() {
         // Check if Columbus is caught by any pirate
        for (PirateShip pirate : pirateShips) {
            if (columbusShip.getPosition().equals(pirate.getPosition())) {
                gameState.setGameOver(true);
                gameState.setStatusMessage("Caught by a pirate! Game Over.");
                System.out.println("GameManager: Columbus caught by pirate at " + columbusShip.getPosition());
                return; // Game ends
            }
        }

        // Check if Columbus is caught by any sea monster
        for (SeaMonster monster : seaMonsters) {
            // Also check if monster is active (relevant if using Composite activate/deactivate)
            if (monster.isActive() && columbusShip.getPosition().equals(monster.getPosition())) {
                 gameState.setGameOver(true);
                 gameState.setStatusMessage("Caught by a Sea Monster! Game Over.");
                 System.out.println("GameManager: Columbus caught by monster at " + columbusShip.getPosition());
                 return; // Game ends
            }
        }

        // Check if Columbus reached the treasure
        Position treasurePos = oceanMap.getTreasurePosition();
        if (treasurePos != null && columbusShip.getPosition().equals(treasurePos)) {
             gameState.setGameOver(true);
             gameState.setStatusMessage("Columbus found the treasure at [" + treasurePos.getX() + "," + treasurePos.getY() + "]! You Win!");
             System.out.println("GameManager: Columbus won at " + columbusShip.getPosition());
             return; // Game ends
        }

        // If game is not over, update status message
        if (!gameState.isGameOver()) {
             gameState.setStatusMessage("Columbus at [" + columbusShip.getPosition().getX() + "," + columbusShip.getPosition().getY() + "]");
        }
    }

    // Method to restart the game
    public synchronized void restartGame() {
        System.out.println("GameManager: Received restart request.");
        initializeGame(); // Re-initialize the game state
    }


    // Removed startGameLoop method

    // Removed checkWinCondition / checkLoseCondition (integrated into updateGameStatus)

    // Getters needed by the WebServer to serialize state
    public GameState getGameState() {
        // Defensively check if gameState is null (e.g., during initial construction)
        return gameState != null ? gameState : new GameState();
    }

     public OceanMap getOceanMap() {
         return oceanMap;
     }

     public ColumbusShip getColumbusShip() {
         return columbusShip;
     }

     public List<PirateShip> getPirateShips() {
         return pirateShips;
     }

     public List<SeaMonster> getSeaMonsters() {
         return seaMonsters;
     }
} 