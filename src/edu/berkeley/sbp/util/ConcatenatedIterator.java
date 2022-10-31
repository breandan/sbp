// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import java.util.*;

public final class ConcatenatedIterator<T> implements Iterator<T>, Iterable<T> {

    private final Iterator<Iterable<T>> ita;
    private       Iterator<T> cur;

    public ConcatenatedIterator(Iterator<Iterable<T>> ita) {
        this.ita = ita;
        cur = ita.hasNext() ? ita.next().iterator() : null;
    }

    public void    remove()       { throw new Error(); }
    public Iterator<T> iterator() { return this; }
    public boolean hasNext()      {
        while (cur!=null && !cur.hasNext())
            cur = ita.hasNext() ? ita.next().iterator() : null;
        return cur!=null && cur.hasNext();
    }
    public T       next()         {
        while (cur!=null && !cur.hasNext())
            cur = ita.hasNext() ? ita.next().iterator() : null;
        return cur==null ? null : cur.next();
    }
}
