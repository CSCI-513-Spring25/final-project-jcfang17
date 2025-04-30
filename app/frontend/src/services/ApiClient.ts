import axios from 'axios';

// Define the base URL for the backend API
// TODO: Configure this properly, maybe via environment variables
const API_BASE_URL = 'http://localhost:8080'; // Assuming backend runs on port 8080

// Create an axios instance
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Define API functions

/**
 * Fetches the current game state from the backend.
 */
export const getGameState = async () => {
  try {
    console.log('ApiClient: Fetching game state...');
    // TODO: Update endpoint path if different in GameController
    const response = await apiClient.get('/state');
    console.log('ApiClient: Received game state:', response.data);
    return response.data; // Assuming backend returns GameState object
  } catch (error) {
    console.error('Error fetching game state:', error);
    // Handle error appropriately (e.g., show message to user)
    throw error;
  }
};

/**
 * Sends a player move command to the backend.
 * @param direction - The direction to move ("UP", "DOWN", "LEFT", "RIGHT")
 */
export const sendMoveCommand = async (direction: string) => {
  try {
    console.log(`ApiClient: Sending move command: ${direction}`);
    // TODO: Update endpoint path and request body if different in GameController
    const response = await apiClient.post('/action/move', { direction });
    console.log('ApiClient: Move response:', response.data);
    return response.data; // Might return updated state or confirmation
  } catch (error) {
    console.error('Error sending move command:', error);
    throw error;
  }
};

/**
 * Sends a command to restart the game on the backend.
 */
export const restartGame = async () => {
  try {
    console.log('ApiClient: Sending restart command...');
    // Send a POST request to the restart endpoint
    const response = await apiClient.post('/action/restart');
    console.log('ApiClient: Restart response:', response.data);
    return response.data; // Should return the new initial game state
  } catch (error) {
    console.error('Error sending restart command:', error);
    throw error;
  }
};

// TODO: Add functions for other API calls (start game, get map details, etc.)

// Export the functions as part of an object or individually
const ApiClient = {
  getGameState,
  sendMoveCommand,
  restartGame,
  // Add other functions here
};

export default ApiClient; 