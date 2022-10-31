// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import java.util.*;

public final class FastSet<T> implements Iterator<T>, Iterable<T>, Visitable<T> {

    public static final int INITIAL_SIZE = 8;

    private       Object[] array = null;
    private       T        only  = null;
    private       int      i     = -1;
    private       int      size  = 0;

    public Iterator<T> iterator() { i=0; return this; }
    public void        remove()   { throw new Error(); }
    public boolean     hasNext()  { return only==null ? i<size : i<1; }
    public T           next()     { return only==null ? (T)array[i++] : (i++)==0 ? only : null; }
    public T get(int i) {
        if (i==0 && only!=null) return only;
        if (array==null) return null;
        return (T)array[i];
    }

    public T first() {
        if (only != null) return only;
        if (array != null) return (T)(array[0]);
        return null;
    }

    public FastSet() { }
    public FastSet(T t) { only = t; }
    public FastSet(Set<T> s) {
        if (s.size()==1) { only = s.iterator().next(); return; }
        array = new Object[s.size()];
        for(T t : s) array[size++] = t;
    }

    public void remove(T t) {
        if (only != null) {
            if (only==t) only=null;
            return;
        }
        boolean found = false;
        int j;
        for(j=0; j<size; j++)
            if (array[j]==t) {
                found = true;
                break;
            }
        if (!found) return;
        array[j] = array[size-1];
        array[size-1] = null;
        size--;
    }

    public <B,C> void visit(Invokable<T,B,C> ivbc, B b, C c) {
        if (only!=null) ivbc.invoke(only, b, c);
        else for(int j=0; j<size; j++)
                 ivbc.invoke((T)array[j], b, c);
    }

    public int size() { return only==null ? size : 1; }
    private void grow() {
        Object[] array2 = array==null ? new Object[INITIAL_SIZE] : new Object[array.length * 2];
        if (array != null) System.arraycopy(array, 0, array2, 0, array.length);
        array = array2;
        if (only!=null) {
            array[size++] = only;
            only = null;
        }
    }
    public void add(T t, boolean check) {
        //if (check) for(Object o : this) if (o.equals(t)) return;
        if (check) {
            if (only==t) return;
            if (array != null)
                for(int i=0; i<size; i++)
                    if (array[i]==t) return;
        }
        add(t);
    }
    public void add(T t) {
        if (array==null) {
            if (only!=null) { only = t; return; }
            grow();
        } else if (size>=array.length-1) {
            grow();
        }
        array[size++] = t;
    }

    public boolean contains(T t) {
        if (t==only) return true;
        if (array==null) return false;
        for(Object o : array) if (o==t) return true;
        return false;
    }
}
