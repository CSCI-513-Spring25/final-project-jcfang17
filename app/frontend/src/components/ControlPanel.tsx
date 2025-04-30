import React from 'react';

interface ControlPanelProps {
    onMove: (direction: string) => void; // Function to call when a move button is clicked
    isDisabled: boolean; // Whether controls should be disabled
    // statusMessage?: string; // Removed status message, as it's handled in App.tsx now
}

function ControlPanel({ onMove, isDisabled }: ControlPanelProps) {
  // TODO: Get game state (status message, etc.) from props or context
  // TODO: Add buttons for player actions (e.g., move directions)
  // TODO: Call API functions when buttons are clicked

  const handleMoveClick = (direction: string) => {
      if (!isDisabled) {
          console.log(`ControlPanel: Requesting move ${direction}`);
          onMove(direction); // Call the function passed from App.tsx
      }
  };

  return (
    <div className="ControlPanel">
      <h2>Controls</h2>
      <div className="controls-grid">
          <button className="up-button" onClick={() => handleMoveClick('UP')} disabled={isDisabled}>↑</button>
          <button className="left-button" onClick={() => handleMoveClick('LEFT')} disabled={isDisabled}>←</button>
          <button className="down-button" onClick={() => handleMoveClick('DOWN')} disabled={isDisabled}>↓</button>
          <button className="right-button" onClick={() => handleMoveClick('RIGHT')} disabled={isDisabled}>→</button>
      </div>
      {isDisabled && <p className="disabled-text">(Controls Disabled)</p>}
    </div>
  );
}

export default ControlPanel; 