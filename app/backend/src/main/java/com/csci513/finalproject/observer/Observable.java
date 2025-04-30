package com.csci513.finalproject.observer;

// Observable interface for the Observer pattern.
public interface Observable {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
} 