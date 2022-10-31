// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.Sequence.Pos;
import java.io.*;
import java.util.*;

/**
 *  A collection of Elements and Sequences which only reference each other.
 *
 *  All analyses are done at the Grammar level, since a given
 *  Element/Sequence can appear in multiple Grammars.  Some of these
 *  analyses depend on which elements *reference* a given element,
 *  rather than which elements *are referenced by* a given element.
 *
 *  All members of this class are package-private because they are
 *  likely to change often.
 */
public abstract class Grammar<Token> {
    protected Union rootUnion;

    HashMap<Sequence, Topology> follow = new HashMap<Sequence, Topology>();
    HashSet<Sequence> followEof = new HashSet<Sequence>();
    final HashMap<SequenceOrElement,Boolean> possiblyEpsilon = new HashMap<SequenceOrElement,Boolean>();
    HashSet<SequenceOrElement> all = new HashSet<SequenceOrElement>();

    abstract Topology<Token> emptyTopology();
    Grammar(Union root) {
        this.rootUnion = root;
        if (root != null)
            for(Sequence s : root)
                buildFollowSet(s, emptyTopology(), true);
    }

    // Follow //////////////////////////////////////////////////////////////////////////////

    Topology follow(Sequence s) { return follow.get(s); }

    void buildFollowSet(Sequence seq, Topology followTopology, boolean eof)  {
        all.add(seq);
        Topology old = follow.get(seq);
        if (old==null) old = followTopology;
        else           old = old.union(followTopology);
        if (seq.follow != null) old = old.intersect(seq.follow.getTokenTopology());
        if (follow.get(seq) != null && follow.get(seq).containsAll(old) && (!eof || followEof.contains(seq)))
            return;
        follow.put(seq, old);
        if (eof) followEof.add(seq);

        for(Pos p = seq.firstp().last().prev(); p!=null; p = p.prev()) {
            all.add(p.element());
            if (p.element() instanceof Union)
                for(Sequence s2 : ((Union)p.element())) {
                    buildFollowSet(s2, followTopology, eof);
                    for(Sequence ss : s2.needs()) buildFollowSet(ss, followTopology, eof);
                    for(Sequence ss : s2.hates()) buildFollowSet(ss, followTopology, eof);
                }
            if (!possiblyEpsilon(p.element())) {
                followTopology = followTopology.empty();
                eof = false;
            }
            followTopology = followTopology.union(first(p.element(), new HashSet<Sequence>()));
        }
    }

    Topology epsilonFollowSet(Union u) {
        Topology ret = emptyTopology();
        for(Sequence s : u)
            ret = ret.union(epsilonFollowSet(s, new HashSet<Sequence>()));
        return ret;
    }
    Topology epsilonFollowSet(Sequence seq, HashSet<Sequence> seen) {
        Topology ret = seq.follow==null ? emptyTopology().complement() : seq.follow.getTokenTopology();
        if (seen.contains(seq)) return ret;
        seen.add(seq);
        for(Pos p = seq.firstp(); p!=null && !p.isLast(); p = p.next()) {
            if (!(p.element() instanceof Union)) continue;
            Topology t = emptyTopology();
            for(Sequence s : ((Union)p.element()))
                if (possiblyEpsilon(s))
                    t = t.union(epsilonFollowSet(s, seen));
            ret = ret.intersect(t);
        }
        return ret;
    }

    Topology first(SequenceOrElement e, HashSet<Sequence> seen) {
        if (e instanceof Atom) return ((Atom)e).getTokenTopology();
        Topology ret = emptyTopology();
        if (e instanceof Union) {
            for(Sequence s : ((Union)e)) ret = ret.union(first(s, seen));
        } else {
            Sequence seq = (Sequence)e;
            if (seen.contains(seq)) return emptyTopology();
            seen.add(seq);
            for(Pos p = seq.firstp(); p!=null && !p.isLast(); p = p.next()) {
                ret = ret.union(first(p.element(), seen));
                if (!possiblyEpsilon(p.element())) break;
            }
        }
        return ret;
    }

    // Epsilon-Ness //////////////////////////////////////////////////////////////////////////////

    final boolean possiblyEpsilon(SequenceOrElement e) {
        Boolean ret = possiblyEpsilon.get(e);
        if (ret != null) return ret.booleanValue();
        ret = possiblyEpsilon(e, new HashMap<SequenceOrElement,Boolean>());
        possiblyEpsilon.put(e, ret);
        return ret;
    }

    private boolean possiblyEpsilon(SequenceOrElement e, HashMap<SequenceOrElement,Boolean> hm) {
        if (hm.get(e) != null) return hm.get(e);
        hm.put(e, false);
        boolean ret = false;
        if      (e instanceof Atom)     ret = false;
        else if (e instanceof Union)    { for(Sequence s : (Union)e) ret |= possiblyEpsilon(s, hm); }
        else if (e instanceof Sequence) {
            ret = true;
            Sequence s = (Sequence)e;
            for(Pos p = s.firstp(); p!=null && !p.isLast(); p = p.next())
                ret &= possiblyEpsilon(p.element(), hm);
        }
        hm.put(e, ret);
        return ret;
    }

    boolean isRightNullable(Pos p) {
        if (p.isLast()) return true;
        if (!possiblyEpsilon(p.element())) return false;
        return isRightNullable(p.next());
    }

    boolean isNulledSubsequence(Sequence parent, Sequence potentialSubSequence) {
        HashSet<Sequence> eq = new HashSet<Sequence>();
        gatherNulledSubsequences(parent, eq);
        return eq.contains(potentialSubSequence);
    }
    private void gatherNulledSubsequences(Sequence seq, HashSet<Sequence> eq) {
        if (eq.contains(seq)) return;
        eq.add(seq);
        Pos p = seq.firstp();
        for(; !p.isLast(); p = p.next()) {
            if (!p.isLast() && isRightNullable(p.next()) && p.element() instanceof Union)
                for(Sequence s : ((Union)p.element()))
                    gatherNulledSubsequences(s, eq);
            if (!possiblyEpsilon(p.element())) break;
        }
    }

    // Pos Ordinality Comparisons //////////////////////////////////////////////////////////////////////////////

    boolean canKill(Pos mep, Pos himp) {
        if (!isRightNullable(mep))  return false;
        if (!isRightNullable(himp)) return false;
        Sequence me  = mep.owner();
        Sequence him = himp.owner();
        Boolean b = me.canKill.get(him);
        if (b!=null) return b;
        for(Sequence killer : him.hates())
            if (isNulledSubsequence(killer, me))
                { me.canKill.put(him, true); return true; }
        me.canKill.put(him, false);
        return false;
    }

    boolean canNeed(Pos mep, Pos himp) {
        if (!isRightNullable(mep))  return false;
        if (!isRightNullable(himp)) return false;
        Sequence me  = mep.owner();
        Sequence him = himp.owner();
        Boolean b = me.canNeed.get(him);
        if (b!=null) return b;
        for(Sequence needer : him.needs())
            if (isNulledSubsequence(needer, me))
                { me.canNeed.put(him, true); return true; }
        me.canNeed.put(him, false);
        return false;
    }

    int comparePositions(Pos position, Pos rposition) {
        int ret = 0;
        if (canKill(position, rposition) && canKill(rposition, position)) throw new Error();
        if      (canKill(position,  rposition))  ret = -1;
        else if (canKill(rposition, position))   ret =  1;
        else if (canNeed(position,  rposition))  ret = -1;
        else if (canNeed(rposition, position))   ret =  1;
        /*
        else if (isNulledSubsequence(position.owner(),  rposition.owner()) && isNulledSubsequence(rposition.owner(),  position.owner())) ret = 0;
        else if (isNulledSubsequence(position.owner(),  rposition.owner())) ret =  1;
        else if (isNulledSubsequence(rposition.owner(), position.owner()))  ret = -1;
        */
        return ret;
    }

}

