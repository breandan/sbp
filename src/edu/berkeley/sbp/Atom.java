// (C) 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp;
import edu.berkeley.sbp.util.*;

/**
 *  <font color=green>
 *  an element which matches some set of one-token-long input strings
 *  </font>.
 *
 *  <p>
 *  This class is a topology over itself (yes, this is impredicative).
 *  This means that you can call Atom.union(Atom) and get back an Atom.
 *  If you are interested in the topology of <i>tokens</i> which this
 *  Atom can match, use the <tt>getTokenTopology()</tt> method.
 *  </p>
 */
public abstract class Atom<Token>
    extends Element
    implements Topology<Atom<Token>> {

    /** the set (topology) of tokens that can match this element */
    public abstract Topology<Token>  getTokenTopology();

    StringBuffer toString(StringBuffer sb) { sb.append(this); return sb; }

}


