import java.awt.Point;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class PirateShip implements Observer {
  private Point location;
  private Ship columbusShip;
  private OceanMap oceanMap;
  
  public PirateShip(OceanMap oceanMap, Ship columbusShip) {
    this.oceanMap = oceanMap;
    this.columbusShip = columbusShip;
    columbusShip.addObserver(this);
    
    Random rand = new Random();
    do {
      location = new Point(rand.nextInt(oceanMap.getMap().length), rand.nextInt(oceanMap.getMap().length));
    } while (oceanMap.isIsland(location.x, location.y));  // Ensure pirates don't spawn on islands
  }
  
  public Point getLocation() {
    return location;
  }
  
  @Override
  public void update(Observable o, Object arg) {
    chaseColumbus();
  }
  
  private void chaseColumbus() {
    int shipX = columbusShip.getShipLocation().x;
    int shipY = columbusShip.getShipLocation().y;
    int pirateX = location.x;
    int pirateY = location.y;
    
    // Move only in one direction at a time (horizontal or vertical)
    if (Math.abs(shipX - pirateX) > Math.abs(shipY - pirateY)) {
      // Move horizontally if X distance is greater
      if (shipX > pirateX && !oceanMap.isIsland(pirateX + 1, pirateY)) {
        location.x++;
      } else if (shipX < pirateX && !oceanMap.isIsland(pirateX - 1, pirateY)) {
        location.x--;
      }
    } else {
      // Move vertically if Y distance is greater or equal
      if (shipY > pirateY && !oceanMap.isIsland(pirateX, pirateY + 1)) {
        location.y++;
      } else if (shipY < pirateY && !oceanMap.isIsland(pirateX, pirateY - 1)) {
        location.y--;
      }
    }
  }
}
