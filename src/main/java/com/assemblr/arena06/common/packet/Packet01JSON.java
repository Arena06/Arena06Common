package com.assemblr.arena06.common.packet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import de.undercouch.bson4jackson.BsonFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class Packet01JSON extends Packet {
    
    private static final ObjectMapper mapper = new ObjectMapper(new BsonFactory());
    
    public static Packet01JSON decode(byte[] data) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        return new Packet01JSON(mapper.readValue(stream, Map.class));
    }
    
    private final Map<String, Object> data;
    
    public Packet01JSON(Map<String, Object> data) {
        this.data = ImmutableMap.copyOf(data);
    }
    
    @Override
    public Map<String, Object> getData() {
        return data;
    }
    
    @Override
    public byte[] encode() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mapper.writeValue(stream, data);
        return stream.toByteArray();
    }
    
}
