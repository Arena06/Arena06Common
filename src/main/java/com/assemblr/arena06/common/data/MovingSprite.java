package com.assemblr.arena06.common.data;

import com.assemblr.arena06.common.utils.serialization.Serialize;
import com.assemblr.arena06.common.utils.Vector2D;

public abstract class MovingSprite extends UpdateableSprite {
    
    private boolean xVelocityDirty = true;
    @Serialize private double xVelocity;
    private boolean yVelocityDirty = true;
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
        xVelocityDirty = true;
        this.xVelocity = velocity.x;
        yVelocityDirty = true;
        this.yVelocity = velocity.y;
    }
    
    public void update(double delta) {
        this.setX(this.getX() + delta * xVelocity);
        this.setY(this.getY() + delta * yVelocity);
    }
    
}
