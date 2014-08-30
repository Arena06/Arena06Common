/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.assemblr.arena06.common.resource;

import java.awt.Image;
import java.io.IOException;

/**
 *
 * @author Henry
 */
public abstract class ResourceResolver {

    /**
     * @param aResolver the resolver to set
     */
    public static void setResolver(ResourceResolver aResolver) {
        resolver = aResolver;

    }

    
    public abstract void unloadResources(ResourceBlock resources);
    public abstract void loadResources(ResourceBlock resources);
    public abstract Image resolveResource(String resourceLocation);

    private static ResourceResolver resolver;

    public static ResourceResolver getResourceResolver() {
        return resolver;
    }
    
}


