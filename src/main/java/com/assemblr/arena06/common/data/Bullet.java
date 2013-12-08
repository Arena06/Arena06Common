package com.assemblr.arena06.common.data;

import com.assemblr.arena06.common.chat.ChatBroadcaster;
import com.assemblr.arena06.common.packet.Packet;
import com.assemblr.arena06.common.utils.Serialize;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Bullet extends MovingSprite{
    @Serialize private String owner = "";
    @Serialize private double damage;
    public void render(Graphics2D g) {
        g.setColor(Color.red);
        g.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
    }

    @Override
    public List<Packet> onContact(int selfID, Sprite interactor, int interactorID, List<Integer> spritesPendingRemoveal, ChatBroadcaster chatter) {
        if (interactor instanceof Player) {
            if (!((Player) interactor).isAlive())
                return new ArrayList<Packet>();
            Player player = (Player) interactor;
            player.setLife(player.getLife() - getDamage());
            if (player.getLife() < 0) {
                chatter.sendChatBroadcast("~ Player " + ((Player) interactor).getName() + " was killed by " + owner);
                spritesPendingRemoveal.add(interactorID);
            }
            spritesPendingRemoveal.add(selfID);
            
        }
        return new ArrayList<Packet>();
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the damage
     */
    public double getDamage() {
        return damage;
    }

    /**
     * @param damage the damage to set
     */
    public void setDamage(double damage) {
        this.damage = damage;
    }
    
    
    
}
