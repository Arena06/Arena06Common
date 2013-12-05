package com.assemblr.arena06.common.data;

import com.assemblr.arena06.common.utils.Serialize;
import com.assemblr.arena06.common.utils.Vector2D;

public abstract class MovingSprite extends UpdateableSprite {
    
    @Serialize private double xVelocity;
    @Serialize private double yVelocity;

    /**
     * @return the velocity
     */
    public Vector2D getVelocity() {
        return new Vector2D(xVelocity, yVelocity);
    }

    /**
     * @param velocity the velocity to set
     */
    public void setVelocity(Vector2D velocity) {
        this.xVelocity = velocity.x;
         this.yVelocity = velocity.y;
    }
    
    public void update(double delta) {
        this.x += delta * xVelocity;
        this.y += delta * yVelocity;
    }
    
}
