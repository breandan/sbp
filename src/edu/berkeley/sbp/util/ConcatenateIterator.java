// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import java.util.*;

public final class ConcatenateIterator<T> implements Iterator<T>, Iterable<T> {

    private Iterator<T> i1;
    private Iterator<T> i2;

    public ConcatenateIterator(Iterator<T> i1, Iterator<T> i2) {
        this.i1 = i1;
        this.i2 = i2;
    }

    public void    remove()       { throw new Error(); }
    public boolean hasNext()      { return i1.hasNext() || i2.hasNext(); }
    public T       next()         { return i1.hasNext() ? i1.next() : i2.next(); }
    public Iterator<T> iterator() { return this; }
}
