package com.csci513.finalproject.model.characters;

import com.csci513.finalproject.utils.Position;

// Abstract base class for all movable entities in the game.
public abstract class GameCharacter {

    private Position position;

    public GameCharacter(int x, int y) {
        this.position = new Position(x, y);
    }

    public Position getPosition() {
        return position;
    }

    protected void setPosition(Position position) {
        this.position = position;
    }

    // Abstract method for movement logic, to be implemented by subclasses.
    public abstract void move();

} 