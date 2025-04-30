import React, { useEffect, useCallback } from 'react';
import './App.css'; // Styles moved here
import MapView from './components/MapView';
import ControlPanel from './components/ControlPanel';
import useGameState from './hooks/useGameState';

function App() {
  const { gameState, loading, error, sendMoveAndRefresh, restartGameAndRefresh } = useGameState();

  // Extract data safely, providing defaults
  const mapData = gameState?.map ?? { width: 0, height: 0 };
  const playerPos = gameState?.player ?? { x: -1, y: -1 };
  const treasurePos = gameState?.treasure ?? { x: -1, y: -1 };
  const pirates = gameState?.pirates ?? [];
  const islands = gameState?.map?.islands ?? [];
  const monsters = gameState?.monsters ?? [];
  const status = gameState?.status ?? { isGameOver: false, message: "Status unknown" };

  // Determine status class based on game state
  const statusClass = status.isGameOver
    ? (status.message.toLowerCase().includes("found the treasure") ? 'game-won' : 'game-over')
    : ''; // No specific class if game is ongoing

  // Define key handler using useCallback to prevent re-creation on every render
  const handleKeyDown = useCallback((event: KeyboardEvent) => {
      // Only process keys if game is active (not loading, not game over)
      if (loading || status.isGameOver) {
          return;
      }

      let direction: string | null = null;
      switch (event.key) {
          case 'ArrowUp':
          case 'w': // Add WASD support
              direction = 'UP';
              break;
          case 'ArrowDown':
          case 's':
              direction = 'DOWN';
              break;
          case 'ArrowLeft':
          case 'a':
              direction = 'LEFT';
              break;
          case 'ArrowRight':
          case 'd':
              direction = 'RIGHT';
              break;
          default:
              return; // Ignore other keys
      }

      event.preventDefault(); // Prevent default arrow key behavior (scrolling)
      console.log(`App: Key pressed - ${event.key}, sending move: ${direction}`);
      sendMoveAndRefresh(direction);

  }, [loading, status.isGameOver, sendMoveAndRefresh]); // Dependencies for useCallback

  // Add and remove the event listener
  useEffect(() => {
      console.log("App: Adding keydown listener");
      window.addEventListener('keydown', handleKeyDown);

      // Cleanup function to remove the listener when the component unmounts
      return () => {
          console.log("App: Removing keydown listener");
          window.removeEventListener('keydown', handleKeyDown);
      };
  }, [handleKeyDown]); // Re-run effect if handleKeyDown changes

  if (loading && !gameState) {
      return <div className="App"><h1>Loading Game...</h1></div>; // App class applied
  }

  if (error) {
      return <div className="App"><h1>Error loading game: {error.message}</h1></div>; // App class applied
  }

  if (!gameState) {
      // This case should ideally not happen if loading and error are handled
      return <div className="App"><h1>No game state available.</h1></div>; // App class applied
  }

  return (
    <div className="App"> {/* Base App class for layout */}
      <h1>Columbus Treasure Hunt</h1>
      {/* Apply dynamic status class */}
      <p className={`status-message ${statusClass}`}>
          {status.message}
      </p>
      <div className="game-container"> {/* Container for map and controls */}
        <MapView
            width={mapData.width}
            height={mapData.height}
            player={playerPos}
            pirates={pirates}
            treasure={treasurePos}
            islands={islands}
            monsters={monsters}
         />
        <ControlPanel
            onMove={sendMoveAndRefresh}
            isDisabled={status.isGameOver || loading} // Disable controls when game over or loading
        />
      </div>
      {/* Add Restart Button when game is over */}
      {status.isGameOver && (
        <button
          className="restart-button" /* Apply class for styling */
          onClick={restartGameAndRefresh}
          disabled={loading} // Disable while processing restart
        >
          Restart Game
        </button>
      )}
    </div>
  );
}

export default App; 