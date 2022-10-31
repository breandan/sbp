// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.misc;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.util.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public abstract class StringWalker extends TreeWalker<String> {
    public void   walk(String tag) { }
    public Object walk(Tree<String> tree) {
        Object o = tree.head();
        if (!(o instanceof String)) o = null; /* FIXME */
        walk((String)o);
        return super.walk(tree);
    }
    public Object walk(String tag, Object[] tokens) {
        if (tokens.length==0) return tag;
        if (tag==null) return null;
        throw new Error("walker error: couldn't walk tag " + tag + " with " + tokens.length + " children");
    }
}
