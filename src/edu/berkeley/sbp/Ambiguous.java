// Copyright 2006-2007-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp;
import edu.berkeley.sbp.util.*;
import java.util.*;

/** thrown to signal that a parse was ambiguous */
public class Ambiguous extends Exception {

    private final Forest<?> ambiguity;
    private final HashSet<Tree<?>> possibilities;

    /**
     *  @param possibilities is a specially-constructed set of trees with shared nodes replaced by '*'
     */
    Ambiguous(Forest<?> ambiguity, HashSet<Tree<?>> possibilities) {
        this.ambiguity = ambiguity;
        this.possibilities = possibilities;
    }

    public Forest<?> getForest() { return ambiguity; }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("unresolved ambiguity at "+ambiguity.getRegion()+"; shared subtrees are shown as \"*\" ");
        for(Tree<?> result : possibilities) {
            sb.append("\n    possibility: ");
            StringBuffer sb2 = new StringBuffer();
            result.toPrettyString(sb2);
            sb.append(StringUtil.indent(sb2.toString(), 15));
        }
        return sb.toString();
    }
}
