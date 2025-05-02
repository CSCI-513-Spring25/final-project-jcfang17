package com.csci513.finalproject.model.map;

import com.csci513.finalproject.utils.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Represents the game map grid, implemented as a Singleton.
public class OceanMap {

    private static OceanMap instance;
    private MapCell[][] grid;
    private int width;
    private int height;
    private Position treasurePosition;
    private List<Position> islandPositions = new ArrayList<>(); // Store island locations
    private Random random = new Random();

    // Private constructor to prevent direct instantiation
    private OceanMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new MapCell[height][width];
        initializeGrid();
        System.out.println("OceanMap Singleton created with size " + width + "x" + height);
    }

    // Public method to get the single instance
    public static synchronized OceanMap getInstance(int width, int height) {
        if (instance == null) {
            instance = new OceanMap(width, height);
        } else if (instance.width != width || instance.height != height) {
            // Handle potential resize/reinitialization if needed, or throw error
            System.err.println("Warning: Requested OceanMap size differs from existing instance! Re-initializing map.");
            instance = new OceanMap(width, height);
        }
        return instance;
    }

    // Convenience getInstance() without size - requires prior initialization
    public static synchronized OceanMap getInstance() {
        if (instance == null) {
            throw new IllegalStateException("OceanMap has not been initialized. Call getInstance(width, height) first.");
        }
        return instance;
    }

    private void initializeGrid() {
        // Reset island list for reinitialization
        this.islandPositions.clear();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = new MapCell();
            }
        }
        // Place some islands
        placeIslands(5); // Place 5 islands

        // Place treasure, ensuring it's not on an island or start pos
        placeTreasureRandomly();

        // Place strategy switcher cells
        placeStrategySwitchers(3); // Place 3 switcher cells

        System.out.println("OceanMap grid initialized with islands and switchers.");
    }

    // Helper to place a number of single-cell islands randomly
    private void placeIslands(int numberOfIslands) {
         System.out.println("Placing " + numberOfIslands + " islands...");
         int islandsPlaced = 0;
         while (islandsPlaced < numberOfIslands) {
             int x = random.nextInt(width);
             int y = random.nextInt(height);
             // Avoid placing on start position (0,0) or on existing islands
             if (!isIsland(x, y) && !(x == 0 && y == 0)) {
                 grid[y][x].setIsland(true);
                 islandPositions.add(new Position(x, y));
                 islandsPlaced++;
                 System.out.println("Placed island at: [" + x + "," + y + "]");
             }
         }
    }

    private void placeTreasureRandomly() {
        int x, y;
        do {
            x = random.nextInt(width);
            y = random.nextInt(height);
            // Ensure treasure is not at start (0,0) and not on an island
        } while ((x == 0 && y == 0) || isIsland(x, y));

        this.treasurePosition = new Position(x, y);
        // if (grid[y][x] != null) { grid[y][x].setHasTreasure(true); }
        System.out.println("Treasure placed at: [" + x + "," + y + "]");
    }

    public Position getTreasurePosition() {
        return treasurePosition;
    }

    public MapCell getCell(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[y][x];
        }
        return null; // Out-of-bounds
    }

    // Helper method to check if a coordinate is an island
    public boolean isIsland(int x, int y) {
         if (x >= 0 && x < width && y >= 0 && y < height) {
             return grid[y][x].isIsland();
         }
         return false; // Out of bounds is not an island
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Position> getIslandPositions() {
         return islandPositions;
    }

    // New method to get positions of all strategy switcher cells
    public List<Position> getStrategySwitcherPositions() {
        List<Position> switcherPositions = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x].isStrategySwitcher()) {
                    switcherPositions.add(new Position(x, y));
                }
            }
        }
        return switcherPositions;
    }

    // Helper to place a number of strategy switcher cells randomly
    private void placeStrategySwitchers(int numberOfSwitchers) {
        System.out.println("Placing " + numberOfSwitchers + " strategy switchers...");
        int switchersPlaced = 0;
        while (switchersPlaced < numberOfSwitchers) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Position potentialPos = new Position(x, y);
            // Avoid placing on start (0,0), islands, treasure, or existing switchers
            if (!isIsland(x, y) &&
                !(x == 0 && y == 0) &&
                !potentialPos.equals(treasurePosition) &&
                !getCell(x,y).isStrategySwitcher()) // Check if already a switcher
            {
                getCell(x, y).setStrategySwitcher(true);
                switchersPlaced++;
                System.out.println("Placed strategy switcher at: [" + x + "," + y + "]");
            }
        }
    }

    // TODO: Add method to place islands on the map - Replaced by placeIslands helper
    // public void placeIsland(int x, int y) { ... }
} 