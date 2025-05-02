import React from 'react';

// Define types for props matching GameStateData structure
interface CharacterPosition {
    x: number;
    y: number;
}

interface PirateData extends CharacterPosition {
    type: string;
}

interface MapViewProps {
    width: number;
    height: number;
    player: CharacterPosition;
    pirates: PirateData[];
    treasure: CharacterPosition;
    islands?: CharacterPosition[]; // Receive island positions
    monsters?: CharacterPosition[]; // Add monsters prop
    strategySwitchers?: CharacterPosition[]; // Add strategy switchers prop
}

const CELL_SIZE = 30; // Size of each grid cell in pixels

function MapView({ width, height, player, pirates, treasure, islands = [], monsters = [], strategySwitchers = [] }: MapViewProps) {

    // Calculate grid dimensions for inline style (needed for dynamic size)
    const gridStyle: React.CSSProperties = {
        width: `${width * CELL_SIZE}px`,
        height: `${height * CELL_SIZE}px`,
        // Other styles like border, background, position are now in App.css (.map-grid)
    };

    // Helper to generate style for absolutely positioned items
    const getElementStyle = (x: number, y: number): React.CSSProperties => ({
        left: `${x * CELL_SIZE}px`,
        top: `${y * CELL_SIZE}px`,
        width: `${CELL_SIZE}px`,
        height: `${CELL_SIZE}px`,
        // Base styles like position:absolute, display:flex etc. are in App.css (.map-element)
    });

    // Define a simple style for images within map elements if needed (or rely on CSS)
    // const imageStyle: React.CSSProperties = { ... };

    return (
        <div className="MapView"> {/* Use class for potential specific container styling */}
            <h2>Game Map ({width}x{height})</h2>
            <div className="map-grid" style={gridStyle}> {/* Apply class and dynamic size style */}
                {/* Render Islands */}
                {islands.map((island, index) => (
                     <div
                        key={`island-${index}`}
                        className="map-element island" /* Use classes */
                        style={getElementStyle(island.x, island.y)} /* Apply dynamic position/size */
                        title={`Island (${island.x}, ${island.y})`}
                    >
                         <img src="/island.jpg" alt="Island" /> {/* Removed inline style */}
                     </div>
                 ))}

                {/* Render player */}
                {player.x >= 0 && player.y >= 0 && (
                    <div
                        className="map-element player" /* Use classes */
                        style={getElementStyle(player.x, player.y)} /* Apply dynamic position/size */
                        title={`Columbus (${player.x}, ${player.y})`}
                    >
                        <img src="/ship.png" alt="Columbus Ship" /> {/* Removed inline style */}
                    </div>
                )}

                {/* Render pirates */}
                {pirates.map((pirate, index) => (
                    <div
                        key={`pirate-${index}`}
                        className="map-element pirate" /* Use classes */
                        style={getElementStyle(pirate.x, pirate.y)} /* Apply dynamic position/size */
                        title={`${pirate.type} (${pirate.x}, ${pirate.y})`}
                    >
                         <img src="/pirateShip.png" alt="Pirate Ship" /> {/* Removed inline style */}
                    </div>
                ))}

                {/* Render treasure */}
                {treasure.x >= 0 && treasure.y >= 0 && (
                    <div
                        className="map-element treasure" /* Use classes */
                        style={getElementStyle(treasure.x, treasure.y)} /* Apply dynamic position/size */
                        title={`Treasure (${treasure.x}, ${treasure.y})`}
                    >
                        💰 {/* Emoji remains as content */}
                    </div>
                )}

                {/* Render Monsters */}
                {monsters.map((monster, index) => (
                     <div
                        key={`monster-${index}`}
                        className="map-element monster" /* Use classes */
                        style={getElementStyle(monster.x, monster.y)} /* Apply dynamic position/size */
                        title={`Monster (${monster.x}, ${monster.y})`}
                    >
                         🐙 {/* Emoji remains as content */}
                     </div>
                 ))}

                {/* Render Strategy Switchers */}
                {strategySwitchers.map((switcher, index) => (
                    <div
                        key={`switcher-${index}`}
                        className="map-element strategy-switcher" /* Use classes */
                        style={getElementStyle(switcher.x, switcher.y)} /* Apply dynamic position/size */
                        title={`Strategy Switcher (${switcher.x}, ${switcher.y})`}
                    >
                        🔪 {/* Knife emoji */}
                    </div>
                ))}

                {/* Grid background/cells are handled by .map-grid style */}
            </div>
        </div>
    );
}

export default MapView; 