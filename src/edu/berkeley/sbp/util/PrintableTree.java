// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.util.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public abstract class PrintableTree<T extends PrintableTree> implements Iterable<T>, ToJava /*, ToHTML*/, GraphViz.ToGraphViz {

    protected abstract Object head();
    protected abstract String headToString();
    protected abstract String headToJava();    
    protected abstract String left();
    protected abstract String right();
    protected abstract boolean ignoreSingleton();

    private static final int MAXCHARS=40;

    private boolean basic() { return toString().length() < MAXCHARS; }
    public String toPrettyString() { return toPrettyString("\n"); }
    public StringBuffer toPrettyString(StringBuffer sb) { sb.append(toPrettyString()); return sb; }
    private String toPrettyString(String nl) {
        String str = toString();
        if (str.length() < MAXCHARS) return str;
        String head = headToString();
        StringBuffer ret = new StringBuffer();

        Iterator<T> iterator = iterator();
        if (!iterator.hasNext()) return head==null ? (left()+right()) : head;
        PrintableTree t0 = iterator.next();
        if (!iterator.hasNext() && ignoreSingleton())
            return t0.toPrettyString(nl);

        ret.append(head==null?(left()+" "):(head+":"+nl));
        boolean first = true;
        int len = 0;
        for(T t : this) {
            String s = t.basic() ? t.toString() : t.toPrettyString(nl+"  ");
            if (!first) {
                if (!t.basic())                ret.append(nl);
                if (s.length()+len>MAXCHARS) { ret.append(nl); len = 0; }
                else                         { ret.append(" "); len++; }
            }
            first = false;
            ret.append(s);
            len += s.length();
        }
        if (head==null) ret.append(" "+right());
        return ret.toString();
    }

    private String cached = null;
    public String toString() {
        if (cached!=null) return cached;
        StringBuffer ret = new StringBuffer();
        int count=0;
        for(T t : this) {
            count++;
            String q = t==null ? "null" : t.toString();
            if (q.length() > 0) { ret.append(q); ret.append(" "); }
        }
        if (count==1 && ignoreSingleton()) { return cached = ret.toString().trim(); }
        String tail = ret.toString().trim();
        String head = headToString();
        String h = (head!=null && !head.toString().equals("")) ? (tail.length() > 0 ? head+":" : head+"") : "";
        if (tail.length() > 0) tail = left() + tail + right();
        return cached = h + tail;
    }

    /** append Java code to <tt>sb</tt> which evaluates to this instance */
    public void toJava(Appendable sb) throws IOException {
        sb.append("new "+this.getClass().getName()+"(null, ");
        String head = headToJava();
        sb.append(head);
        sb.append(", new "+this.getClass().getName()+"[] { ");
        boolean first = true;
        for(T t : this) {
            if (!first) sb.append(",\n        ");
            if (t==null)   sb.append("null");
            else           t.toJava(sb);
            first = false;
        }
        sb.append("})");
    }

    // this is here to keep it out of the javadoc for Tree<T>
    
    public GraphViz.StateNode toGraphViz(GraphViz gv) {
        if (gv.hasNode(this)) return gv.createNode(this);
        GraphViz.StateNode n = gv.createNode(this);
        n.label = head()==null ? "" : head().toString();
        for(T t : this) n.edge(t, null);
        return n;
    }
    public boolean isTransparent() { return false; }
    public boolean isHidden() { return false; }
}
