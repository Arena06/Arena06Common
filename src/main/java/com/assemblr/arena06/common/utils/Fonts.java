package com.assemblr.arena06.common.utils;

import java.awt.Font;


public class Fonts {
    
    public static final Font FONT_PRIMARY;
    static {
        Font f = null;
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, Fonts.class.getResourceAsStream("/minecraft.ttf"));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        FONT_PRIMARY = f;
    }
    
}
