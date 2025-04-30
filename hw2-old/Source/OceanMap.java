import java.util.Random;

public class OceanMap {
  private int[][] oceanGrid;
  private int dimension;
  private int numIslands;
  
  public OceanMap(int dimension, int numIslands) {
    this.dimension = dimension;
    this.numIslands = numIslands;
    oceanGrid = new int[dimension][dimension];
    
    generateIslands();
  }
  
  private void generateIslands() {
    Random rand = new Random();
    int count = 0;
    while (count < numIslands) {
      int x = rand.nextInt(dimension);
      int y = rand.nextInt(dimension);
      if (oceanGrid[x][y] == 0) {  // Only place an island if the spot is empty
        oceanGrid[x][y] = 1;  // 1 represents an island
        count++;
      }
    }
  }
  
  public int[][] getMap() {
    return oceanGrid;
  }
  
  public boolean isIsland(int x, int y) {
    return oceanGrid[x][y] == 1;
  }
}
