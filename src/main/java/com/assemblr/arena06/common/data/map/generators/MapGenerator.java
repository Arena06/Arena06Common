package com.assemblr.arena06.common.data.map.generators;

import com.assemblr.arena06.common.data.map.TileType;


public interface MapGenerator {
    
    public static final double TILE_SIZE = 40;
    
    public TileType[][] generateMap(long seed);
    
}
