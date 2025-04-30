import java.awt.Point;
import java.util.Observable;

public class Ship extends Observable {
  private Point currentLocation;
  private OceanMap oceanMap;
  private int oceanSize;
  
  public Ship(OceanMap oceanMap) {
    this.oceanMap = oceanMap;
    this.oceanSize = oceanMap.getMap().length;
    this.currentLocation = new Point(oceanSize / 2, oceanSize / 2); // Start in center
  }
  
  public Point getShipLocation() {
    return currentLocation;
  }
  
  public void goNorth() {
    if (currentLocation.y > 0 && !oceanMap.isIsland(currentLocation.x, currentLocation.y - 1)) {
      currentLocation.y--;
      setChanged();
      notifyObservers();
    }
  }
  
  public void goSouth() {
    if (currentLocation.y < oceanSize - 1 && !oceanMap.isIsland(currentLocation.x, currentLocation.y + 1)) {
      currentLocation.y++;
      setChanged();
      notifyObservers();
    }
  }
  
  public void goEast() {
    if (currentLocation.x < oceanSize - 1 && !oceanMap.isIsland(currentLocation.x + 1, currentLocation.y)) {
      currentLocation.x++;
      setChanged();
      notifyObservers();
    }
  }
  
  public void goWest() {
    if (currentLocation.x > 0 && !oceanMap.isIsland(currentLocation.x - 1, currentLocation.y)) {
      currentLocation.x--;
      setChanged();
      notifyObservers();
    }
  }
}
