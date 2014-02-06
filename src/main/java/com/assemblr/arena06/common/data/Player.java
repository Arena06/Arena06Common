package com.assemblr.arena06.common.data;

import com.assemblr.arena06.common.chat.ChatBroadcaster;
import com.assemblr.arena06.common.data.map.generators.MapGenerator;
import com.assemblr.arena06.common.data.weapon.Weapon;
import com.assemblr.arena06.common.data.weapon.WeaponInfo;
import com.assemblr.arena06.common.packet.Packet;
import com.assemblr.arena06.common.utils.Fonts;
import com.assemblr.arena06.common.utils.Serialize;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Player extends MovingSprite {
    
    private final boolean self;
    private boolean clientIsCurrent = true;
    @Serialize private List<WeaponInfo> weaponsData;
    private int weaponIndex;
    @Serialize private double life = 1;
    @Serialize private boolean alive = false;
    @Serialize private String name;
    //@Serialize private int newCartregesRemainingPendingInsertion = -1;
    //@Serialize private int weaponIndexForPendingCartreges = -1;
    
    public Player() {
        this(false, "Player");
    }
    
    public Player(boolean self, String name) {
        this.self = self;
        this.name = name;
        width = height = MapGenerator.TILE_SIZE - 10;
        weaponsData = new ArrayList<WeaponInfo>();
        for (Weapon w : Weapon.values()) {
            weaponsData.add(new WeaponInfo(w));
        }
    }
    
    public Color getColor() {
        return new Color(name.hashCode());
    }
    
    public void render(Graphics2D g) {
        if (!alive) {
            return;
        }
        g.setColor(getColor());
        g.fill(new Rectangle2D.Double(0, 0, width, height));
        
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
        this.name = name;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        this.alive = false;
    }
    
    public void setAlive(boolean alive) {
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
        this.life = life;
    }

    /**
     * @return the weapon
     */
    public Weapon getWeapon() {
        return getWeaponsData().get(weaponIndex).getWeapon();
    }

    public void incrementWeaponIndex(int amount) {
        weaponIndex += amount;
        weaponIndex += getWeaponsData().size() * 3;
        weaponIndex = weaponIndex % getWeaponsData().size();
    }
    /**
     * @param weapon the weapon to set
     */
    public void setWeapon(Weapon weapon) {
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
        this.getWeaponsData().get(weaponIndex).setReloading(isReloading);
    }
    
    public void fillMagazine() {
        this.getWeaponsData().get(weaponIndex).setLoadedBullets(getWeaponsData().get(weaponIndex).getWeapon().getMagSize());
    }
    public WeaponInfo getWeaponData() {
        WeaponInfo value = getWeaponsData().get(weaponIndex);
        return value;
    }
    public WeaponInfo getWeaponData(int weaponIndex) {
        WeaponInfo value = getWeaponsData().get(weaponIndex);
        return value;
    }
    public WeaponInfo getWeaponData(Weapon weapon) {
        for (WeaponInfo wi : getWeaponsData()) {
            if (wi.getWeapon() == weapon)
                return wi;
        }
        return null;
    }

    @Override
    public List<Packet> onContact(int selfID, Sprite interactor, int interactorID, List<Integer> dirtySprites, List<Integer> spritesPendingRemoveal, ChatBroadcaster chater) {
        if (interactor instanceof AmoPickup && isAlive()) {
            WeaponInfo wi = getWeaponData(((AmoPickup)interactor).getWeapon());//Update Localy
            wi.setCartregesReamaining(wi.getCartregesReamaining() + ((AmoPickup)interactor).getAmount());
            spritesPendingRemoveal.add(interactorID);
            
            //Use primatives to tell client to update
            setNewCartregesRemainingPendingInsertion(wi.getCartregesReamaining() + ((AmoPickup)interactor).getAmount());
            for (int i = 0; i < getWeaponsData().size(); i++) {
                if (getWeaponsData().get(i).getWeapon().equals(wi.getWeapon())) {
                    setWeaponIndexForPendingCartreges(i);
                    break;
                }
            }
            
        }
        return new ArrayList<Packet>();
    }

    /**
     * @return the newCartregesRemainingPendingInsertion
     */
    public int getNewCartregesRemainingPendingInsertion() {
        //return newCartregesRemainingPendingInsertion;
        return -1;
    }

    /**
     * @param newCartregesRemainingPendingInsertion the newCartregesRemainingPendingInsertion to set
     */
    public void setNewCartregesRemainingPendingInsertion(int newCartregesRemainingPendingInsertion) {
        //this.newCartregesRemainingPendingInsertion = newCartregesRemainingPendingInsertion;
    }

    /**
     * @return the weaponIndexForPendingCartreges
     */
    public int getWeaponIndexForPendingCartreges() {
        //return weaponIndexForPendingCartreges;
        return -1;
    }

    /**
     * @param weaponIndexForPendingCartreges the weaponIndexForPendingCartreges to set
     */
    public void setWeaponIndexForPendingCartreges(int weaponIndexForPendingCartreges) {
       // this.weaponIndexForPendingCartreges = weaponIndexForPendingCartreges;
    }

    /**
     * @return the weaponsData
     */
    public List<WeaponInfo> getWeaponsData() {
        for (int i = 0; i < weaponsData.size(); i++) {
        }
        
        return weaponsData;
    }

    /**
     * @param weaponsData the weaponsData to set
     */
    public void setWeaponsData(List<WeaponInfo> weaponsData) {
        this.weaponsData = weaponsData;
    }
    
    
}
