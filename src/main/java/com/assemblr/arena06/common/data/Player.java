package com.assemblr.arena06.common.data;

import com.assemblr.arena06.common.chat.ChatBroadcaster;
import com.assemblr.arena06.common.data.map.generators.MapGenerator;
import com.assemblr.arena06.common.data.weapon.Weapon;
import com.assemblr.arena06.common.data.weapon.WeaponInfo;
import com.assemblr.arena06.common.packet.Packet;
import com.assemblr.arena06.common.resource.ResourceResolver;
import com.assemblr.arena06.common.utils.Fonts;
import com.assemblr.arena06.common.utils.serialization.Serialize;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Player extends MovingSprite {
    
    private final boolean self;
    private boolean clientIsCurrent = true;
    private boolean weaponsDataDirty = true;
    @Serialize private List<WeaponInfo> weaponsData;
    private boolean weaponIndexDirty = true;
    @Serialize private int weaponIndex;
    private boolean lifeDirty = true;
    @Serialize private double life = 1;
    private boolean aliveDirty = true;
    @Serialize private boolean alive = false;
    private boolean nameDirty = true;
    @Serialize private String name;
    private boolean directionDirty = true;
    @Serialize private double direction;
    
    public Player() {
        this(false, "Player");
    }
    
    public Player(boolean self, String name) {
        this.self = self;
        this.name = name;
        direction = 0;
        width = height = MapGenerator.TILE_SIZE - 10;
        weaponsData = new ArrayList<WeaponInfo>();
        for (Weapon w : Weapon.values()) {
            weaponsData.add(new WeaponInfo(w));
        }
        setStartingAmmo();
    }
    
    public Color getColor() {
        return new Color(name.hashCode());
    }
    
    public void render(Graphics2D g) {
        if (!alive) {
            return;
        }
        Image sprite = ResourceResolver.getResourceResolver().resolveResource("/player.png");
        AffineTransform  af = new AffineTransform();
        af.rotate(getDirection(), getWidth() / 2, getHeight() / 2);
        af.translate(getWidth() / 2 - sprite.getWidth(null) / 2, getHeight()/ 2 - sprite.getHeight(null)/ 2);
        g.drawImage(sprite, af, null);
        if (!self) {
            Font f = Fonts.FONT_PRIMARY.deriveFont(8f);
            FontMetrics metrics = g.getFontMetrics(f);
            Rectangle2D bounds = metrics.getStringBounds(name, g);
            
            g.setColor(new Color(0x88000000, true));
            g.fillRect((int) (-bounds.getWidth()/2 - 5 + getWidth()/2), (int) (-bounds.getHeight() - 13), (int) bounds.getWidth() + 10, (int) bounds.getHeight() + 8);
            
            g.setColor(Color.WHITE);
            g.setFont(f);
            g.drawString(name, (int) (-bounds.getWidth()/2 + getWidth()/2), -9);
        }
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        nameDirty = true;
        this.name = name;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        setAlive(false);
    }
    
    public void setAlive(boolean alive) {
        aliveDirty = true;
        lifeDirty = true;
        if (this.alive == false && alive) {
            this.setLife(1);
            for (WeaponInfo wi : getWeaponsData()) {
                wi.resetAmoAmount();
            }
        }
        this.alive = alive;
    }

    /**
     * @return the clientIsCurrent
     */
    public boolean isClientIsCurrent() {
        return clientIsCurrent;
    }

    /**
     * @param clientIsCurrent the clientIsCurrent to set
     */
    public void setClientIsCurrent(boolean clientIsCurrent) {
        this.clientIsCurrent = clientIsCurrent;
    }

    /**
     * @return the life
     */
    public double getLife() {
        
        return life;
    }

    /**
     * @param life the life to set
     */
    public void setLife(double life) {
        lifeDirty = true;
        this.life = life;
    }

    /**
     * @return the weapon
     */
    public Weapon getWeapon() {
        return getWeaponsData().get(weaponIndex).getWeapon();
    }

    public void incrementWeaponIndex(int amount) {
        weaponIndexDirty = true;
        weaponIndex += amount;
        weaponIndex += getWeaponsData().size() * 3;
        weaponIndex = weaponIndex % getWeaponsData().size();
    }
    /**
     * @param weapon the weapon to set
     */
    public void setWeapon(Weapon weapon) {
        weaponIndexDirty = true;
        for (int i = 0; i < getWeaponsData().size(); i++) {
            if (getWeaponsData().get(i).getWeapon().equals(weapon)) {
                weaponIndex = i;
                break;
            }
        }
    }

    /**
     * @return the loadedBullets
     */
    public int getLoadedBullets() {
        return getWeaponsData().get(weaponIndex).getLoadedBullets();
    }

    /**
     * @param loadedBullets the loadedBullets to set
     */
    public void setLoadedBullets(int loadedBullets) {
        weaponsDataDirty = true;
        this.getWeaponsData().get(weaponIndex).setLoadedBullets(loadedBullets);
    }

    /**
     * @return the timeCoolingDown
     */
    public double cooldownRemaining() {
        return getWeaponsData().get(weaponIndex).getCooldownRemaining();
    }

    /**
     * @param timeCoolingDown the timeCoolingDown to set
     */
    public void setCooldownRemaining(double timeCoolingDown) {
        weaponsDataDirty = true;
        this.getWeaponsData().get(weaponIndex).setCooldownRemaining(timeCoolingDown);
    }

    /**
     * @return the timeRreloading
     */
    public double getReloadRemaining() {
        return getWeaponsData().get(weaponIndex).getReloadRemaining();
    }

    /**
     * @param timeRreloading the timeRreloading to set
     */
    public void setReloadRemaining(double timeRreloading) {
        weaponsDataDirty = true;
        this.getWeaponsData().get(weaponIndex).setReloadRemaining(timeRreloading);
    }

    /**
     * @return the isReloading
     */
    public boolean isReloading() {
        return getWeaponsData().get(weaponIndex).isReloading();
    }

    /**
     * @param isReloading the isReloading to set
     */
    public void setIsReloading(boolean isReloading) {
        weaponsDataDirty = true;
        this.getWeaponsData().get(weaponIndex).setReloading(isReloading);
    }
    
    public void setStartingAmmo() {
        weaponsDataDirty = true;
        for (WeaponInfo weaponInfo : weaponsData) {
            weaponInfo.setDefaults();
        }
        getWeaponDataModifyable(Weapon.BERETTA_93R).setCartregesReamaining(5);
    }
    
    public WeaponInfo getWeaponData() {
        WeaponInfo value = getWeaponsData().get(weaponIndex);
        return value;
    }
    
    public WeaponInfo getWeaponDataModifyable() {
        
        weaponsDataDirty = true;
        WeaponInfo value = getWeaponsData().get(weaponIndex);
        return value;
    }
    
    public WeaponInfo getWeaponDataModifyable(int weaponIndex) {
        
        weaponsDataDirty = true;
        return getWeaponData(weaponIndex);
    }
    
    
    public WeaponInfo getWeaponDataModifyable(Weapon weapon) {
        weaponsDataDirty = true;
        return getWeaponData(weapon);
    }
    
    public WeaponInfo getWeaponData(int weaponIndex) {
        weaponsDataDirty = true;
        WeaponInfo value = getWeaponsData().get(weaponIndex);
        return value;
    }
    public WeaponInfo getWeaponData(Weapon weapon) {
        weaponsDataDirty = true;
        for (WeaponInfo wi : getWeaponsData()) {
            if (wi.getWeapon() == weapon)
                return wi;
        }
        return null;
    }

    @Override
    public List<Packet> onContact(int selfID, Sprite interactor, int interactorID, List<Integer> dirtySprites, List<Integer> spritesPendingRemoveal, ChatBroadcaster chater) {
        if (interactor instanceof AmmoPickup && isAlive()) {
            weaponsDataDirty = true;
            WeaponInfo wi = getWeaponDataModifyable(((AmmoPickup)interactor).getWeapon());//Update Localy
            wi.setCartregesReamaining(wi.getCartregesReamaining() + ((AmmoPickup)interactor).getAmount());
            spritesPendingRemoveal.add(interactorID);
            dirtySprites.add(selfID);
            //Use primatives to tell client to update
            //setNewCartregesRemainingPendingInsertion(wi.getCartregesReamaining() + ((AmmoPickup)interactor).getAmount());
            for (int i = 0; i < getWeaponsData().size(); i++) {
                if (getWeaponsData().get(i).getWeapon().equals(wi.getWeapon())) {
                    //setWeaponIndexForPendingCartreges(i);
                    break;
                }
            }
            
        }
        return new ArrayList<Packet>();
    }

    

    


    /**
     * @return the weaponsData
     */
    public List<WeaponInfo> getWeaponsData() {
        
        return weaponsData;
    }

    /**
     * @param weaponsData the weaponsData to set
     */
    public void setWeaponsData(List<WeaponInfo> weaponsData) {
        this.weaponsData = weaponsData;
    }

    @Override
    public String toString() {
        return "[Player at " + getPosition().toString() + " with name " +getName() +"]";
    }

    /**
     * @return the direction
     */
    public double getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(double direction) {
        directionDirty = true;
        this.direction = direction;
    }
    
}
