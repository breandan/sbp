// Copyright 2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.meta;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.ref.*;

/**
 *  Currently this class exports only static methods to create repetitions;
 *  there are no public instance methods or constructors
 */
class Repeat extends Union {

    public Repeat(final Element e, boolean zeroOkay, boolean manyOkay, Object tag) {
        this(e, zeroOkay, manyOkay, null, false, tag); }
    public Repeat(final Element e, boolean zeroOkay, boolean manyOkay, final Element separator, Object tag) {
        this(e, zeroOkay, manyOkay, separator, false, tag); }
    protected Repeat(final Element e, boolean zeroOkay, boolean manyOkay, final Element separator, boolean maximal, Object tag) {
        this(e, zeroOkay, manyOkay, separator, maximal, tag, null); }
    protected Repeat(final Element e, boolean zeroOkay, boolean manyOkay, final Element separator, boolean maximal, Object tag, Atom follow) {
        super(e
              +(!manyOkay
                ? "?"
                : (zeroOkay
                   ? (maximal ? "**" : "*")
                   : (maximal ? "++" : "+")))
              +(separator==null
                ? ""
                : ("/"+separator)),
              true);
        if (follow != null) {
            Sequence s = Sequence.create(new Repeat(e, zeroOkay, manyOkay,
                                                    separator, maximal, tag, null)).followedBy(follow);
            add(s);
            return;
        }
        if (zeroOkay) 
            add(Sequence.create(tag, new Element[] {   }, null).followedBy(follow));
        if (!(zeroOkay && manyOkay))
            add(Sequence.create(tag, new Element[] { e }, null).followedBy(follow));

        // FEATURE: stringify ~[]* as ...
        if (zeroOkay && manyOkay && separator!=null) {
            add(Sequence.create(many1(e, separator, tag)).followedBy(follow));

        } else if (manyOkay) {
            if (separator==null)
                add(Sequence.create(tag,
                                    new Element[] { Repeat.this,    e },
                                    new boolean[] { false, false },
                                    new boolean[] { true, false }));
            else
                add(Sequence.create(tag,
                                    new Element[] { Repeat.this, separator, e },
                                    new boolean[] { false, true,  false },
                                    new boolean[] { true,  false, false }
                                    ));

        }
    }

    public static class Maximal extends Repeat {
        public Maximal(final Element e, boolean zeroOkay, boolean manyOkay, final Atom separator, Object tag) {
            super(e, zeroOkay, manyOkay, separator, true, tag, (Atom)separator.complement());
            if (zeroOkay && separator != null)
                throw new RuntimeException("cannot create a maximal repetition of zero or more items with a separator (yet): " + this);
        }
        public Maximal(final Atom e, boolean zeroOkay, boolean manyOkay, Object tag) {
            super(e, zeroOkay, manyOkay, null, true, tag, (Atom)e.complement());
        }
    }

    /** repeat zero or one times */
    public  static Element maybe(Element e)                             { return new Repeat(e, true, false, null, null); }
    public  static Element maybe(Element e, Object tag)                 { return new Repeat(e, true, false, null, tag); }
    /** repeat zero or more times */
    public  static Element many0(Element e)                             { return new Repeat(e, true, true, null, null); }
    public  static Element many0(Element e, Object tag)                 { return new Repeat(e, true, true, null, tag); }
    /** repeat zero or more times, separated by <tt>sep</tt> */
    public  static Element many0(Element e, Element sep)                { return new Repeat(e, true, true, sep, null); }
    public  static Element many0(Element e, Element sep, Object tag)    { return new Repeat(e, true, true, sep, tag); }
    /** repeat one or more times */
    public  static Element many1(Element e)                             { return new Repeat(e, false, true, null, null); }
    public  static Element many1(Element e, Object tag)                 { return new Repeat(e, false, true, null, tag); }
    /** repeat one or more times, separated by <tt>sep</tt> */
    public  static Element many1(Element e, Element sep)                { return new Repeat(e, false, true, sep, null); }
    public  static Element many1(Element e, Element sep, Object tag)    { return new Repeat(e, false, true, sep, tag); }

    /** repeat zero or more times, matching a maximal sequence of atoms */
    public  static Element maximal0(Atom e)                             { return new Repeat.Maximal(e, true, true, null); }
    public  static Element maximal0(Atom e, Object tag)                 { return new Repeat.Maximal(e, true, true, tag); }
    /** repeat one or more times, matching a maximal sequence of atoms */
    public  static Element maximal1(Atom e)                             { return new Repeat.Maximal(e, false, true, null); }
    public  static Element maximal1(Atom e, Object tag)                 { return new Repeat.Maximal(e, false, true, tag); }
    /** repeat one or more times, separated by an atom <tt>sep</tt>, matching a maximal sequence */
    public  static Element maximal1(Element e, Atom sep)                { return new Repeat.Maximal(e, false, true, sep, null); }
    public  static Element maximal1(Element e, Atom sep, Object tag)    { return new Repeat.Maximal(e, false, true, sep, tag); }

    public  static Element repeatMaximal(Atom e, boolean zero, boolean many, Object tag) {
        return new Repeat.Maximal(e, zero, many, tag); }
    public  static Element repeatMaximal(Element e, boolean zero, boolean many, Atom sep, Object tag) {
        return new Repeat.Maximal(e, zero, many, sep, tag); }
    public  static Element repeat(Element e, boolean zero, boolean many, Object tag) {
        return new Repeat(e, zero, many, tag); }
    public  static Element repeat(Element e, boolean zero, boolean many, Element sep, Object tag) {
        return new Repeat(e, zero, many, sep, tag); }
}
