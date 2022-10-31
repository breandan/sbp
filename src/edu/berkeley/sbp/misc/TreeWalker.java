// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.misc;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.ref.*;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.*;

public abstract class TreeWalker<T> {
    public abstract Object walk(T head, Object[] args);
    public Object walk(Tree<T> tree, int idx) {
        return walk(tree.child(idx));
    }
    public void walkChildren(Tree<T> tree) {
        for(Tree<T> child : tree.children()) walk(child);        
    }
    public Object walk(Tree<T> tree) {
        Object[] args = new Object[tree.size()];
        int i = 0;
        for(Tree<T> child : tree.children()) args[i++] = walk(child);
        args = Reflection.lub(args);
        return walk(tree.head(), args);
    }
}
