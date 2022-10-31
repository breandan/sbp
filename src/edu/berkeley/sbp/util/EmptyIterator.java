// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import java.util.*;
import java.io.*;

public final class EmptyIterator<T> implements Iterable<T>, Iterator<T>, Serializable {
    public void        remove()   { throw new Error(); }
    public boolean     hasNext()  { return false; }
    public T           next()     { throw new Error(); }
    public Iterator<T> iterator() { return this; }
}
