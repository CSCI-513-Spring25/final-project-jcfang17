package com.csci513.finalproject.core;

import com.csci513.finalproject.model.characters.ColumbusShip;
import com.csci513.finalproject.model.characters.PirateShip;
import com.csci513.finalproject.model.characters.SeaMonster;
import com.csci513.finalproject.model.map.OceanMap;
import com.csci513.finalproject.utils.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GameManagerTest {

    private GameManager gameManager;
    private OceanMap oceanMap; // Access the singleton map instance used by GameManager

    @BeforeEach
    void setUp() {
        // Need to reset the singleton before each test if tests modify its state implicitly
        // This is tricky with singletons. A better approach might involve dependency injection
        // or a reset method on the singleton for testing.
        // For now, we assume GameManager() correctly gets/initializes the map.
        gameManager = new GameManager();
        oceanMap = OceanMap.getInstance(); // Get the same instance GameManager uses
    }

    @Test
    @DisplayName("Constructor initializes game components")
    void testConstructorInitialization() {
        assertNotNull(gameManager.getOceanMap(), "OceanMap should be initialized");
        assertNotNull(gameManager.getGameState(), "GameState should be initialized");
        assertNotNull(gameManager.getColumbusShip(), "ColumbusShip should be initialized");
        assertNotNull(gameManager.getPirateShips(), "PirateShips list should be initialized");
        assertNotNull(gameManager.getSeaMonsters(), "SeaMonsters list should be initialized");

        assertFalse(gameManager.getPirateShips().isEmpty(), "PirateShips list should not be empty");
        assertFalse(gameManager.getSeaMonsters().isEmpty(), "SeaMonsters list should not be empty");

        // Check if player starts at (0,0)
        assertEquals(new Position(0, 0), gameManager.getColumbusShip().getPosition(), "Columbus should start at (0,0)");

        // Basic check for unique positions (more robust test is complex)
        List<Position> initialPositions = Stream.concat(
                Stream.of(gameManager.getColumbusShip().getPosition()),
                Stream.concat(
                    gameManager.getPirateShips().stream().map(PirateShip::getPosition),
                    gameManager.getSeaMonsters().stream().map(SeaMonster::getPosition)
                )
            ).collect(Collectors.toList());
        long distinctPositions = initialPositions.stream().distinct().count();
        assertEquals(initialPositions.size(), distinctPositions, "All initial character positions should be unique");

        assertFalse(gameManager.getGameState().isGameOver(), "Game should not be over initially");
    }

    @Test
    @DisplayName("processPlayerMove updates Columbus position")
    void testProcessPlayerMove_UpdatesPosition() {
        Position initialPos = gameManager.getColumbusShip().getPosition();
        assertEquals(new Position(0, 0), initialPos);

        gameManager.processPlayerMove("RIGHT");
        Position posAfterRight = gameManager.getColumbusShip().getPosition();
        assertEquals(new Position(1, 0), posAfterRight, "Position should be (1,0) after moving RIGHT");

        gameManager.processPlayerMove("DOWN");
        Position posAfterDown = gameManager.getColumbusShip().getPosition();
        assertEquals(new Position(1, 1), posAfterDown, "Position should be (1,1) after moving DOWN");

        gameManager.processPlayerMove("LEFT");
        Position posAfterLeft = gameManager.getColumbusShip().getPosition();
        assertEquals(new Position(0, 1), posAfterLeft, "Position should be (0,1) after moving LEFT");

        gameManager.processPlayerMove("UP");
        Position posAfterUp = gameManager.getColumbusShip().getPosition();
        assertEquals(new Position(0, 0), posAfterUp, "Position should be (0,0) after moving UP");
    }

     @Test
    @DisplayName("processPlayerMove handles invalid direction")
    void testProcessPlayerMove_InvalidDirection() {
        Position initialPos = gameManager.getColumbusShip().getPosition();
        gameManager.processPlayerMove("INVALID");
        assertEquals(initialPos, gameManager.getColumbusShip().getPosition(), "Position should not change on invalid direction");
    }

     @Test
    @DisplayName("processPlayerMove handles out-of-bounds move")
    void testProcessPlayerMove_OutOfBounds() {
        Position initialPos = gameManager.getColumbusShip().getPosition(); // Starts at (0,0)

        gameManager.processPlayerMove("UP");
        assertEquals(initialPos, gameManager.getColumbusShip().getPosition(), "Position should not change when moving UP from (0,0)");

        gameManager.processPlayerMove("LEFT");
        assertEquals(initialPos, gameManager.getColumbusShip().getPosition(), "Position should not change when moving LEFT from (0,0)");
    }

     @Test
     @DisplayName("processPlayerMove triggers pirate moves")
     void testProcessPlayerMove_TriggersPirateMoves() {
         // This is hard to test precisely without knowing exact strategies/positions
         // We can check if pirate positions *change* after a player move
         List<Position> initialPiratePositions = gameManager.getPirateShips().stream()
                                                        .map(PirateShip::getPosition)
                                                        .collect(Collectors.toList());

         gameManager.processPlayerMove("RIGHT"); // Player moves

         List<Position> finalPiratePositions = gameManager.getPirateShips().stream()
                                                      .map(PirateShip::getPosition)
                                                      .collect(Collectors.toList());

         // Assert that *at least one* pirate position is likely different (unless they were blocked/at target)
         // This isn't a perfect test, but indicates movement was attempted.
         boolean positionsChanged = false;
         for (int i = 0; i < initialPiratePositions.size(); i++) {
             if (!initialPiratePositions.get(i).equals(finalPiratePositions.get(i))) {
                 positionsChanged = true;
                 break;
             }
         }
         assertTrue(positionsChanged, "At least one pirate position should likely change after player move");
     }

    @Test
    @DisplayName("Game ends when Columbus reaches treasure")
    void testWinCondition() {
        Position treasurePos = oceanMap.getTreasurePosition();
        ColumbusShip columbus = gameManager.getColumbusShip();

        // Manually set Columbus position to the treasure location
        // Note: This bypasses regular movement logic, directly testing the win check
        ((GameCharacter)columbus).setPosition(treasurePos);

        // Process a dummy move to trigger the status update
        gameManager.processPlayerMove("STAY"); // Assuming STAY is not a valid move, just triggers check

        assertTrue(gameManager.getGameState().isGameOver(), "Game should be over after reaching treasure");
        assertTrue(gameManager.getGameState().getStatusMessage().contains("You Win!"), "Status message should indicate win");
    }

    @Test
    @DisplayName("Game ends when Columbus is caught by Pirate")
    void testLoseCondition_PirateCatch() {
        PirateShip firstPirate = gameManager.getPirateShips().get(0);
        ColumbusShip columbus = gameManager.getColumbusShip();
        Position piratePos = firstPirate.getPosition();

        // Move Columbus to the pirate's position
        ((GameCharacter)columbus).setPosition(piratePos);

        // Process a dummy move
        gameManager.processPlayerMove("STAY");

        assertTrue(gameManager.getGameState().isGameOver(), "Game should be over after being caught by pirate");
        assertTrue(gameManager.getGameState().getStatusMessage().contains("Caught by a pirate"), "Status message should indicate caught by pirate");
    }

    @Test
    @DisplayName("Game ends when Columbus is caught by Sea Monster")
    void testLoseCondition_MonsterCatch() {
         SeaMonster firstMonster = gameManager.getSeaMonsters().get(0);
         ColumbusShip columbus = gameManager.getColumbusShip();
         Position monsterPos = firstMonster.getPosition();

         // Activate monster just in case it could be inactive
         firstMonster.activate();

         // Move Columbus to the monster's position
         ((GameCharacter)columbus).setPosition(monsterPos);

         // Process a dummy move
         gameManager.processPlayerMove("STAY");

         assertTrue(gameManager.getGameState().isGameOver(), "Game should be over after being caught by monster");
         assertTrue(gameManager.getGameState().getStatusMessage().contains("Caught by a Sea Monster"), "Status message should indicate caught by monster");
    }
} 