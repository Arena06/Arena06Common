package com.assemblr.arena06.common.data;

import com.assemblr.arena06.common.chat.ChatBroadcaster;
import com.assemblr.arena06.common.data.map.generators.MapGenerator;
import com.assemblr.arena06.common.packet.Packet;
import com.assemblr.arena06.common.utils.Dimension2D;
import com.assemblr.arena06.common.utils.Serialize;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Sprite implements Renderable {
    
    @Serialize protected double x;
    @Serialize protected double y;
    @Serialize protected double width;
    @Serialize protected double height;
    
    public void updateState(Map<String, Object> state) {
        Class<?> clazz = this.getClass();
        do {
            Map<String, Object> classState = (Map<String, Object>) state.get(clazz.getName());
            if (classState == null) continue;
            for (Map.Entry<String, Object> entry : classState.entrySet()) {
                try {
                    Field f = clazz.getDeclaredField(entry.getKey());
                    if (!f.isAccessible())
                        f.setAccessible(true);
                    if (f.isAnnotationPresent(Serialize.class)) {
                        f.set(this, entry.getValue());
                    }
                } catch (NoSuchFieldException ex) {
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } while (Sprite.class.isAssignableFrom(clazz = clazz.getSuperclass()));
    }
    
    public Map<String, Object> serializeState() {
        Map<String, Object> state = new HashMap<String, Object>();
        Class<?> clazz = this.getClass();
        do {
            Map<String, Object> classState = new HashMap<String, Object>();
            state.put(clazz.getName(), classState);
            for (Field f : clazz.getDeclaredFields()) {
                if (!f.isAccessible())
                        f.setAccessible(true);
                if (f.isAnnotationPresent(Serialize.class)) {
                    try {
                        classState.put(f.getName(), f.get(this));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } while (Sprite.class.isAssignableFrom(clazz = clazz.getSuperclass()));
        return state;
    }
    
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public int getTileX() {
        return (int) (x / MapGenerator.TILE_SIZE);
    }
    
    public int getTileXRightSide() {
        return (int) ((x + getWidth())/ MapGenerator.TILE_SIZE);
    }
    
    public void setTileX(int x) {
        this.x = x * MapGenerator.TILE_SIZE;
    }
    
    public int getTileY() {
        return (int) (y / MapGenerator.TILE_SIZE);
    }
    
    public int getTileYBottom() {
        return (int) ((y + getHeight())/ MapGenerator.TILE_SIZE);
    }
    
    public void setTileY(int y) {
        this.y = y * MapGenerator.TILE_SIZE;
    }
    
    public double getWidth() {
        return width;
    }
    
    public void setWidth(double width) {
        this.width = width;
    }
    
    public double getHeight() {
        return height;
    }
    
    public void setHeight(double height) {
        this.height = height;
    }
    
    public Point2D.Double getPosition() {
        return new Point2D.Double(x, y);
    }
    
    public void setPosition(Point2D position) {
        x = position.getX();
        y = position.getY();
    }
    
    public Point2D.Double getCenter() {
        return new Point2D.Double(x + width/2.0, y + height/2.0);
    }
    
    public void setCenter(Point2D position) {
        x = position.getX() - width/2.0;
        y = position.getY() - height/2.0;
    }
    
    public Dimension2D.Double getSize() {
        return new Dimension2D.Double(width, height);
    }
    
    public void setSize(java.awt.geom.Dimension2D size) {
        width = size.getWidth();
        height = size.getHeight();
    }
    
    public Rectangle2D.Double getBounds() {
        return new Rectangle2D.Double(x, y, width, height);
    }
    
    public void setBounds(Rectangle2D bounds) {
        x = bounds.getX();
        y = bounds.getY();
        width = bounds.getWidth();
        height = bounds.getHeight();
    }
    
    public List<Packet> onContact(int selfID, Sprite interactor, int interactorID, List<Integer> spritesPendingRemoveal, ChatBroadcaster chater) {
        return new ArrayList<Packet>();
    }
    
}
