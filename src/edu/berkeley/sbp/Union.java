// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.ref.*;

/**
 *  <font color=green>an element which can produce one of several alternatives</font>.
 *  <p>
 *
 *  Unlike the other Elements, Union is not immutable once
 *  constructed.  To simulate this desirable feature, it is immutable
 *  <i>once examined</i> by taking its iterator or calling contains().
 */
public class Union extends Element implements Iterable<Sequence> {

    private final String  name;
    private final boolean synthetic;
    private boolean viewed = false;

    private final List<Sequence> alternatives = new ArrayList<Sequence>();

    public Union(String name) { this(name, false); }
    public Union(String name, Sequence s) { this(name, s, false); }
    public Union(String name, Sequence s, boolean synthetic) { this(name, synthetic); add(s); }

    /**
     *  Since every cycle in a non-degenerate grammar contains at
     *  least one Union, every instance of this class must be able to
     *  display itself in both "long form" (list of the long forms of
     *  its alternatives) and "short form" (some name).
     *
     *  @param shortForm the "short form" display; for display purposes only
     *  @param synthetic if true, this Union's "long form" is "obvious" and should not be displayed when printing the grammar
     */
    public Union(String name, boolean synthetic) {
        this.name = name;
        this.synthetic = synthetic;
    }

    public boolean contains(Sequence s) {
        viewed = true;
        return alternatives.contains(s);
    }

    /** iterator over this Union's Sequences */
    public Iterator<Sequence> iterator() {
        viewed = true;
        return alternatives.iterator();
    }

    /** adds an alternative */
    public Union add(Sequence s) {
        if (viewed)
            throw new RuntimeException("once Union.contains() or Union.iterator() has been invoked, "+
                                       "you may not add any more Sequences to it\n  "+
                                       "  union in question: " + this);
        if (s.needed_or_hated)
            throw new RuntimeException("you may not add a conjunct directly to a Union");
        s.in_a_union = true;
        if (alternatives.contains(s)) return this;
        alternatives.add(s);
        return this;
    }

    /** adds a one-element sequence */
    public void add(Element e) {
        add(Sequence.create(e));
    }

    /** the Forest which results from matching this Union against the empty string at region <tt>region</tt> */
    Forest epsilonForm(Input.Region region) {
        viewed = true;
        Forest.Many epsilonForm = new Forest.Many();
        for(Sequence s : this)
            if (Element.possiblyEpsilon(s))
                epsilonForm.merge(s.epsilonForm(region));
        return epsilonForm;
    }


    // Display //////////////////////////////////////////////////////////////////////////////

    boolean isSynthetic() { return synthetic; }
    String getName()      { return name==null ? "(anon_union)" : name; }

    public String toString() {
        // technically this should be turned on, but we don't make a big deal
        //viewed = true;
        if (name != null) return name;
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        bodyToString(sb, "", " | ");
        sb.append(")");
        return sb.toString();
    }

    /** display this union in long/expanded form */
    public StringBuffer toString(StringBuffer sb) {
        // technically this should be turned on, but we don't make a big deal
        //viewed = true;
        if (synthetic) return sb;
        boolean first = true;
        String before = StringUtil.pad(15, getName()) + " = ";
        if (alternatives.size()==0) {
            sb.append(before);
        } else {
            bodyToString(sb,
                         before,
                         "\n" + StringUtil.pad(15, "")        + " | ");
            sb.append('\n');
        }
        return sb;
    }
    
    private void bodyToString(StringBuffer sb, String before, String between) {
        viewed = true;
        boolean first = true;
        for(Sequence s : this) {
            // FIXME: what to do here about printing out negated sequences?
            sb.append(first ? before : between);
            first = false;
            sb.append(s.toString());
        }
    }

}
