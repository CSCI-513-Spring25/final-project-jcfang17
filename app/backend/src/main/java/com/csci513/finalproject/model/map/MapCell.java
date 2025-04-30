package com.csci513.finalproject.model.map;

// Represents a single cell in the OceanMap grid.
public class MapCell {
    private boolean isIsland = false;
    private boolean hasTreasure;
    // Add other properties like terrain type, containsMonster, containsPowerUp etc.

    public MapCell() {
        this.isIsland = false;
        this.hasTreasure = false;
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

    // TODO: Add properties like terrain type, containsPowerUp etc.
} 