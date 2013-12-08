package com.assemblr.arena06.common.data.weapon;

import com.assemblr.arena06.common.data.Bullet;
import com.assemblr.arena06.common.utils.Vector2D;
import java.util.ArrayList;
import java.util.List;

public enum Weapon {
    BERETTA_93R(3, .07, 1.1, true, new BulletFactory() {
        public List<Bullet> getBullets() {
            ArrayList<Bullet> bullets = new ArrayList<Bullet>();
            bullets.add(new Bullet());
            bullets.get(0).setVelocity(new Vector2D(0, 1000, true));
            bullets.get(0).setWidth(4);
            bullets.get(0).setHeight(4);
            bullets.get(0).setDamage(.4);
            return bullets;
        }
    });
    private double reloadTime, fireTime;
    int magSize;
    private boolean isFullAuto;
    private BulletFactory bulletFactory;
    Weapon(int magazineSize, double fireTime, double reloadTime, boolean isFullAuto, BulletFactory bulletFactory) {
        this.magSize = magazineSize;
        this.fireTime = fireTime;
        this.reloadTime = reloadTime;
        this.bulletFactory = bulletFactory;
        this.isFullAuto = isFullAuto;
    }

    /**
     * @return the magSize
     */
    public int getMagSize() {
        return magSize;
    }

    /**
     * @return the reloadTime
     */
    public double getReloadTime() {
        return reloadTime;
    }

    /**
     * @return the fireTime
     */
    public double getFireTime() {
        return fireTime;
    }

    /**
     * @return the isFullAuto
     */
    public boolean isFullAuto() {
        return isFullAuto;
    }

    /**
     * @return the bulletFactory
     */
    public BulletFactory getBulletFactory() {
        return bulletFactory;
    }
}
