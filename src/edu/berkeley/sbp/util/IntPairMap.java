// This was taken from org.ibex.util
//
// Copyright 2000-2005 the Contributors, as shown in the revision logs.
// Licensed under the Apache Public Source License 2.0 ("the License").
// You may not use this file except in compliance with the License.

package edu.berkeley.sbp.util;
import java.util.*;

// FEATURE: make this faster (plenty of ways: quadradic probing hash table is one)
/** a sparse mapping from pairs of <tt>int</tt>'s to <tt>V</tt>'s */
public final class IntPairMap<V> implements Iterable<V> {

    /** this object is inserted as key in a slot when the
     *  corresponding value is removed -- this ensures that the
     *  probing sequence for any given key remains the same even if
     *  other keys are removed.
     */
    // FIXME: this should have been static except that that makes it nonserializable
    private Object placeholder = new java.io.Serializable() { };

    /** the number of entries with at least one non-null key */
    private int usedslots = 0;

    /** the number of entries with non-null values */
    protected int size = 0;

    /** when num_slots < loadFactor * size, rehash into a bigger table */
    private final int loadFactor;

    /** primary keys */
    private int[] keys1 = null;

    /** secondary keys; null if no secondary key has ever been added */
    private int[] keys2 = null;

    /** the values for the table */
    private V[] vals = null;
    
    /** the number of entries with a non-null value */
    public int size() { return size; }

    public IntPairMap() { this(25, 3); }
    public IntPairMap(int initialcapacity, int loadFactor) {
        // using a pseudoprime in the form 4x+3 ensures full coverage
        initialcapacity = initialcapacity / 4;
        initialcapacity = 4 * initialcapacity + 3;
        keys1 = new int[initialcapacity];
        keys2 = new int[initialcapacity];
        vals = (V[])(new Object[initialcapacity]);
        this.loadFactor = loadFactor;
    }
    
    public void remove(int k1, int k2) { put_(k1, k2, null); }
    private void rehash() {
        int[] oldkeys1 = keys1;
        int[] oldkeys2 = keys2;
        V[] oldvals = vals;
        keys1 = new int[oldvals.length * 2];
        keys2 = new int[oldvals.length * 2];
        vals = (V[])(new Object[oldvals.length * 2]);
        size = 0;
        usedslots = 0;
        for(int i=0; i<oldvals.length; i++)
            if (oldvals[i] != null && oldvals[i] != placeholder)
                put_(oldkeys1[i], oldkeys2[i], oldvals[i]);
    }

    public V get(int k1, int k2) {
        int hash = k1 ^ k2;
        int dest = Math.abs(hash) % vals.length;
        int odest = dest;
        int tries = 1;
        boolean plus = true;
        while (vals[dest]!=null) {
            int hk1 = keys1[dest];
            int hk2 = keys2[dest];
            if (k1 == hk1 && k2 == hk2 && vals[dest]!=placeholder) return vals[dest];
            dest = Math.abs((odest + (plus ? 1 : -1 ) * tries * tries) % vals.length);
            if (plus) tries++;
            plus = !plus;
        }
        return null;
    }

    public void put(int k1, int k2, Object v) { put_(k1, k2, v); }
    private void put_(int k1, int k2, Object v) {
        if (usedslots * loadFactor > vals.length) rehash();
        int hash = k1 ^ k2;
        int dest = Math.abs(hash) % vals.length;
        int odest = dest;
        boolean plus = true;
        int tries = 1;
        while (true) {
            int hk1 = keys1[dest];
            int hk2 = keys2[dest];
            if (vals[dest]==null || (k1==hk1 && k2==hk2 && vals[dest]!=placeholder)) {
                if (v == null) {
                    if (vals[dest]==null) return;
                    v = placeholder;
                    size--;
                } else {
                    size++;
                    usedslots++;
                }
                break;
            }

            dest = Math.abs((odest + (plus ? 1 : -1 ) * tries * tries) % vals.length);
            if (plus) tries++;
            plus = !plus;
        }

        keys1[dest] = k1;
        keys2[dest] = k2;
        ((Object[])vals)[dest] = v;
    }

    private class IntPairMapIterator implements Iterator<V> {
        private int iterator = -1;
        private int found = 0;
        
        public void remove() { throw new Error(); }
        public boolean hasNext() { return found < size; }

        public V next() {
            if (!hasNext()) return null;
            V o = null;
            while (o==null || o==placeholder) o = vals[++iterator];
            found++;
            return o;
        }
    }
    public Iterator<V> iterator() { return new IntPairMapIterator(); }

    public void put(IntegerMappable k1, IntegerMappable k2, V v) {
        put((k1==null?0:k1.toInt()), (k2==null?0:k2.toInt()), v);
    }
    public V get(IntegerMappable k1, IntegerMappable k2) {
        return get((k1==null?0:k1.toInt()), (k2==null?0:k2.toInt()));
    }
    public void remove(IntegerMappable k1, IntegerMappable k2) {
        remove((k1==null?0:k1.toInt()), (k2==null?0:k2.toInt()));
    }
    //public Iterable<V> values() { return this; }
    //public void toArray(V[] v) { hm.values().toArray(v); }
    //public Iterator<V> iterator() { return hm.values().iterator(); }
}
