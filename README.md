[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/0KYOwdvP)


# CSCI 513 Final Project Repository

## File Structure

- `hw2-old/` - Original Christopher Columbus project from Homework 2 (starting point)
- `hw4-old/` - From Homework 4 (frontend/backend game setup)

- `app/` - Main project directory for the enhanced Christopher Columbus game
  - `backend/` - Java backend application
    - `src/main/java/com/csci513/finalproject/` - Java source code
    - `src/test/` - JUnit test files
    - `pom.xml` - Maven configuration file
  - `frontend/` - React TypeScript frontend
    - `src/` - Frontend source code
      - `components/` - React components
      - `hooks/` - Custom React hooks
      - `services/` - Services for API calls and game logic
    - `public/` - Static assets

- `diagram.drawio` & `diagram.drawio.png` - Project design diagrams
- `README.md` - This file. 
- `final-presentation.pptx` - Presentation slides presented on Apr 30 class.

## Game functionality

The player controls Christopher Columbus's ship on a grid-based ocean map, navigating around islands to find hidden treasure. The objective is to locate the treasure before being caught by pursuing pirate ships or lurking sea monsters. Pirate ships employ different movement strategies to hunt the player. A win occurs upon finding the treasure; a loss occurs if the player's ship is captured.

## Design implementation

This project implements five core design patterns:

1.  **Observer:** Used for decoupling the Pirate Ships (Observers) from Christopher Columbus's ship (Observable). When Columbus moves, the Pirates are notified and can react.
    ```java
    // package com.csci513.finalproject.observer;
    public interface Observer {
        void update(Observable observable);
    }
    public interface Observable {
        void registerObserver(Observer observer);
        void removeObserver(Observer observer);
        void notifyObservers();
    }
    ```

2.  **Singleton:** Ensures only one instance of the `OceanMap` exists throughout the application. This provides a global access point to the game map data (grid, islands, treasure location) without passing the map object around explicitly.
    ```java
    // package com.csci513.finalproject.model.map;
    public class OceanMap {
        private static OceanMap instance;
        private OceanMap(int width, int height) { /* ... */ }
        public static synchronized OceanMap getInstance(int width, int height) {
            if (instance == null) {
                instance = new OceanMap(width, height);
            }
            return instance;
        }
        public static synchronized OceanMap getInstance() { /* ... */ }
        // ... map methods ...
    }
    ```

3.  **Strategy:** Defines a family of algorithms for pirate ship movement (e.g., `ChaseStrategy`, `PatrolStrategy`, `PredictiveChaseStrategy`) and encapsulates each one. This allows the movement behavior of a pirate ship to be selected or changed at runtime.
    ```java
    // package com.csci513.finalproject.strategy;
    public interface MovementStrategy {
        Position move(Position currentPosition);
        // Implementations: ChaseStrategy, PatrolStrategy, PredictiveChaseStrategy
    }
    ```

4.  **Factory Method:** An abstract `PirateShipFactory` defines an interface for creating pirate ship objects, but lets subclasses (`StandardPirateShipFactory`) decide which class to instantiate. This allows for easy extension with different types of pirate ships.
    ```java
    // package com.csci513.finalproject.factory;
    public abstract class PirateShipFactory {
        public abstract PirateShip createPirateShip(String type, Position position);
        // ... other methods like orderPirateShip ...
    }
    // Example subclass:
    // public class StandardPirateShipFactory extends PirateShipFactory { ... }
    ```

5.  **Composite:** Allows treating individual ocean features (like whirlpools, monsters - leaves) and groups of features (like a monster pack within defined boundaries - composites) uniformly. The `OceanFeature` interface represents the common component.
    ```java
    // package com.csci513.finalproject.composite;
    public interface OceanFeature {
        void activate();
        void deactivate();
    }
    // Example Composite: FeatureGroup
    // Example Leaf: Could be Monster, Whirlpool etc. implementing OceanFeature
    ```

