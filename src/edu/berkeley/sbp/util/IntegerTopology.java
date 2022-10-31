// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.ref.*;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.util.*;

/** implementation of <tt>Topology</tt> for any class for which there is a mapping to the <tt>int</tt>s */
public class IntegerTopology<V> implements Topology<V>, Serializable {

    private final Range.Set rs;
    private final Functor<V,Integer> f;

    private int toInt(V v) { return f==null?((IntegerMappable)v).toInt():f.invoke(v); }

    public Range.Set getRanges()         { return new Range.Set(rs); }
    public Functor<V,Integer> functor() { return f; }

    public IntegerTopology(Functor<V,Integer> f)               { this(f, new Range.Set()); }
    public IntegerTopology(Functor<V,Integer> f, V a)          { this(f, f==null?((IntegerMappable)a).toInt():f.invoke(a)); }
    public IntegerTopology(Functor<V,Integer> f, V a, V b)     { this(f, f==null?((IntegerMappable)a).toInt():f.invoke(a),
                                                                      f==null?((IntegerMappable)b).toInt():f.invoke(b)); }
    public IntegerTopology(Functor<V,Integer> f, int a)        { this(f, a, a); }
    public IntegerTopology(Functor<V,Integer> f, int a, int b) { this(f, new Range(a, b)); }
    public IntegerTopology(Functor<V,Integer> f, Range r)      { this(f, new Range.Set(r)); }
    public IntegerTopology(Functor<V,Integer> f, Range.Set rs) {
        this.rs = rs;
        this.f = f==null?(this instanceof Functor ? (Functor)this : null):f;
    }
    
    public Topology<V> empty()              { return new IntegerTopology<V>(f);   }
        
    public boolean          contains(V v)              { return rs.contains(toInt(v)); }
        
    public Topology<V>      complement()               { return new IntegerTopology<V>(f, rs.complement()); }
    public Topology<V>      intersect(Topology<V> t)   { return new IntegerTopology<V>(f, rs.intersect(((IntegerTopology<V>)t.unwrap()).rs)); }
    public Topology<V>      minus(Topology<V> t)       { return new IntegerTopology<V>(f, rs.intersect(((IntegerTopology<V>)t.unwrap()).rs.complement())); }
    public Topology<V>      union(Topology<V> t)       { return new IntegerTopology<V>(f, rs.union(((IntegerTopology<V>)t.unwrap()).rs)); }
    public boolean          disjoint(Topology<V> t)    { return rs.intersect(((IntegerTopology<V>)t.unwrap()).rs).size()==0; }
    public boolean          containsAll(Topology<V> t) { return rs.containsAll(((IntegerTopology<V>)t.unwrap()).rs); }

    public Topology<V> unwrap() { return this; }
    public int     hashCode()                          { return rs.hashCode(); }
    public boolean equals(Object o)                    { return o!=null && o instanceof IntegerTopology && ((IntegerTopology<V>)o).rs.equals(rs); }

}
