// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.ref.*;

//
// This is a fairly crude implementation; if things need to be fast,
// you should implement a special-purpose class
//

/** a mapping from topologies over <tt>K</tt> to <i>sets of</i> values of type <tt>V</tt> */
public class TopologicalBag<K,V> implements MapBag<Topology<K>,V>, VisitableMap<K,V>, Serializable {

    // CRUCIAL INVARIANT: keys in this hashmap MUST be disjoint or the universe will implode
    private final HashMap<Topology<K>,HashSet<V>> h = new HashMap<Topology<K>,HashSet<V>>();

    public Iterator<Topology<K>> iterator() { return h.keySet().iterator(); }

    public void addAll(TopologicalBag<K,V> tb) {
        for(Topology<K> k : tb)
            addAll(k, tb.getAll(k));
    }

    public void add(Topology<K> t, V v) { put(t, v); }
    public void addAll(Topology<K> k, Iterable<V> vi) { putAll(k, vi); }

    public void putAll(Topology<K> k, Iterable<V> vi) { if (vi!=null) for(V v : vi) put(k, v); }
    public void put(Topology<K> t, V v) {
        for(Topology<K> ht : h.keySet()) {
            if (t.disjoint(ht)) continue;
            if (t.containsAll(ht) && ht.containsAll(t)) {
                h.get(ht).add(v);
                return;
            }
            if (ht.containsAll(t)) {
                HashSet<V> v0 = h.get(ht);
                HashSet<V> v1 = new HashSet<V>();
                v1.addAll(v0);
                h.remove(ht);
                v1.add(v);
                h.put(ht.intersect(t), v1);
                h.put(ht.minus(t), v0);
                return;
            }
            put(t.intersect(ht), v);
            put(t.minus(ht), v);
            return;
        }
        HashSet<V> v1 = new HashSet<V>();
        v1.add(v);
        h.put(t, v1);
    }


    public TopologicalBag<K,V> subset(Topology<K> t) {
        TopologicalBag<K,V> ret = new TopologicalBag<K,V>();
        for(Topology<K> ht : h.keySet()) {
            if (ht.disjoint(t)) continue;
            Iterable<V> it = h.get(ht);
            Topology<K> tk = ht.intersect(t);
            ret.putAll(tk, it);
        }
        return ret;
    }

    public Map<Topology<K>,HashSet<V>> gett(Topology<K> t) {
        HashMap<Topology<K>,HashSet<V>> ret = new HashMap<Topology<K>,HashSet<V>>();
        for(Topology<K> ht : h.keySet()) {
            if (ht.disjoint(t)) continue;
            ret.put(ht.intersect(t), h.get(ht));
        }
        return ret;
    }

    public boolean empty(K k) {
        for(Topology<K> t : h.keySet())
            if (t.contains(k) && h.get(t).size() > 0)
                return false;
        return true;
    }

    public boolean contains(K k) { return has(k); }

    public boolean contains(K k, V v) {
        for(Topology<K> t : h.keySet())
            if (t.contains(k))
                return h.get(t).contains(v);
        return false;
    }

    public boolean has(K k) {
        for(Topology<K> t : h.keySet())
            if (t.contains(k) && !h.get(t).isEmpty())
                return true;
        return false;
    }

    private HashMap<K,Iterable<V>> cache = new HashMap<K,Iterable<V>>();
    public Iterable<V> getAll(Topology<K> k) { return h.get(k); }

    public <B,C> void invoke(K k, Invokable<V,B,C> ivbc, B b, C c) {
        for(Topology<K> t : h.keySet())
            if (t.contains(k))
                for(V v : h.get(t))
                    ivbc.invoke(v, b, c);
    }

    public Iterable<V> get(K k) {
        Iterable<V> c = cache.get(k);
        if (c != null) return c;
        HashSet<V> ret = null;
        HashSet<V> ret0 = null;
        for(Topology<K> t : h.keySet())
            if (t.contains(k)) {
                if (ret==null) {
                    ret0 = ret = h.get(t);
                } else {
                    if (ret0 != null) {
                        ret = new HashSet<V>();
                        ret.addAll(ret0);
                        ret0 = null;
                    }
                    ret.addAll(h.get(t));
                }
            }
        if (ret==null) {
            cache.put(k, emptyIterator);
            return emptyIterator;
        } else {
            cache.put(k, new FastSet<V>(ret));
            return ret;
        }
    }

    public VisitableMap<K,V> optimize(final Functor<K,Integer> f) {
        ArrayList<Long> min_ = new ArrayList<Long>();
        ArrayList<Long> max_ = new ArrayList<Long>();
        ArrayList<Object[]> v_ = new ArrayList<Object[]>();
        for(Topology<K> t : h.keySet()) {
            ArrayList<V> al = new ArrayList<V>();
            for(V vv : h.get(t)) al.add(vv);
            Object[] vs = new Object[al.size()];
            al.toArray(vs);
            IntegerTopology it = (IntegerTopology)t;
            for(Range r : it.getRanges()) {
                min_.add(r.isMinNegInf() ? Long.MIN_VALUE : r.getMin());
                max_.add(r.isMaxPosInf() ? Long.MAX_VALUE : r.getMax());
                v_.add(vs);
            }
        }
        final int size = v_.size();
        final long[]     min = new long[size];     for(int i=0; i<min.length; i++) min[i]=min_.get(i);
        final long[]     max = new long[size];     for(int i=0; i<max.length; i++) max[i]=max_.get(i);
        final Object[][]   v = new Object[size][]; v_.toArray(v);
        return new VisitableMap<K,V>() {
            public boolean contains(K k) {
                int asint = f.invoke(k);
                for(int i=0; i<size; i++)
                    if (min[i] <= asint && max[i] >= asint && v[i].length > 0)
                        return true;
                return false;
            }
            public <B,C> void invoke(K k, Invokable<V,B,C> ivbc, B b, C c) {
                int asint = f.invoke(k);
                for(int i=0; i<size; i++) {
                    if (min[i] <= asint && max[i] >= asint) {
                        Object[] arr = v[i];
                        for(int j=0; j<arr.length; j++)
                            ivbc.invoke((V)arr[j], b, c);
                    }
                }
            }
        };
    }

    private final Iterable<V> emptyIterator = new EmptyIterator<V>();
}
