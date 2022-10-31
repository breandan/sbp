// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import java.util.*;

public final class ArrayIterator<T> implements Iterator<T>, Iterable<T> {

    private final T[] array;
    private int start;
    private int len;
    private int i = 0;

    public ArrayIterator(T[] array) { this(array, 0, array.length); }
    public ArrayIterator(T[] array, int start, int len) {
        this.start = start;
        this.len = len;
        this.array = array;
        this.i = start;
    }

    public void    remove()       { throw new Error(); }
    public boolean hasNext()      { return i<start+len; }
    public T       next()         { return array[i++]; }
    public Iterator<T> iterator() { return this; }
}
