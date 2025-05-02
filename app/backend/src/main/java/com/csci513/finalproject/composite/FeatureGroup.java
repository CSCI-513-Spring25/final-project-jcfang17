package com.csci513.finalproject.composite;

import java.util.ArrayList;
import java.util.List;

// Composite class for grouping OceanFeatures (e.g., SeaMonsters, Whirlpools).
public class FeatureGroup implements OceanFeature {

    private List<OceanFeature> features = new ArrayList<>();
    private String groupName;

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

    // usage: can be used for user to temperary disable a group of features, 
    // like stopping pirates from chasing columbus for several turns
} 