// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import edu.berkeley.sbp.*;
import java.util.*;

public abstract class TreeBuilder<T> /*implements Invokable<Forest.Body<T>,Boolean,Integer>*/ {
    /*
    public ArrayList<Tree<T>> toks = new ArrayList<Tree<T>>();
    public boolean toss;
    protected T head;
    public TreeBuilder(boolean toss) { this.toss = toss; }
    public abstract void start(T head, Input.Region loc);
    public abstract void finish(T head, Input.Region loc);
    public abstract void addTree(Tree<T> t);
    public void invoke(Forest.Body<T> bod, Boolean o, Integer i) {
        if (i==null) {
            ArrayList<Tree<T>> toks = this.toks;
            this.toks = new ArrayList<Tree<T>>();
            bod.expand(0, this);
            this.toks = toks;
        } else {
            bod.expand(i, this);
        }
    }
    */
}
