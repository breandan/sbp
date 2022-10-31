// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.chr;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.ref.*;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.misc.*;
import edu.berkeley.sbp.Input.Location;

public class CharAtom extends Atom<Character> {

    public CharAtom() { this(new CharTopology()); }
    public CharAtom(char a) { this(a,a); }
    public CharAtom(char a, char b) { this(new CharTopology(a, b)); }
    public CharAtom(CharTopology t) { this.t = t; }
    public CharAtom(Topology<Character> t) { this(t instanceof CharTopology ? (CharTopology)t : new CharTopology(t)); }

    private CharTopology t;
    public  Topology<Character> top() { return t; }

    public static final char left       = (char)9998;
    public static final char right      = (char)9999;
    public static final Atom leftBrace  = new CharAtom(left,left)   { public String toString() { return ">>"; } };
    public static final Atom rightBrace = new CharAtom(right,right) { public String toString() { return "<<"; } };
    //public static final Atom braces     = new CharAtom(left,right)   { public String toString() { return "[\\{\\}]"; } };

    public static Atom set(Range.Set r) { return new CharAtom(new CharTopology(r)); }
    public String toString() { return t.toString(); }

    /** returns an element which exactly matches the string given */
    public static Element string(String s) { return string(s, true); }
    public static Element string(String s, boolean share) {
        if (share && s.length() == 0) return emptyString;
        final String escapified = "\""+StringUtil.escapify(s, "\"\r\t\n\\")+"\"";
        Element ret;
        if (share && s.length() == 1) {
            ret =
                new CharAtom(s.charAt(0)) {
                    public String toString() { return escapified; } };
        } else {
            Union ret2 = new Union("\""+s+"\"_str", true) {
                    public String toString() { return escapified; } };
            Element[] refs = new Element[s.length()];
            for(int i=0; i<refs.length; i++) refs[i] = new CharAtom(s.charAt(i));
            ret2.add(Sequence.create(s, refs));
            ret = ret2;
        }
        return ret;
    }

    private static Union emptyString = new Union("()");
    static {
        // FIXME: force this to be dropped wherever used!
        emptyString.add(Sequence.create("", new Element[0]));
    }

    public Topology<Atom<Character>>       unwrap() { return this; }
    public Topology<Atom<Character>>       empty()  { return new CharAtom(); }
    public Topology<Character>             getTokenTopology()  { return top(); }

    public boolean           contains(Atom<Character> v) { return top().containsAll(((CharAtom)v).top()); }
    public boolean           disjoint(Topology<Atom<Character>> t) { return top().disjoint(((CharAtom)t).top()); }
    public boolean           containsAll(Topology<Atom<Character>> t) { return top().containsAll(((CharAtom)t).top()); }

    public Topology<Atom<Character>>       complement() { return new CharAtom(top().complement()); }
    public Topology<Atom<Character>>       intersect(Topology<Atom<Character>> t) { return new CharAtom(top().intersect(((CharAtom)t).top())); }
    public Topology<Atom<Character>>       minus(Topology<Atom<Character>> t) { return new CharAtom(top().minus(((CharAtom)t).top())); }
    public Topology<Atom<Character>>       union(Topology<Atom<Character>> t) { return new CharAtom(top().union(((CharAtom)t).top())); }

    public int     hashCode() { return top().hashCode(); }
    public boolean equals(Object o) { return o != null && (o instanceof CharAtom) && ((CharAtom)o).top().equals(top()); }

}
