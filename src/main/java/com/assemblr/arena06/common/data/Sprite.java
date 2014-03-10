package com.assemblr.arena06.common.data;

import com.assemblr.arena06.common.chat.ChatBroadcaster;
import com.assemblr.arena06.common.data.map.generators.MapGenerator;
import com.assemblr.arena06.common.packet.Packet;
import com.assemblr.arena06.common.utils.Dimension2D;
import com.assemblr.arena06.common.utils.SerializationUtils;
import com.assemblr.arena06.common.utils.Serialize;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Sprite implements Renderable {
    private boolean xDirty = true;
    @Serialize private double x;
    private boolean yDirty = true;
    @Serialize private double y;
    private boolean widthDirty = true;
    @Serialize protected double width;
    private boolean heightDirty = true;
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
                        if (!f.getType().isPrimitive() || f.getType().isEnum()) {
                            f.set(this, SerializationUtils.unserialize(((Map<String, Object>)entry.getValue())));
                        } else {
                            f.set(this, entry.getValue());
                        }
                        try {
                            Field dirtyIndicator = clazz.getDeclaredField(f.getName() + "Dirty");
                            dirtyIndicator.setAccessible(true);
                            dirtyIndicator.set(this, false);
                        } catch (NoSuchFieldException ex) {}
                    }
                } catch (NoSuchFieldException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } while (Sprite.class.isAssignableFrom(clazz = clazz.getSuperclass()));
        if (this instanceof Player) {
            //System.out.println("Sprite line 57: " + ((Player)this).getWeaponData());
        }
    }
    public Map<String, Object> serializeState() {
        return serializeState(false);
    }
    public Map<String, Object> serializeState(boolean sendAll) {
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
                        try {
                            Field dirtyIndicator = clazz.getDeclaredField(f.getName() + "Dirty");
                            dirtyIndicator.setAccessible(true);
                           if (sendAll || dirtyIndicator.getBoolean(this)) {
                                if (!f.getType().isPrimitive() || f.getType().isEnum()) {
                                  
                                    classState.put(f.getName(), SerializationUtils.serialize(f.get(this)));
                                } else {
                                    classState.put(f.getName(), f.get(this));
                                }
                            } else {
                                
                            }
                        } catch (Exception ex) {
                            if (!f.getType().isPrimitive() || f.getType().isEnum()) {
                                classState.put(f.getName(), SerializationUtils.serialize(f.get(this)));
                            } else {
                                classState.put(f.getName(), f.get(this));
                            }
                        }
                        
                        
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
        xDirty = true;
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        yDirty = true;
        this.y = y;
    }
    
    public int getTileX() {
        return (int) (x / MapGenerator.TILE_SIZE);
    }
    
    public int getTileXRightSide() {
        return (int) ((x + getWidth())/ MapGenerator.TILE_SIZE);
    }
    
    public void setTileX(int x) {
        xDirty = true;
        this.setX(x * MapGenerator.TILE_SIZE);
    }
    
    public int getTileY() {
        return (int) (y / MapGenerator.TILE_SIZE);
    }
    
    public int getTileYBottom() {
        return (int) ((y + getHeight())/ MapGenerator.TILE_SIZE);
    }
    
    public void setTileY(int y) {
        yDirty = true;
        this.setY(y * MapGenerator.TILE_SIZE);
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
        heightDirty = true;
        this.height = height;
    }
    
    public Point2D.Double getPosition() {
        return new Point2D.Double(x, y);
    }
    
    public void setPosition(Point2D position) {
        xDirty = true;
        yDirty = true;
        setX(position.getX());
        setY(position.getY());
    }
    
    public Point2D.Double getCenter() {
        return new Point2D.Double(x + width/2.0, y + height/2.0);
    }
    
    public void setCenter(Point2D position) {
        xDirty = true;
        yDirty = true;
        setX(position.getX() - width/2.0);
        setY(position.getY() - height/2.0);
    }
    
    public Dimension2D.Double getSize() {
        return new Dimension2D.Double(width, height);
    }
    
    public void setSize(java.awt.geom.Dimension2D size) {
        heightDirty = true;
        widthDirty = true;
        width = size.getWidth();
        height = size.getHeight();
    }
    
    public Rectangle2D.Double getBounds() {
        return new Rectangle2D.Double(x, y, width, height);
    }
    
    public void setBounds(Rectangle2D bounds) {
        xDirty = true;
        yDirty = true;
        heightDirty = true;
        widthDirty = true;
        setX(bounds.getX());
        setY(bounds.getY());
        width = bounds.getWidth();
        height = bounds.getHeight();
    }
    
    public List<Packet> onContact(int selfID, Sprite interactor, int interactorID, List<Integer> dirtySprites, List<Integer> spritesPendingRemoveal, ChatBroadcaster chater) {
        return new ArrayList<Packet>();
    }
    
}
