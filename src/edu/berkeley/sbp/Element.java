// (C) 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp;
import java.util.*;

/**
 *  <font color=green>
 *  the root superclass for all components of the grammar (terminals,
 *  nonterminals, literals, etc)
 *  </font>
 */
public abstract class Element implements SequenceOrElement {

    /** sorry, you can't make up new, custom elements */
    Element() { }

    /** a more verbose version of toString() for displaying whole grammars */
    abstract StringBuffer toString(StringBuffer sb);

    /** a slow and inefficient epsilon-ness checker used when constructing parse trees (see Union.epsilonForm()) */
    public static boolean possiblyEpsilon(SequenceOrElement e) {
        if (e instanceof Atom) return false;
        if (e instanceof Sequence) {
            Sequence s = (Sequence)e;
            for(Sequence.Pos p = s.firstp(); !p.isLast(); p = p.next())
                if (!possiblyEpsilon(p.element()))
                    return false;
            return true;
        }
        if (e instanceof Union) {
            Union u = (Union)e;
            if (u.visiting) return true;
            try {
                u.visiting = true;
                for(Sequence s : u)
                    if (possiblyEpsilon(s))
                        return true;
                return false;
            } finally {
                u.visiting = false;
            }
        }
        throw new Error();
    }
    boolean visiting = false;
}
