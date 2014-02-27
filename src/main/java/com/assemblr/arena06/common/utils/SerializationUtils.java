package com.assemblr.arena06.common.utils;

import com.assemblr.arena06.common.utils.Serialize;
import com.google.common.collect.ImmutableMap;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SerializationUtils {
    
    public static Map<String, Object> serialize(Object o) {
        if (o == null)
            return null;
        
        // handle primitives
        if (o instanceof Integer || o instanceof Long || o instanceof Float || o instanceof Double
         || o instanceof Boolean || o instanceof Character || o instanceof Byte || o instanceof Short) {
            return serializePrimitive(o);
        }
        
        // handle strings
        if (o instanceof String) {
            return serializeString((String) o);
        }
        
        if (o.getClass().isEnum()) {
            return serializeEnum(o);
        }
        
        // handle lists, arrays, maps
        if (o instanceof List) {
            return serializeList((List) o);
        }
        if (o.getClass().isArray()) {
            return serializeArray(o.getClass(), toObjectArray(o));
        }
        if (o instanceof Map) {
            return serializeMap((Map) o);
        }
        
        // check if the object has a custom serializer
        Method serializer = null;
        try {
            serializer = o.getClass().getDeclaredMethod("serialize", new Class[] {});
        } catch (Exception ex) {}
        
        // if so, call it
        if (serializer != null) {
            serializer.setAccessible(true);
            if (serializer.getReturnType() != Map.class)
                throw new RuntimeException("serialize method must return a Map instance");
            try {
                return serializeCustom(o.getClass(), (Map<String, Object>) serializer.invoke(o));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        
        // otherwise, perform default serialization
        return defaultSerialize(o);
    }
    
    public static Map<String, Object> defaultSerialize(Object o) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("T", "L");
        
        Map<String, Object> classesData = new HashMap<String, Object>();
        data.put("data", classesData);
        
        Class<?> clazz = o.getClass();
        data.put("class", clazz.getName());
        
        // write all @Serialize'd fields
        do {
            Map<String, Object> classData = new HashMap<String, Object>();
            classesData.put(clazz.getName(), classData);
            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(Serialize.class)) {
                    try {
                        classData.put(f.getName(), serialize(f.get(o)));
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        } while ((clazz = clazz.getSuperclass()) != Object.class);
        
        return data;
    }
    
    private static Map<String, Object> serializeCustom(Class<?> clazz, Map<String, Object> customData) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("T", "L");
        data.put("class", clazz.getName());
        data.put("data", customData);
        return data;
    }
    public static Map<String, Object> serializeEnum(Object enumIn) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("T", "L");
        data.put("class", enumIn.getClass().getName());
        data.put("data", ((Enum)enumIn).ordinal());
        System.out.println("enum " + enumIn.toString() + " was serialized.");
        return data;
    }
    private static Map<String, Object> serializePrimitive(Object primitive) {
        Map<String, Object> data = new HashMap<String, Object>();
        if (primitive instanceof Integer)
            data.put("T", "I");
        else if (primitive instanceof Long)
            data.put("T", "J");
        else if (primitive instanceof Float)
            data.put("T", "F");
        else if (primitive instanceof Double)
            data.put("T", "D");
        else if (primitive instanceof Boolean)
            data.put("T", "Z");
        else if (primitive instanceof Character)
            data.put("T", "C");
        else if (primitive instanceof Byte)
            data.put("T", "B");
        else if (primitive instanceof Short)
            data.put("T", "S");
        else
            throw new RuntimeException("attempt to serialize " + primitive.getClass().getName() + " as primitive");
        data.put("value", primitive);
        return data;
    }
    
    private static Map<String, Object> serializeString(String string) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("T", "L");
        data.put("class", String.class.getName());
        data.put("data", string);
        return data;
    }
    
    private static Map<String, Object> serializeList(List<Object> list) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("T", "L");
        data.put("class", list.getClass().getName());
        List<Object> objList = new ArrayList<Object>(list.size());
        for (int i = 0; i < list.size(); i++) {
            objList.add(serialize(list.get(i)));
        }
        data.put("data", objList);
        return data;
    }
    
    private static Map<String, Object> serializeArray(Class<?> type, Object[] arr) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("T", type.getComponentType().isPrimitive() ? type.getName() : ("[L"));
        data.put("class", arr.getClass().getComponentType().getName());
        data.put("length", arr.length);
        
        List<Object> list = new ArrayList<Object>(arr.length);
        for (Object o : arr) {
            list.add(serialize(o));
        }
        data.put("data", list);
        
        return data;
    }
    
    private static <K, V> Map<String, Object> serializeMap(Map<K, V> map) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("T", "L");
        data.put("class", map.getClass().getName());
        
        List<List<Object>> pairs = new ArrayList<List<Object>>(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            List<Object> pair = new ArrayList<Object>(2);
            pair.add(serialize(entry.getKey()));
            pair.add(serialize(entry.getValue()));
            pairs.add(pair);
        }
        data.put("data", pairs);
        
        return data;
    }
    
    
    public static Object unserialize(Map<String, Object> data) {
        // handle primitives
        char type = ((String) data.get("T")).charAt(0);
        switch (type) {
        case 'I':
            return (Integer) data.get("value");
        case 'J':
            return (Long) data.get("value");
        case 'F':
            return (Float) data.get("value");
        case 'D':
            return (Double) data.get("value");
        case 'Z':
            return (Boolean) data.get("value");
        case 'C':
            return (Character) data.get("value");
        case 'B':
            return (Byte) data.get("value");
        case 'S':
            return (Short) data.get("value");
        case 'L':
            break;
        case '[':
            return unserializeArray(data);
        default:
            throw new RuntimeException("invalid serialization type '" + type + "'");
        }
        
        Class<?> clazz;
        try {
            clazz = Class.forName((String) data.get("class"));
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        
        if (clazz.equals(String.class)) {
            return data.get("data");
        }
        /*if (clazz.equals(List.class)) {
            
            return data.get("data");
            
        }*/
        
        if (clazz.isEnum()) {
            
            System.out.println("unserialize unum from class: " + clazz.toString());
            return clazz.getEnumConstants()[(Integer) data.get("data")];
        }
        
        Method unserializer = null;
        try {
            unserializer = clazz.getDeclaredMethod("unserialize", new Class[] { Map.class });
        } catch (Exception ex) {}
        
        if (unserializer != null) {
            unserializer.setAccessible(true);
            try {
                return unserializer.invoke(null, data.get("data"));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        
        Object o;
        try {
            o = clazz.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        if (o instanceof List) {
            
            ArrayList<Map<String, Object>> listDataMaps = (ArrayList<Map<String, Object>>) data.get("data");
            List list = ((List)o);
            for (Map<String, Object> map : listDataMaps) {
                list.add(unserialize(map));
            }
            
            return list;
        } else {
            Map<String, Map<String, Object>> classData = (Map<String, Map<String, Object>>) data.get("data");
            for (Map.Entry<String, Map<String, Object>> entry : classData.entrySet()) {
                try {
                    clazz = Class.forName(entry.getKey());
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                for (Map.Entry<String, Object> field : entry.getValue().entrySet()) {
                    Field f;
                    try {
                        f = clazz.getDeclaredField(field.getKey());
                    } catch (NoSuchFieldException ex) {
                        throw new RuntimeException(ex);
                    }
                    Object value = unserialize((Map<String, Object>) field.getValue());
                    f.setAccessible(true);
                    try {
                        f.set(o, value);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
        
        return o;
    }
    
    private static Object unserializeArray(Map<String, Object> data) {
        Object array = null;
        int length = (Integer) data.get("length");
        
        char type = ((String) data.get("T")).charAt(1);
        switch (type) {
        case 'I':
            array = new int[length];
            break;
        case 'J':
            array = new long[length];
            break;
        case 'F':
            array = new float[length];
            break;
        case 'D':
            array = new double[length];
            break;
        case 'Z':
            array = new boolean[length];
            break;
        case 'C':
            array = new char[length];
            break;
        case 'B':
            array = new byte[length];
            break;
        case 'S':
            array = new short[length];
            break;
        case 'L':
            Class<?> clazz;
            try {
                clazz = Class.forName((String) data.get("class"));
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            array = Array.newInstance(clazz, length);
            break;
        default:
            throw new RuntimeException("invalid array serialization type '" + type + "'");
        }
        
        List<Object> content = (List<Object>) data.get("data");
        for (int i = 0; i < content.size(); i++) {
            Array.set(array, i, content.get(i));
        }
        
        return array;
    }
    
    private static Object[] toObjectArray(Object array) {
        int length = Array.getLength(array);
        Object[] ret = new Object[length];
        for(int i = 0; i < length; i++)
            ret[i] = Array.get(array, i);
        return ret;
    }
    
}
