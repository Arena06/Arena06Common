package com.assemblr.arena06.common.data.weapon;

import com.assemblr.arena06.common.data.Bullet;
import com.assemblr.arena06.common.utils.Vector2D;
import java.util.ArrayList;
import java.util.List;

public enum Weapon {
    BERETTA_93R("Pistol", 3, .07, 1.1, true, new BulletFactory() {
        public List<Bullet> getBullets() {
            ArrayList<Bullet> bullets = new ArrayList<Bullet>();
            bullets.add(new Bullet());
            bullets.get(0).setVelocity(new Vector2D(0, 1000, true));
            bullets.get(0).setWidth(4);
            bullets.get(0).setHeight(4);
            bullets.get(0).setDamage(.4);
            return bullets;
        }
    }),
    AK_47("Assault Rifle", 24, .12, 3, true, new BulletFactory() {
        public List<Bullet> getBullets() {
            ArrayList<Bullet> bullets = new ArrayList<Bullet>();
            bullets.add(new Bullet());
            bullets.get(0).setVelocity(new Vector2D(Math.random() * Math.PI / 9 - Math.PI / 18, 1200, true));
            bullets.get(0).setWidth(6);
            bullets.get(0).setHeight(6);
            bullets.get(0).setDamage(.4);
            return bullets;
        }
    }),
    LEE_ENFIELD("Sniper Rifle", 1, .1, 2, false, new BulletFactory() {
        public List<Bullet> getBullets() {
            ArrayList<Bullet> bullets = new ArrayList<Bullet>();
            bullets.add(new Bullet());
            bullets.get(0).setVelocity(new Vector2D(0, 1500, true));
            bullets.get(0).setWidth(4);
            bullets.get(0).setHeight(4);
            bullets.get(0).setDamage(1.1);
            return bullets;
        }
    }),
    SHOTGUN("Shotgun", 3, .07, 2, false, new BulletFactory() {
        public List<Bullet> getBullets() {
            ArrayList<Bullet> bullets = new ArrayList<Bullet>();
            for (int i = 0; i < 20; i++) {
                Bullet b = new Bullet();
                b.setWidth(4);
                b.setHeight(4);
                b.setVelocity(new Vector2D(Math.random() * Math.PI / 9 - Math.PI / 18, 1000, true));
                b.setDamage(.12);
                bullets.add(b);
            }
            return bullets;
        }
    });
    
    public static Weapon getRandomWeapon() {
        System.out.println("random weapon");
        return Weapon.values()[(int)(Math.random()*Weapon.values().length)];
    }
    
    private String name;
    private double reloadTime, fireTime;
    private int magSize;
    private boolean isFullAuto;
    private BulletFactory bulletFactory;
    Weapon(String weaponName, int magazineSize, double fireTime, double reloadTime, boolean isFullAuto, BulletFactory bulletFactory) {
        this.magSize = magazineSize;
        this.fireTime = fireTime;
        this.reloadTime = reloadTime;
        this.bulletFactory = bulletFactory;
        this.isFullAuto = isFullAuto;
        this.name = weaponName;
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

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    public int getIndex() {
        for (int i = 0; i < Weapon.values().length; i++) {
            if (Weapon.values()[i].equals(this)) {
                return i;
            }
        }
        return -1;
    }
}
