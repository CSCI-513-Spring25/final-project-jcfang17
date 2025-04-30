import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;

public class OceanExplorer extends Application {
  final int dimension = 10;
  final int scale = 50;
  OceanMap oceanMap;
  Ship ship;
  ArrayList<PirateShip> pirateShips;
  AnchorPane root;
  Scene scene;
  ImageView shipImageView;
  ArrayList<ImageView> pirateImages;
  
  @Override
  public void start(Stage oceanStage) {
    oceanMap = new OceanMap(dimension, 10);
    ship = new Ship(oceanMap);
    pirateShips = new ArrayList<>();
    
    for (int i = 0; i < 2; i++) {
      pirateShips.add(new PirateShip(oceanMap, ship));
    }
    
    root = new AnchorPane();
    scene = new Scene(root, dimension * scale, dimension * scale);
    
    drawMap();
    loadShipImage();
    loadPirates();
    
    oceanStage.setTitle("Christopher Columbus vs Pirates!");
    oceanStage.setScene(scene);
    oceanStage.show();
    
    startSailing();
  }
  
  private void drawMap() {
    int[][] oceanGrid = oceanMap.getMap();
    for (int x = 0; x < dimension; x++) {
      for (int y = 0; y < dimension; y++) {
        Rectangle rect = new Rectangle(x * scale, y * scale, scale, scale);
        rect.setStroke(Color.BLACK);
        rect.setFill(oceanGrid[x][y] == 1 ? Color.GREEN : Color.PALETURQUOISE);
        root.getChildren().add(rect);
      }
    }
  }
  
  private void loadShipImage() {
    Image shipImage = new Image("file:ship.png", 50, 50, true, true);
    shipImageView = new ImageView(shipImage);
    updateShipPosition();
    root.getChildren().add(shipImageView);
  }
  
  private void loadPirates() {
    pirateImages = new ArrayList<>();
    for (PirateShip pirate : pirateShips) {
      ImageView pirateView = new ImageView(new Image("file:pirateShip.png", 50, 50, true, true));
      pirateImages.add(pirateView);
      root.getChildren().add(pirateView);
    }
    updatePiratePositions();
  }
  
  private void startSailing() {
    scene.setOnKeyPressed((KeyEvent ke) -> {
      switch (ke.getCode()) {
        case RIGHT: ship.goEast(); break;
        case LEFT: ship.goWest(); break;
        case UP: ship.goNorth(); break;
        case DOWN: ship.goSouth(); break;
      }
      updateShipPosition();
      updatePiratePositions();
    });
  }
  
  private void updateShipPosition() {
    shipImageView.setX(ship.getShipLocation().x * scale);
    shipImageView.setY(ship.getShipLocation().y * scale);
  }
  
  private void updatePiratePositions() {
    for (int i = 0; i < pirateShips.size(); i++) {
      pirateImages.get(i).setX(pirateShips.get(i).getLocation().x * scale);
      pirateImages.get(i).setY(pirateShips.get(i).getLocation().y * scale);
    }
  }
  
  public static void main(String[] args) {
    launch(args);
  }
}
