package com.assemblr.arena06.common.test;

import com.assemblr.arena06.common.utils.SerializationUtils;
import com.google.common.collect.ImmutableMap;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SerializationTest {
    
    private void assertSerializedUnserialized(Object o) {
        Map<String, Object> s = SerializationUtils.serialize(o);
        Object u = SerializationUtils.unserialize(s);
        assertEquals(o, u);
    }
    
    private void print(Object o) {
        print(o, true);
    }
    
    private void print(Object o, boolean top) {
        if (o == null) {
            System.out.print("null");
        } else if (o.getClass().isArray()) {
            System.out.print("[");
            for (int i = 0; i < Array.getLength(o); i++) {
                print(Array.get(o, i), false);
                if (i < Array.getLength(o) - 1)
                    System.out.print(", ");
            }
            System.out.print("]");
        } else {
            System.out.print(o);
        }
        if (top) System.out.println();
    }
    
    private void assertEquals(Object a, Object b) {
        if (a == null) {
            if (b != null) Assert.fail();
            return;
        }
        if (!a.getClass().equals(b.getClass()))
            Assert.fail();
        if (a.getClass().isArray()) {
            if (Array.getLength(a) != Array.getLength(b))
                Assert.fail();
            for (int i = 0; i < Array.getLength(a); i++) {
                assertEquals(Array.get(a, i), Array.get(b, i));
            }
        } else if (a instanceof List<?>) {
            Iterator<?> ia = ((List<?>) a).iterator();
            Iterator<?> ib = ((List<?>) b).iterator();
            for (; ia.hasNext();) {
                Object oa = ia.next();
                Object ob = ib.next();
                assertEquals(oa, ob);
            }
        } else if (a instanceof Map<?, ?>) {
            Map<?, ?> ma = (Map<?, ?>) a;
            Map<?, ?> mb = (Map<?, ?>) b;
            assertEquals(ma.keySet(), mb.keySet());
            for (Object key : ma.keySet()) {
                assertEquals(ma.get(key), mb.get(key));
            }
        } else {
            Assert.assertEquals(a, b);
        }
    }
    
    @Test public void testPrimitives() {
        assertSerializedUnserialized(true);
        assertSerializedUnserialized(false);
        
        assertSerializedUnserialized('c');
        assertSerializedUnserialized('B');
        assertSerializedUnserialized('!');
        assertSerializedUnserialized('\n');
        assertSerializedUnserialized('\uABCD');
        
        assertSerializedUnserialized((byte) 0xEF);
        assertSerializedUnserialized((byte) 0xA);
        
        assertSerializedUnserialized((short) 127);
        assertSerializedUnserialized((short) -52);
        
        assertSerializedUnserialized(3);
        assertSerializedUnserialized(-20);
        assertSerializedUnserialized(Integer.MAX_VALUE);
        
        assertSerializedUnserialized(32515113084L);
        assertSerializedUnserialized(-12309840192842938L);
        assertSerializedUnserialized(Long.MAX_VALUE);
        
        assertSerializedUnserialized(3.2);
        assertSerializedUnserialized(-20.5);
        assertSerializedUnserialized(Double.MAX_VALUE);
        
        assertSerializedUnserialized(3.2f);
        assertSerializedUnserialized(-20.5f);
    }
    
    @Test public void testStrings() {
        assertSerializedUnserialized("a");
        assertSerializedUnserialized("ABC");
        assertSerializedUnserialized("\n\t");
    }
    
    @Test public void testArrays() {
        assertSerializedUnserialized(new Object[] {});
        assertSerializedUnserialized(new Object[] { null });
        
        assertSerializedUnserialized(new int[] { 1, 2, 3 });
        assertSerializedUnserialized(new Integer[] { 1, 2, 3 });
        assertSerializedUnserialized(new String[] { "a", "b", "c" });
        
        assertSerializedUnserialized(new int[][] {{ 1, 2, 3 }, { 4, 5, 6 }});
        assertSerializedUnserialized(new Object[] { new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 } });
    }
    
    @Test public void testLists() {
        assertSerializedUnserialized(new ArrayList<String>(Arrays.asList("a", "b", "c")));
        assertSerializedUnserialized(new ArrayList<Integer>(Arrays.asList(1, 2, 3)));
        assertSerializedUnserialized(new ArrayList<Object>(Arrays.asList(new int[] { 1, 2, 3 }, null)));
        
        assertSerializedUnserialized(new LinkedList<String>(Arrays.asList("a", "b", "c")));
        assertSerializedUnserialized(new LinkedList<Integer>(Arrays.asList(1, 2, 3)));
        assertSerializedUnserialized(new LinkedList<Object>(Arrays.asList(new int[] { 1, 2, 3 }, null)));
    }
    
    @Test public void testMaps() {
        assertSerializedUnserialized(new HashMap<String, String>(ImmutableMap.<String, String>builder()
                .put("a", "b")
                .put("c", "d")
                .put("foo", "bar")
                .build()));
        
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put(1, new int [] { 1, 2, 3 });
        map.put(1.0, new LinkedList<Object>(Arrays.asList("x", "y", "z", null)));
        map.put('\n', null);
        assertSerializedUnserialized(map);
    }
    
    private static enum TestEnum {
        A, B, C, D;
    }
    
    @Test public void testEnums() {
        assertSerializedUnserialized(TestEnum.A);
        assertSerializedUnserialized(TestEnum.B);
        assertSerializedUnserialized(TestEnum.C);
        assertSerializedUnserialized(TestEnum.D);
    }
    
}
