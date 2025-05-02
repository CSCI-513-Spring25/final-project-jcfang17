package com.csci513.finalproject.core;

import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Simple web server using NanoHTTPD to expose game state and actions.
public class WebServer extends NanoHTTPD {

    private GameManager gameManager;

    public WebServer(int port, GameManager gameManager) throws IOException {
        super(port);
        this.gameManager = gameManager;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:" + port + "/ \n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Method method = session.getMethod();
        Map<String, String> headers = session.getHeaders();

        // Allow CORS requests from the frontend (adjust origin if needed)
        Response response = newFixedLengthResponse(Response.Status.OK, "application/json", "{}");
        response.addHeader("Access-Control-Allow-Origin", "*"); // Allow all origins for simplicity
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");

        // Handle CORS preflight requests (OPTIONS)
        if (Method.OPTIONS.equals(method)) {
            return response; // Just return headers for OPTIONS
        }

        System.out.println("WebServer received: " + method + " " + uri);

        try {
            if (uri.equals("/state") && Method.GET.equals(method)) {
                return getState();
            } else if (uri.equals("/action/move") && Method.POST.equals(method)) {
                return handleMoveAction(session);
            } else if (uri.equals("/action/restart") && Method.POST.equals(method)) {
                return handleRestartAction();
            }
        } catch (Exception e) {
            System.err.println("Error handling request: " + e.getMessage());
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Internal Server Error: " + e.getMessage());
        }

        // Default response for unknown routes
        response = newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "Not Found");
        response.addHeader("Access-Control-Allow-Origin", "*");
        return response;
    }

    private Response getState() {
        // Create JSON representation of the game state
        JSONObject jsonState = new JSONObject();
        JSONObject mapJson = new JSONObject();
        mapJson.put("width", gameManager.getOceanMap().getWidth());
        mapJson.put("height", gameManager.getOceanMap().getHeight());
        // Add island positions
        JSONArray islandsJson = new JSONArray();
        gameManager.getOceanMap().getIslandPositions().forEach(islandPos -> {
            JSONObject islandJson = new JSONObject();
            islandJson.put("x", islandPos.getX());
            islandJson.put("y", islandPos.getY());
            islandsJson.put(islandJson);
        });
        mapJson.put("islands", islandsJson);

        // Add strategy switcher positions
        JSONArray switchersJson = new JSONArray();
        gameManager.getOceanMap().getStrategySwitcherPositions().forEach(switcherPos -> {
            JSONObject switcherJson = new JSONObject();
            switcherJson.put("x", switcherPos.getX());
            switcherJson.put("y", switcherPos.getY());
            switchersJson.put(switcherJson);
        });
        mapJson.put("strategySwitchers", switchersJson);

        // TODO: Add grid details if needed by frontend for rendering terrain

        JSONObject playerJson = new JSONObject();
        playerJson.put("x", gameManager.getColumbusShip().getPosition().getX());
        playerJson.put("y", gameManager.getColumbusShip().getPosition().getY());

        JSONObject treasureJson = new JSONObject();
        treasureJson.put("x", gameManager.getOceanMap().getTreasurePosition().getX());
        treasureJson.put("y", gameManager.getOceanMap().getTreasurePosition().getY());

        JSONArray piratesJson = new JSONArray();
        gameManager.getPirateShips().forEach(pirate -> {
            JSONObject pirateJson = new JSONObject();
            pirateJson.put("x", pirate.getPosition().getX());
            pirateJson.put("y", pirate.getPosition().getY());
            pirateJson.put("type", pirate.getClass().getSimpleName()); // e.g., "ChaserPirateShip"
            piratesJson.put(pirateJson);
        });

        JSONArray monstersJson = new JSONArray();
        gameManager.getSeaMonsters().forEach(monster -> {
             if (monster.isActive()) { // Only send active monsters
                 JSONObject monsterJson = new JSONObject();
                 monsterJson.put("x", monster.getPosition().getX());
                 monsterJson.put("y", monster.getPosition().getY());
                 monstersJson.put(monsterJson);
             }
        });

        JSONObject statusJson = new JSONObject();
        statusJson.put("isGameOver", gameManager.getGameState().isGameOver());
        statusJson.put("message", gameManager.getGameState().getStatusMessage());

        jsonState.put("map", mapJson);
        jsonState.put("player", playerJson);
        jsonState.put("treasure", treasureJson);
        jsonState.put("pirates", piratesJson);
        jsonState.put("monsters", monstersJson);
        jsonState.put("status", statusJson);

        Response response = newFixedLengthResponse(Response.Status.OK, "application/json", jsonState.toString());
        response.addHeader("Access-Control-Allow-Origin", "*");
        return response;
    }

    private Response handleMoveAction(IHTTPSession session) throws IOException, ResponseException {
        // Need to parse the POST body to get the direction
        Map<String, String> files = new HashMap<>();
        session.parseBody(files); // Parses application/x-www-form-urlencoded or multipart/form-data

        String postBody = files.get("postData"); // For urlencoded/raw body
        if (postBody == null) {
             // If content type is application/json, NanoHTTPD doesn't parse it automatically
             // We need to read the input stream manually
             int contentLength = Integer.parseInt(session.getHeaders().get("content-length"));
             byte[] buf = new byte[contentLength];
             session.getInputStream().read(buf, 0, contentLength);
             postBody = new String(buf);
        }

        String direction = null;
        try {
             JSONObject jsonBody = new JSONObject(postBody);
             if (jsonBody.has("direction")) {
                  direction = jsonBody.getString("direction");
             }
        } catch (Exception e) {
             System.err.println("Error parsing move action JSON: " + e.getMessage());
             Response response = newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Invalid JSON format for move action.");
             response.addHeader("Access-Control-Allow-Origin", "*");
             return response;
        }


        if (direction != null && !direction.isEmpty()) {
            gameManager.processPlayerMove(direction);
            // Return the new game state after the move
            return getState();
        } else {
            Response response = newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Missing or invalid 'direction' parameter in request body.");
             response.addHeader("Access-Control-Allow-Origin", "*");
             return response;
        }
    }

    // Handles the restart action
    private Response handleRestartAction() {
        System.out.println("WebServer: Processing restart action...");
        gameManager.restartGame();
        // Return the new initial game state
        return getState();
    }
} 