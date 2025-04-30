package com.csci513.finalproject.composite;

import java.util.ArrayList;
import java.util.List;

// Composite class for grouping OceanFeatures (e.g., SeaMonsters, Whirlpools).
public class FeatureGroup implements OceanFeature {

    private List<OceanFeature> features = new ArrayList<>();
    private String groupName;
    // Could add boundary definitions here

    public FeatureGroup(String groupName) {
        this.groupName = groupName;
    }

    public void addFeature(OceanFeature feature) {
        features.add(feature);
    }

    public void removeFeature(OceanFeature feature) {
        features.remove(feature);
    }

    @Override
    public void activate() {
        System.out.println("Activating feature group: " + groupName);
        for (OceanFeature feature : features) {
            feature.activate();
        }
    }

    @Override
    public void deactivate() {
        System.out.println("Deactivating feature group: " + groupName);
        for (OceanFeature feature : features) {
            feature.deactivate();
        }
    }

    // Could add methods to check if a position is within the group's area
} 