package com.assemblr.arena06.common.data.weapon;

import com.assemblr.arena06.common.utils.Serialize;

public class WeaponInfo {
    
    @Serialize private int cartregesReamaining = 0;
    @Serialize private Weapon weapon;
    @Serialize private int loadedBullets;
    @Serialize private double reloadRemaining;
    @Serialize private boolean reloading = false;
    @Serialize private double cooldownRemaining;
    @Serialize private boolean outOfAmo = true;
    /*
    Do not use, serialization system only!!!
    */
    public WeaponInfo() {
        weapon = Weapon.AK_47;
        setDefaults();
    }
    public WeaponInfo(Weapon w) {
        weapon = w;
        setDefaults();
    }

    
    public void setDefaults() {
        cartregesReamaining = 0;
        loadedBullets = 0;
        outOfAmo = true;
        reloading = false;
        reloadRemaining = 0;
        cooldownRemaining = 0;
    }
    /**
     * @return the weapon
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * @return the loadedBullets
     */
    public int getLoadedBullets() {
        return loadedBullets;
    }

    /**
     * @param loadedBullets the loadedBullets to set
     */
    public void setLoadedBullets(int loadedBullets) {
        this.loadedBullets = loadedBullets;
    }

    /**
     * @return the reloadRemaining
     */
    public double getReloadRemaining() {
        return reloadRemaining;
    }

    /**
     * @param reloadRemaining the reloadRemaining to set
     */
    public void setReloadRemaining(double reloadRemaining) {
        this.reloadRemaining = reloadRemaining;
    }

    /**
     * @return the cooldownRemaining
     */
    public double getCooldownRemaining() {
        return cooldownRemaining;
    }

    /**
     * @param cooldownRemaining the cooldownRemaining to set
     */
    public void setCooldownRemaining(double cooldownRemaining) {
        this.cooldownRemaining = cooldownRemaining;
    }

    /**
     * @return the reloading
     */
    public boolean isReloading() {
        return reloading;
    }

    /**
     * @param reloading the reloading to set
     */
    public void setReloading(boolean reloading) {
        this.reloading = reloading;
    }
    
    public void reload() {
        if (getCartregesReamaining() > 0) {
            this.setReloading(true);
            this.reloadRemaining = weapon.getReloadTime();
            setCartregesReamaining(getCartregesReamaining() - 1);
        } else {
            outOfAmo = true;
        }
    }

    /**
     * @return the cartregesReamaining
     */
    public int getCartregesReamaining() {
        return cartregesReamaining;
    }

    /**
     * @param cartregesReamaining the cartregesReamaining to set
     */
    public void setCartregesReamaining(int cartregesReamaining) {
        this.cartregesReamaining = cartregesReamaining;
        if (isOutOfAmo() && cartregesReamaining > 0) {
            setOutOfAmo(false);
            reload();
        }
    }

    /**
     * @return the outOfAmo
     */
    public boolean isOutOfAmo() {
        return outOfAmo;
    }

    /**
     * @param outOfAmo the outOfAmo to set
     */
    public void setOutOfAmo(boolean outOfAmo) {
        this.outOfAmo = outOfAmo;
    }
    
    public void resetAmoAmount() {
        this.cartregesReamaining = 5;
    }

    @Override
    public String toString() {
        return "{WeaponInfo: weapon: " + weapon.getName() + " with " + cartregesReamaining + " cartreges reamaining}";
                
    }
    
    
}
