package com.csci513.finalproject.composite;

// Component interface for the Composite pattern.
// Represents individual features (leaves) or groups of features (composites).
public interface OceanFeature {
    void activate();   // Example operation: start movement/effect
    void deactivate(); // Example operation: stop movement/effect
    // Could add methods like getPosition(), isInBounds(Position p), etc.
} 