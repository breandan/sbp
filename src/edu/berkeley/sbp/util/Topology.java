// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.ref.*;

/** values inhabiting a topology over <tt>V</tt> (roughly, infinite sets of <tt>V</tt>'s equipped with union/intersection/complement) */
public interface Topology<V> {

    public Topology<V>       unwrap();
    public Topology<V>       empty();

    public boolean           contains(V v);
    public boolean           disjoint(Topology<V> t);
    public boolean           containsAll(Topology<V> t);

    public Topology<V>       complement();
    public Topology<V>       intersect(Topology<V> t);
    public Topology<V>       minus(Topology<V> t);
    public Topology<V>       union(Topology<V> t);

    public abstract int     hashCode();
    public abstract boolean equals(Object o);
}
