package com.assemblr.arena06.common.data;

import com.assemblr.arena06.common.data.map.generators.MapGenerator;
import com.assemblr.arena06.common.utils.Serialize;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Player extends Sprite {
    
    @Serialize private String name;
    
    public Player() {
        this("Player");
    }
    
    public Player(String name) {
        this.name = name;
        width = height = MapGenerator.TILE_SIZE - 10;
    }
    
    public Color getColor() {
        return new Color(name.hashCode());
    }
    
    public void render(Graphics2D g) {
        g.setColor(getColor());
        g.fill(new Rectangle2D.Double(0, 0, width, height));
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
}
