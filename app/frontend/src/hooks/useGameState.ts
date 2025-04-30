import { useState, useEffect, useCallback } from 'react';
import ApiClient from '../services/ApiClient';

// Define the expected structure of the game state from the backend
interface MapData {
    width: number;
    height: number;
    islands?: CharacterPosition[]; // Add islands array (optional)
    // grid?: any[][]; // Add later if needed for terrain
}

interface CharacterPosition {
    x: number;
    y: number;
}

interface PirateData extends CharacterPosition {
    type: string; // e.g., "ChaserPirateShip"
}

interface StatusData {
    isGameOver: boolean;
    message: string;
}

export interface GameStateData {
  map?: MapData;
  player?: CharacterPosition;
  treasure?: CharacterPosition;
  pirates?: PirateData[];
  monsters?: CharacterPosition[]; // Add monsters array (optional)
  islands?: CharacterPosition[]; // Already added
  status?: StatusData;
}

// Custom hook to manage fetching and updating game state
function useGameState() {
  const [gameState, setGameState] = useState<GameStateData | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);

  // Define fetchState using useCallback to prevent unnecessary re-renders
  const fetchState = useCallback(async () => {
    console.log("useGameState: Fetching state...");
    setLoading(true);
    setError(null);
    try {
      const stateData = await ApiClient.getGameState();
      console.log("useGameState: Received state:", stateData);
      setGameState(stateData);
    } catch (err) {
      const error = err as Error;
      setError(error);
      console.error("useGameState: Failed to fetch game state:", error);
      // Optionally set a default/error state
      // setGameState({ status: { isGameOver: true, message: `Error fetching state: ${error.message}` }});
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchState();
    // Initial fetch on mount
    // Polling/WebSocket would go here if needed
  }, [fetchState]); // Dependency array ensures this runs only once on mount

  // Function to explicitly trigger a state refresh after an action
  const refreshGameState = useCallback(() => {
       console.log("useGameState: Refresh triggered");
       fetchState();
  }, [fetchState]); // Include fetchState in dependency array

  // Function to handle sending a move and refreshing state
  const sendMoveAndRefresh = useCallback(async (direction: string) => {
      setLoading(true); // Optional: show loading state during move processing
      setError(null);
      try {
          console.log(`useGameState: Sending move ${direction}...`);
          const updatedState = await ApiClient.sendMoveCommand(direction);
          console.log("useGameState: Received updated state after move:", updatedState);
          setGameState(updatedState); // Update state immediately with response
      } catch (err) {
          const error = err as Error;
          setError(error);
          console.error(`useGameState: Failed to send move command ${direction}:`, error);
          // Optionally trigger a regular fetch to try and recover state
          // fetchState();
      } finally {
          setLoading(false);
      }
  }, []); // Empty dependency array, relies on closure for ApiClient

  // Function to handle sending a restart command and refreshing state
  const restartGameAndRefresh = useCallback(async () => {
    setLoading(true); // Show loading state during restart
    setError(null);
    try {
        console.log(`useGameState: Sending restart command...`);
        const newState = await ApiClient.restartGame();
        console.log("useGameState: Received new state after restart:", newState);
        setGameState(newState); // Update state immediately with the response
    } catch (err) {
        const error = err as Error;
        setError(error);
        console.error(`useGameState: Failed to send restart command:`, error);
        // Optionally trigger a regular fetch to try and recover state
        // fetchState();
    } finally {
        setLoading(false);
    }
}, []); // Empty dependency array, relies on closure for ApiClient

  return { gameState, loading, error, refreshGameState, sendMoveAndRefresh, restartGameAndRefresh };
}

export default useGameState; 