package com.csci513.finalproject.model.map;

import com.csci513.finalproject.utils.Position;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OceanMapTest {

    private static final int TEST_WIDTH = 25;
    private static final int TEST_HEIGHT = 25;
    private static OceanMap oceanMapInstance;

    @BeforeAll
    static void setUp() {
        // Get the singleton instance once for all tests
        oceanMapInstance = OceanMap.getInstance(TEST_WIDTH, TEST_HEIGHT);
    }

    @Test
    @DisplayName("getInstance() returns Singleton instance")
    void testSingletonInstance() {
        assertNotNull(oceanMapInstance, "Instance should not be null");
        OceanMap anotherInstance = OceanMap.getInstance(); // Get instance without size after init
        assertSame(oceanMapInstance, anotherInstance, "Instances should be the same object");
        OceanMap thirdInstance = OceanMap.getInstance(TEST_WIDTH, TEST_HEIGHT); // Get again with size
        assertSame(oceanMapInstance, thirdInstance, "Instances should still be the same object");
    }

    @Test
    @DisplayName("Map dimensions are set correctly")
    void testMapDimensions() {
        assertEquals(TEST_WIDTH, oceanMapInstance.getWidth(), "Width should match constructor argument");
        assertEquals(TEST_HEIGHT, oceanMapInstance.getHeight(), "Height should match constructor argument");
    }

    @Test
    @DisplayName("Treasure is placed within bounds and not at (0,0)")
    void testTreasurePlacement() {
        Position treasurePos = oceanMapInstance.getTreasurePosition();
        assertNotNull(treasurePos, "Treasure position should not be null");

        assertTrue(treasurePos.getX() >= 0 && treasurePos.getX() < TEST_WIDTH,
                   "Treasure X coordinate out of bounds");
        assertTrue(treasurePos.getY() >= 0 && treasurePos.getY() < TEST_HEIGHT,
                   "Treasure Y coordinate out of bounds");

        assertFalse(treasurePos.getX() == 0 && treasurePos.getY() == 0,
                    "Treasure should not be placed at the starting position (0,0)");
    }

    @Test
    @DisplayName("getCell() returns null for out-of-bounds coordinates")
    void testGetCellBounds() {
        assertNull(oceanMapInstance.getCell(-1, 0), "Cell at x=-1 should be null");
        assertNull(oceanMapInstance.getCell(0, -1), "Cell at y=-1 should be null");
        assertNull(oceanMapInstance.getCell(TEST_WIDTH, 0), "Cell at x=width should be null");
        assertNull(oceanMapInstance.getCell(0, TEST_HEIGHT), "Cell at y=height should be null");
    }

    @Test
    @DisplayName("getCell() returns non-null for in-bounds coordinates")
    void testGetCellInBounds() {
         assertNotNull(oceanMapInstance.getCell(0, 0), "Cell at (0,0) should not be null");
         assertNotNull(oceanMapInstance.getCell(TEST_WIDTH - 1, TEST_HEIGHT - 1), "Cell at max bounds should not be null");
         assertNotNull(oceanMapInstance.getCell(TEST_WIDTH / 2, TEST_HEIGHT / 2), "Cell at center should not be null");
    }
} 