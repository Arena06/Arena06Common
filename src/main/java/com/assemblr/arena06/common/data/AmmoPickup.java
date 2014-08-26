/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.assemblr.arena06.common.data;

import com.assemblr.arena06.common.chat.ChatBroadcaster;
import com.assemblr.arena06.common.data.weapon.Weapon;
import com.assemblr.arena06.common.packet.Packet;
import com.assemblr.arena06.common.utils.Fonts;
import com.assemblr.arena06.common.utils.serialization.Serialize;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Henry
 */
public class AmmoPickup extends Sprite {
    @Serialize private int weaponIndex = 0;
    @Serialize private int amount = 5;
    public Weapon getWeapon() {
        return Weapon.values()[weaponIndex];
    }
    public AmmoPickup() {
        this.setWidth(20);
        this.setHeight(20);
    }
    
    public AmmoPickup(Weapon weapon, int amount) {
        this.setWidth(20);
        this.setHeight(20);
        this.amount = amount;
        setWeapon(weapon);
    }
    public void setWeapon(Weapon w) {
        weaponIndex = w.getIndex();
    }

    public void render(Graphics2D g) {
        g.setColor(Color.red);
        g.drawRect(0, 0, (int) getWidth(), (int) getHeight());
        g.setFont(Fonts.FONT_PRIMARY.deriveFont(12F));
        g.drawString(getWeapon().getName(), 0, 10);
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    
    

    @Override
    public String toString() {
        return "[Ammo Pickup for " + getWeapon( ).getName() + " with " + amount + " cartreges at position " + getPosition() + "]";
    }
    
    
    
}
