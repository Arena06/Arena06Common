/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.assemblr.arena06.common.data;

import com.assemblr.arena06.common.data.weapon.Weapon;
import com.assemblr.arena06.common.utils.Serialize;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Henry
 */
public class AmmoPickup extends Sprite {
    @Serialize private int weaponIndex = 0;
    private int amount = 5;
    public Weapon getWeapon() {
        return Weapon.values()[weaponIndex];
    }
    
    public void setWeapon(Weapon w) {
        weaponIndex = w.getIndex();
    }

    public void render(Graphics2D g) {
        g.setColor(Color.red);
        g.fillRect(0, 0, 20, 20);
        g.drawString(getWeapon().getName(), 0, 0);
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
}
