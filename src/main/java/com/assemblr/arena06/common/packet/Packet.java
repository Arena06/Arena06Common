package com.assemblr.arena06.common.packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import java.io.IOException;
import java.util.Map;

public abstract class Packet {
    
    public static final BiMap<Byte, Class<? extends Packet>> packetIdMap = ImmutableBiMap.<Byte, Class<? extends Packet>>builder()
            .put((byte) 0x01, Packet01JSON.class)
            .build();
    
    public abstract Map<String, Object> getData();
    public abstract byte[] encode() throws IOException;
    
}
