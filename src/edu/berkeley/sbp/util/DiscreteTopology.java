// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.ref.*;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.*;

/** implementation of <tt>Topology</tt> for any class via equals()/hashCode() */
public class DiscreteTopology<V> implements Topology<V> {

    private static final DiscreteTopology empty = new DiscreteTopology();
    
    HashSet<V> hs = new HashSet<V>();

    public DiscreteTopology()              { }
    public DiscreteTopology(V v)           { hs.add(v); }
    DiscreteTopology(HashSet<V> hs)        { this.hs.addAll(hs); }

    public Topology<V> empty()             { return (Topology<V>)empty; }

    public HashSet<V> hs() {
        HashSet<V> ret = new HashSet<V>();
        ret.addAll(hs);
        return ret;
    }
        
    public boolean          contains(V v)              { return hs.contains(v); }

    public Topology<V> unwrap() { return this; }
    public Topology<V> complement()                    { throw new Error(); }
    public Topology<V> intersect(Topology<V> t)        { return new DiscreteTopology(hs().retainAll(((DiscreteTopology<V>)t.unwrap()).hs)); }
    public Topology<V> minus(Topology<V> t)            { return new DiscreteTopology(hs().removeAll(((DiscreteTopology<V>)t.unwrap()).hs)); }
    public Topology<V> union(Topology<V> t)            { return new DiscreteTopology(hs().addAll(((DiscreteTopology<V>)t.unwrap()).hs)); }
    public boolean          disjoint(Topology<V> t)    { return ((DiscreteTopology)intersect(t).unwrap()).size()==0; }
    public boolean          containsAll(Topology<V> t) { return hs.containsAll(((DiscreteTopology<V>)t.unwrap()).hs); }

    public int     hashCode()                          { return hs.hashCode(); }
    public boolean equals(Object o)                    { return o!=null && o instanceof DiscreteTopology && ((DiscreteTopology<V>)o).hs.equals(hs); }
    public int size() { return hs.size(); }
}
