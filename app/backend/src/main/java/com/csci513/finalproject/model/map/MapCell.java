package com.csci513.finalproject.model.map;

// Represents a single cell in the OceanMap grid.
public class MapCell {
    private boolean isIsland = false;
    private boolean hasTreasure;
    private boolean isStrategySwitcher = false; // New field for knife cells
    // Add other properties like terrain type, containsMonster, containsPowerUp etc.

    public MapCell() {
        this.isIsland = false;
        this.hasTreasure = false;
        this.isStrategySwitcher = false; // Initialize
    }

    // Getters and setters
    public boolean isIsland() {
        return isIsland;
    }

    public void setIsland(boolean island) {
        isIsland = island;
    }

    public boolean hasTreasure() {
        return hasTreasure;
    }

    public void setHasTreasure(boolean hasTreasure) {
        this.hasTreasure = hasTreasure;
    }

    // Getter for strategy switcher
    public boolean isStrategySwitcher() {
        return isStrategySwitcher;
    }

    // Setter for strategy switcher
    public void setStrategySwitcher(boolean strategySwitcher) {
        isStrategySwitcher = strategySwitcher;
    }

    // TODO: Add properties like terrain type, containsPowerUp etc.
} 