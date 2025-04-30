package com.csci513.finalproject;

import com.csci513.finalproject.core.GameManager;
import com.csci513.finalproject.core.WebServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            // Create the game manager instance
            GameManager gameManager = new GameManager();

            // Start the web server, passing the game manager
            new WebServer(8080, gameManager); // Listen on port 8080

            System.out.println("Backend server started.");
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }
} 