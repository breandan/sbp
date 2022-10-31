// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import java.util.*;
import java.io.*;

/** a Map that knows how to apply an Invokable to all its elements */
public interface VisitableMap<K,V> extends Serializable {

    /** invokes <tt>ivbc</tt> on each element of this map, passing constant arguments <tt>b</tt> and <tt>c</tt> */
    public <B,C> void invoke(K k, Invokable<V,B,C> ivbc, B b, C c);

    /** returns true iff this map contains some value <tt>v</tt> for key <tt>k</tt> */
    public boolean contains(K k);

}
