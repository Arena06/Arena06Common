
package com.assemblr.arena06.common.data;

/*
*This class is for sprites that are somewhat static and can be predicted by the client and can be partially updated by the client without contacting the server.
*/
public abstract class UpdateableSprite extends Sprite {
    public abstract void update(double delta);
}
