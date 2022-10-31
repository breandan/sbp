// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.Parser.Table.*;
import edu.berkeley.sbp.Sequence.Pos;
import edu.berkeley.sbp.Sequence.Pos;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

abstract class Node<OtherNode extends Node>
    implements IntegerMappable,
               GraphViz.ToGraphViz,
               Iterable<OtherNode> {

    private final GSS.Phase phase;
    private final GSS.Phase predecessorPhase;
    protected boolean destroyed        = false;
    private int numNonDoomedSuccessors = 0;
    private boolean hasPathToRoot      = false;

    private       FastSet<OtherNode> predecessors = new FastSet<OtherNode>();
    private       FastSet<OtherNode> successors = new FastSet<OtherNode>();
    //protected       HashSet<OtherNode> predecessors = new HashSet<OtherNode>();
    //protected       HashSet<OtherNode> successors = new HashSet<OtherNode>();

    public GSS.Phase phase() { return phase; }
    public GSS.Phase predecessorPhase() { return predecessorPhase; }
    public boolean hasPredecessors() { return predecessors.size() > 0; }
    public boolean hasSuccessors() { return successors.size() > 0; }
    public boolean hasNonDoomedSuccessors() { return numNonDoomedSuccessors > 0; }

    public boolean hasPathToRoot() { return hasPathToRoot; }
    public void updatePathsToRootAfterRemoval() {
        if (!hasPathToRoot()) return;
        for(OtherNode on : predecessors)
            if (on.hasPathToRoot())
                return;
        hasPathToRoot = false;
        for(OtherNode on : successors)
            on.updatePathsToRootAfterRemoval();
    }

    /**
     *  Every Node has a phase; StateNodes belong to the phase in
     *  which they were shifted, ResultNodes belong to the phase of
     *  their predecessors.  All of the predecessors of a given node
     *  must belong to the same phase
     */
    public Node(GSS.Phase phase, GSS.Phase predecessorPhase) {
        this.phase = phase;
        this.predecessorPhase = predecessorPhase;
    }

    /** only to be invoked (or overridden as public) by the subclass */
    protected void addPred(OtherNode pred) {
        if (predecessors.contains(pred)) throw new Error("a predecessor was added twice");
        if (pred.phase() != predecessorPhase()) throw new Error();
        predecessors.add(pred);
        pred.addSucc(this);
        if (pred.hasPathToRoot()) hasPathToRoot = true;
    }
    public void addSucc(OtherNode succ) {
        if (successors.contains(succ)) throw new Error("a predecessor was added twice");
        successors.add(succ);
        numNonDoomedSuccessors += succ.isDoomedState() ? 0 : 1;
        if (predecessors.size() > 1 && (((Object)this) instanceof ResultNode)) throw new Error();
    }

    protected void destroy() {
        destroyed = true;
        while(predecessors.size() > 0) {
            OtherNode pred = predecessors.first();
            removePred(pred);
            pred.removeSucc(this);
        }
        predecessors = null;
        while(successors.size() > 0) {
            OtherNode succ = successors.first();
            removeSucc(succ);
            succ.removePred(this);
        }
        successors = null;
    }
    public void removeSucc(OtherNode succ) {
        if (!successors.contains(succ)) return;
        successors.remove(succ);
        numNonDoomedSuccessors -= succ.isDoomedState() ? 0 : 1;
        check();
    }
    public void removePred(OtherNode pred) {
        if (!predecessors.contains(pred)) return;
        predecessors.remove(pred);
        updatePathsToRootAfterRemoval();
        check();
    }


    public abstract boolean isDoomedState();
    protected abstract void check();

    public Iterator<OtherNode> iterator() { return predecessors.iterator(); }
    public Iterable<OtherNode> successors() { return successors; }

    public boolean noSuccessors() { return successors.size()==0; }
    public boolean predecessorsContains(OtherNode n) {
        return predecessors.contains(n);
    }

    // GraphViz //////////////////////////////////////////////////////////////////////////////

    public GraphViz.StateNode toGraphViz(GraphViz gv) {
        if (gv.hasNode(this)) return gv.createNode(this);
        GraphViz.StateNode n = gv.createNode(this);
        /*
        n.label = ""+f;
        n.shape = "rectangle";
        //if (pred()!=null) n.edge(pred, "");
        n.color = "blue";
        if (phase() != null)
            ((GraphViz.Group)phase().toGraphViz(gv)).add(n);
        n.label = "state["+state.toInt()+"]";
        n.shape = "rectangle";
        boolean haspreds = false;
        for(ResultNode r : results) n.edge(r, "");
        n.color = state.doomed ? "red" : "green";
        ((GraphViz.Group)phase().toGraphViz(gv)).add(n);
        */
        return n;
    }
    public boolean isTransparent() { return false; }
    public boolean isHidden() { return false; }

    // IntegerMappable ////////////////////////////////////////////////////////////

    private static int node_idx = 0;
    private final int idx = node_idx++;
    public int toInt() { return idx; }

}