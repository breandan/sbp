// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import java.util.*;

public final class SingletonIterator<T> implements Iterator<T>, Iterable<T> {

    private boolean spent = false;
    private final T t;

    public SingletonIterator(T t) { this.t = t; }

    public void    remove()       { throw new Error(); }
    public boolean hasNext()      { return !spent; }
    public T       next()         { spent = true; return t; }
    public Iterator<T> iterator() { return this; }
}
