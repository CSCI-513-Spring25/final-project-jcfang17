package com.csci513.finalproject.model.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MapCellTest {

    private MapCell cell;

    @BeforeEach
    void setUp() {
        cell = new MapCell();
    }

    @Test
    void testDefaultState() {
        assertFalse(cell.isIsland(), "Default cell should not be an island");
        assertFalse(cell.hasTreasure(), "Default cell should not have treasure");
        assertFalse(cell.isStrategySwitcher(), "Default cell should not be a strategy switcher");
    }

    @Test
    void testSetIsland() {
        cell.setIsland(true);
        assertTrue(cell.isIsland(), "Cell should be an island after setting");
        assertFalse(cell.hasTreasure(), "Setting island should not affect treasure");
        assertFalse(cell.isStrategySwitcher(), "Setting island should not affect strategy switcher");

        cell.setIsland(false);
        assertFalse(cell.isIsland(), "Cell should not be an island after unsetting");
    }

    @Test
    void testSetHasTreasure() {
        cell.setHasTreasure(true);
        assertTrue(cell.hasTreasure(), "Cell should have treasure after setting");
        assertFalse(cell.isIsland(), "Setting treasure should not affect island");
        assertFalse(cell.isStrategySwitcher(), "Setting treasure should not affect strategy switcher");

        cell.setHasTreasure(false);
        assertFalse(cell.hasTreasure(), "Cell should not have treasure after unsetting");
    }

    @Test
    void testSetStrategySwitcher() {
        cell.setStrategySwitcher(true);
        assertTrue(cell.isStrategySwitcher(), "Cell should be a strategy switcher after setting");
        assertFalse(cell.isIsland(), "Setting strategy switcher should not affect island");
        assertFalse(cell.hasTreasure(), "Setting strategy switcher should not affect treasure");

        cell.setStrategySwitcher(false);
        assertFalse(cell.isStrategySwitcher(), "Cell should not be a strategy switcher after unsetting");
    }
} 