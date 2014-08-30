/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.assemblr.arena06.common.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Henry
 */
public enum ResourceBlock {
    SPRITES("/player.png", "/Pistolamo.png", "/Assault Rifleamo.png", "/Sniper Rifleamo.png", "/Shotgunamo.png");
    
    private List<String> resourceNames;
    private ResourceBlock(String ... resourceNames) {
        this.resourceNames = Arrays.asList(resourceNames);;
    }
    public List<String> getResources() {
        return resourceNames;
    }
    
    
}
