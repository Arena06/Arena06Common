package com.assemblr.arena06.common.data;

import com.assemblr.arena06.common.chat.ChatBroadcaster;
import com.assemblr.arena06.common.packet.Packet;
import com.assemblr.arena06.common.utils.Serialize;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class Bullet extends MovingSprite{
    @Serialize private String owner = "";
    
    public void render(Graphics2D g) {
        g.setColor(Color.red);
        g.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
    }

    @Override
    public List<Packet> onContact(Sprite interactor, int interactorID, List<Integer> spritesPendingRemoveal, ChatBroadcaster chatter) {
        if (interactor instanceof Player) {
            spritesPendingRemoveal.add(interactorID);
            chatter.sendChatBroadcast("~ Player " + ((Player) interactor).getName() + " was killed by " + owner);
        }
        return super.onContact(interactor, interactorID, spritesPendingRemoveal, chatter);
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
    
    
    
}
