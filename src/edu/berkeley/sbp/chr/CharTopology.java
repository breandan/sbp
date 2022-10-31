// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.chr;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.misc.*;
import edu.berkeley.sbp.util.*;

public class CharTopology extends IntegerTopology<Character> implements Functor<Character,Integer> {

    public CharTopology()               { super(null); }
    public CharTopology(Range.Set r)    { super(null, r); }
    public CharTopology(Topology<Character> it)  { this(((IntegerTopology<Character>)it.unwrap()).getRanges()); }
    public CharTopology(char a, char b) { super(null, a, b); }

    public Integer invoke(Character c) { return (int)c.charValue(); }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        Range.Set ranges = getRanges();
        if (ranges.size() == -1 || ranges.size() > Character.MAX_VALUE/2) {
            sb.append('~');
            ranges = ranges.complement();
        }
        sb.append('[');
        ranges = ranges.intersect(new Range.Set(new Range(0, Character.MAX_VALUE)));
        for(Range r : ranges) {
            if (r.isMinNegInf() || r.isMaxPosInf()) throw new Error("should not happen");
            if (r.getMin()==r.getMax()) {
                sb.append(esc((char)r.getMin()));
            } else{
                sb.append(esc((char)r.getMin()));
                sb.append('-');
                sb.append(esc((char)r.getMax()));
            }
        }
        sb.append(']');
        return sb.toString();
    }

    private String esc(char c) {
        if (c==CharAtom.left) return ">>";
        if (c==CharAtom.right) return "<<";
        return StringUtil.escapify(c+"", "[]-~\\\"\'\n\r\t");
    }

}
