// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.ref.*;

/** <font color=green>juxtaposition; zero or more adjacent Elements; can specify a rewriting</font> */
public abstract class Sequence implements Iterable<Element>, SequenceOrElement, Serializable {

    protected transient final Element[] elements;

    boolean needed_or_hated = false;
    boolean in_a_union = false;

    final HashSet<Sequence> needs  = new HashSet<Sequence>();
    final HashSet<Sequence> hates  = new HashSet<Sequence>();

    // FIXME: these are ugly -- migrate into Grammar
    HashMap<Sequence,Boolean> canNeed = new HashMap<Sequence,Boolean>();
    HashMap<Sequence,Boolean> canKill = new HashMap<Sequence,Boolean>();

    final Position firstp;

    transient Atom follow = null;

    private static int global_sernum = 0;
    private int sernum = global_sernum++;
    int[] needs_int() {
        int[] ret = new int[needs.size()];
        int i = 0;
        for(Sequence s : needs) ret[i++] = s.sernum;
        return ret;
    }
    int[] hates_int() {
        int[] ret = new int[hates.size()];
        int i = 0;
        for(Sequence s : hates) ret[i++] = s.sernum;
        return ret;
    }


    // Static Constructors //////////////////////////////////////////////////////////////////////////////

    /** create a sequence of one element */
    public static Sequence create(Element e) { return create(new Element[] { e }, 0); }

    /** create a sequence which drops the result of all but one of its element */
    public static Sequence create(Element[] e, int which) {
        return new Singleton(e, which); }

    /** create a sequence which always evaluates to a constant result  */
    public static Sequence create(Object result, Element[] e) {
        return new RewritingSequence(result, e, trues(e.length)); }

    private static boolean[] trues(int length) {
        boolean[] ret = new boolean[length];
        for(int i=0; i<ret.length; i++) ret[i] = true;
        return ret;
    }

    /**
     *  create a sequence (general form)
     *  @param head   the head of the output tree
     *  @param e      the elements to match
     *  @param drop   only elements of <tt>e</tt> whose corresponding <tt>boolean</tt> in <tt>drops</tt>
     *                is <i>false</i> will be included in the output tree
     *  @param lifts  which (if any) child trees to lift
     **/
    public static Sequence create(Object head, Element[] e, boolean[] drop) {
        return create(head, e, drop, new boolean[e.length]); }
    public static Sequence create(Object head, Element[] e, boolean[] drop, boolean[] lifts) {
        if (lifts==null) lifts = new boolean[e.length];
        return new RewritingSequence(head, e, drop, lifts);
    }

    /** return a new sequence identical to this one, but with a positive conjunct <tt>s</tt> */
    public Sequence and(Sequence s) {
        if (s.in_a_union)
            throw new RuntimeException("you may not use a sequence as a conjunct if it belongs to a Union");
        Sequence ret = dup();
        ret.needs.add(s);
        s.needed_or_hated=true;
        return ret;
    }

    /** return a new sequence identical to this one, but with a negative conjunct <tt>s</tt> */
    public Sequence andnot(Sequence s) {
        if (s.in_a_union)
            throw new RuntimeException("you may not use a sequence as a conjunct if it belongs to a Union");
        Sequence ret = dup();
        ret.hates.add(s);
        s.needed_or_hated=true;
        return ret;
    }

    /** return a new sequence identical to this one, but with a follow-set restricted to <tt>a</tt> */
    public Sequence followedBy(Atom a) { Sequence ret = dup(); ret.follow = a; return ret; }

    ////////////////////////////////////////////////////////////////////////////////

    abstract Sequence _clone();
    private Sequence dup() {
        Sequence ret = _clone();
        for(Sequence s : needs) { ret.needs.add(s); }
        for(Sequence s : hates) { ret.hates.add(s); }
        ret.follow = follow;
        return ret;
    }

    Iterable<Sequence> needs() { return needs; }
    Iterable<Sequence> hates() { return hates; }

    Pos firstp() { return firstp; }
    Pos lastp() { return firstp().last(); }

    public Iterator<Element> iterator()    { return new ArrayIterator<Element>(elements); }
    protected Sequence(Element[] elements) {
        this.elements = elements;
        for(int i=0; i<elements.length; i++)
            if (elements[i]==null)
                throw new RuntimeException("cannot have nulls in a sequence: " + this);
        this.firstp = new Position(this, 0, null);
    }

    abstract Forest epsilonForm(Input.Region loc);

    protected abstract <T> Forest<T> postReduce(Input.Region loc, Forest<T>[] args, Position p);


    // Position //////////////////////////////////////////////////////////////////////////////

    static abstract class Pos implements IntegerMappable, Comparable<Pos>, Serializable {

        public int ord = -1;
        private Sequence owner;

        public int ord()     { return ord; }

        final Forest[] holder;

        Pos(int len, Sequence owner) {
            this.owner = owner;
            this.holder = new Forest[len];
        }

        public abstract int   provides();
        public abstract int[] needs();
        public abstract int[] hates();
        public abstract boolean            owner_needed_or_hated();

        public abstract boolean isFirst();
        public abstract boolean isLast();
        public abstract Pos last();
        public abstract Pos prev();
        public abstract Pos next();

        /** the element which produces the sequence to which this Position belongs */
        public Sequence owner() { return owner; }

        abstract Element  element();

        public abstract int numPops();
        public abstract <T> Forest<T> rewrite(Input.Region loc);
    }

    /** the imaginary position before or after an element of a sequence; corresponds to an "LR item" */
    private static class Position extends Pos implements IntegerMappable {
        /*
        public Pos getPos() {
            return new DumbPos(elements.length, provides(), needs(), hates(), owner_needed_or_hated(), numPops(), 
                public int   provides();
                public int[] needs();
                public int[] hates();
                public boolean            owner_needed_or_hated();
                public int numPops();
                public <T> Forest<T> rewrite(Input.Region loc)
            };
        }
        */
        public int numPops() { return pos; }

                final     int      pos;
        private final     Position next;
        private final     Position prev;

        public int     provides() { return owner().sernum; }
        public int[]   needs() { return owner().needs_int(); }
        public int[]   hates() { return owner().hates_int(); }
        public boolean owner_needed_or_hated() { return owner().needed_or_hated; }
        
        private Position(Sequence owner, int pos, Position prev) {
            super(owner.elements.length,owner);
            this.pos      = pos;
            this.next     = pos==owner.elements.length ? null : new Position(owner, pos+1, this);
            this.prev     = prev;
        }

        public int compareTo(Pos p) {
            return ord - ((Position)p).ord;
        }

        public boolean isFirst() { return pos==0; }
        public int pos() { return pos; }

        /** the element immediately after this Position, or null if this is the last Position */
        public Element  element() { return pos>=owner().elements.length ? null : owner().elements[pos]; }

        /** the next Position (the Position after <tt>this.element()</tt>) */
        public Position next() { return next; }

        /** true iff this Position is the last one in the sequence */
        public boolean isLast() { return next()==null; }
        public Position last() { return isLast() ? this : next().last(); }
        public Position prev() { return prev; }

        // Position /////////////////////////////////////////////////////////////////////////////////

        public final <T> Forest<T> rewrite(Input.Region loc) {
            if (isFirst()) return owner().epsilonForm(loc);
            for(int i=0; i<pos; i++) if (holder[i]==null) throw new Error("realbad " + i);
            Position p = this;
            for(int i=pos; p!=null && p.next()!=null; p=p.next(), i++) {
                if (holder[i]==null)
                    holder[i] = owner().elements==null
                        ? new Forest.Many() /* FIXME */
                        : ((Union)owner().elements[i]).epsilonForm(loc);
                if (holder[i]==null) throw new Error("bad");
            }
            return owner().postReduce(loc, holder, this);
        }

        public String   toString() {
            StringBuffer ret = new StringBuffer();
            ret.append("<{");
            for(Position p = (Position)owner().firstp(); p != null; p = p.next()) {
                ret.append(' ');
                if (p==this) ret.append(" | ");
                if (p.element()!=null) ret.append(p.element());
                else                   ret.append(' ');
            }
            ret.append("}>");
            return ret.toString();
        }
        private final int idx = master_position_idx++;
        public int toInt() { return idx; }
    }
    private static int master_position_idx = 0;


    // toString //////////////////////////////////////////////////////////////////////////////

    public String toString() { return toString(new StringBuffer(), false).toString(); }
    StringBuffer toString(StringBuffer sb) { return toString(sb, true); }
    StringBuffer toString(StringBuffer sb, boolean spacing) {
        for(int i=0; i<elements.length; i++) {
            sb.append(elements[i]+"");
            sb.append(' ');
        }
        if (follow != null) {
            sb.append("-> ");
            sb.append(follow);
        }
        for(Sequence s : needs) {
            sb.append("& ");
            sb.append(s);
        }
        for(Sequence s : hates) {
            sb.append("&~ ");
            sb.append(s);
        }
        return sb;
    }

    // Specialized Subclasses //////////////////////////////////////////////////////////////////////////////

    static class Singleton extends Sequence {
        private final int idx;
        public Singleton(Element e) { this(new Element[] { e }, 0); }
        public Singleton(Element[] e, int idx) { super(e); this.idx = idx; }
        public <T> Forest<T> postReduce(Input.Region loc, Forest<T>[] args, Position p) { return args[idx]; }
        Sequence _clone() { return new Singleton(elements,idx); }
        Forest epsilonForm(Input.Region loc) {
            if (elements==null) return new Forest.Many(); /* FIXME */
            return ((Union)elements[idx]).epsilonForm(loc);
        }
    }

    static class RewritingSequence extends Sequence {
        private final Object    tag;
        private final boolean[] drops;
        private final boolean[] lifts;
        Sequence _clone() { return new RewritingSequence(tag, elements, drops); }
        public RewritingSequence(Object tag, Element[] e) { this(tag, e, null); }
        public RewritingSequence(Object tag, Element[] e, boolean[] drops) { this(tag, e, drops, new boolean[e.length]); }
        public RewritingSequence(Object tag, Element[] e, boolean[] drops, boolean[] lifts) {
            super(e);
            if (tag==null) throw new Error();
            this.tag = tag;
            this.drops = drops == null ? new boolean[e.length] : drops;
            int count = 0;
            for(int i=0; i<this.drops.length; i++) if (!this.drops[i]) count++;
            this.lifts = new boolean[count];
            int j = 0;
            for(int i=0; i<this.drops.length; i++)
                if (!this.drops[i])
                    this.lifts[j++] = lifts[i];
        }
        public <T> Forest<T> postReduce(Input.Region loc, Forest<T>[] args, Position p) {
            Forest<T>[] args2 = new Forest[lifts.length];
            int j = 0;
            for(int i=0; i<args.length; i++) if (!drops[i]) args2[j++] = args[i];
            return Forest.create(loc, (T)tag, args2, lifts);
        }
        public StringBuffer toString(StringBuffer sb, boolean spacing) {
            int len = sb.length();
            if (tag != null)
                sb.append("\""+StringUtil.escapify(tag.toString(),"\"\r\n")+"\":: ");
            super.toString(sb, spacing);
            len = sb.length()-len;
            if (spacing) for(int i=0; i<50-len; i++) sb.append(' ');
            return sb;
        }
        Forest epsilonForm(Input.Region loc) {
            return Forest.create(loc, tag, new Forest[0], lifts);
        }
    }
}
